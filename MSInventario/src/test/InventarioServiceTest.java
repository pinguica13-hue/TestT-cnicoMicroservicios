package com.example.msinventario.service;

import com.example.msinventario.client.ProductoClient;
import com.example.msinventario.entity.Inventario;
import com.example.msinventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioServiceTest {

    private final InventarioRepository repo = mock(InventarioRepository.class);
    private final ProductoClient productoClient = mock(ProductoClient.class);
    private final InventarioService service = new InventarioService(repo, productoClient);

    @Test
    void actualizarInventarioNuevo() {
        when(repo.findByProductoId(1L)).thenReturn(Optional.empty());
        when(repo.save(any(Inventario.class))).thenAnswer(i -> i.getArguments()[0]);

        Inventario inventario = service.actualizarInventario(1L, 10);

        assertEquals(1L, inventario.getProductoId());
        assertEquals(10, inventario.getCantidad());
    }

    @Test
    void existeProductoTrue() {
        when(productoClient.getProducto(1L)).thenReturn(Map.of("id", 1L, "nombre", "Laptop"));
        assertTrue(service.existeProducto(1L));
    }

    @Test
    void existeProductoFalse() {
        when(productoClient.getProducto(99L)).thenThrow(new RuntimeException("No encontrado"));
        assertFalse(service.existeProducto(99L));
    }
}
