package com.example.msinventario.service;

import com.example.msinventario.entity.Compra;
import com.example.msinventario.entity.Inventario;
import com.example.msinventario.repository.CompraRepository;
import com.example.msinventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    private InventarioRepository inventarioRepository;
    private CompraRepository compraRepository;
    private RestTemplate restTemplate;
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        inventarioRepository = mock(InventarioRepository.class);
        compraRepository = mock(CompraRepository.class);
        restTemplate = mock(RestTemplate.class);

        compraService = new CompraService(inventarioRepository, compraRepository, restTemplate);
    }

    @Test
    void testRealizarCompra_Success() {
        // Preparar datos
        Long productoId = 1L;
        int cantidad = 2;

        Inventario inventario = new Inventario();
        inventario.setProductoId(productoId);
        inventario.setCantidad(10);

        CompraService.ProductoDTO productoDTO = new CompraService.ProductoDTO(productoId, "Producto A", 50.0);

        // Mock
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));
        when(restTemplate.getForObject("http://msproductos:8080/productos/" + productoId, CompraService.ProductoDTO.class))
                .thenReturn(productoDTO);

        ArgumentCaptor<Inventario> inventarioCaptor = ArgumentCaptor.forClass(Inventario.class);
        ArgumentCaptor<Compra> compraCaptor = ArgumentCaptor.forClass(Compra.class);

        when(compraRepository.save(any(Compra.class))).thenAnswer(i -> i.getArgument(0));

        // Ejecutar
        var response = compraService.realizarCompra(
                new com.example.msinventario.descuento.CompraRequest(productoId, cantidad)
        );

        // Verificar
        verify(inventarioRepository).save(inventarioCaptor.capture());
        verify(compraRepository).save(compraCaptor.capture());

        Inventario savedInventario = inventarioCaptor.getValue();
        Compra savedCompra = compraCaptor.getValue();

        assertEquals(8, savedInventario.getCantidad()); // 10 - 2
        assertEquals(50.0, savedCompra.getPrecioUnitario());
        assertEquals(100.0, savedCompra.getTotal());

        assertEquals(productoId, response.getProductoId());
        assertEquals("Producto A", response.getNombreProducto());
        assertEquals(cantidad, response.getCantidad());
        assertEquals(50.0, response.getPrecioUnitario());
        assertEquals(100.0, response.getTotal());
    }

    @Test
    void testRealizarCompra_InventarioInsuficiente() {
        Long productoId = 1L;
        int cantidad = 5;

        Inventario inventario = new Inventario();
        inventario.setProductoId(productoId);
        inventario.setCantidad(3);

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));

        Exception exception = assertThrows(RuntimeException.class, () ->
                compraService.realizarCompra(new com.example.msinventario.descuento.CompraRequest(productoId, cantidad))
        );

        assertEquals("Inventario insuficiente", exception.getMessage());
    }
}