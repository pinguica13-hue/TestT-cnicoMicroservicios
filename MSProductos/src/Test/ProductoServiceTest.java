package com.example.msproductos.service;

import com.example.msproductos.model.Producto;
import com.example.msproductos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    private final ProductoRepository repo = Mockito.mock(ProductoRepository.class);
    private final ProductoService service = new ProductoService(repo);

    @Test
    void crearProducto() {
        Producto p = new Producto();
        p.setNombre("Laptop");
        p.setPrecio(2000.0);

        when(repo.save(p)).thenReturn(p);

        Producto resultado = service.crear(p);
        assertEquals("Laptop", resultado.getNombre());
        assertEquals(2000.0, resultado.getPrecio());
    }

    @Test
    void obtenerProductoPorId() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Teclado");

        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Optional<Producto> resultado = service.obtener(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Teclado", resultado.get().getNombre());
    }

    @Test
    void listarProductos() {
        when(repo.findAll()).thenReturn(List.of(new Producto(), new Producto()));
        assertEquals(2, service.listar().size());
    }
}
