package com.example.msinventario.service;

import com.example.msinventario.client.ProductoClient;
import com.example.msinventario.entity.Inventario;
import com.example.msinventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventarioService {
    private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;

    public InventarioService(InventarioRepository inventarioRepository, ProductoClient productoClient) {
        this.inventarioRepository = inventarioRepository;
        this.productoClient = productoClient;
    }

    /**
     * Consulta el inventario de un producto específico.
     */
    public Optional<Inventario> getInventarioPorProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    /**
     * Actualiza o crea el inventario de un producto.
     */
    public Inventario actualizarInventario(Long productoId, Integer nuevaCantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setProductoId(productoId);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        inventario.setCantidad(nuevaCantidad);
        return inventarioRepository.save(inventario);
    }

    /**
     * Verifica si el producto existe en MSProductos.
     */
    public boolean existeProducto(Long productoId) {
        try {
            productoClient.getProducto(productoId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Registra una compra y descuenta la cantidad del inventario.
     */
    @Transactional
    public Inventario registrarCompra(Long productoId, int cantidad) {
        // Validar existencia en MSProductos
        if (!existeProducto(productoId)) {
            throw new RuntimeException("El producto no existe en MSProductos");
        }

        // Obtener inventario
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("El producto no existe en inventario"));

        // Validar stock
        if (inventario.getCantidad() < cantidad) {
            throw new RuntimeException("Inventario insuficiente");
        }

        // Actualizar stock
        inventario.setCantidad(inventario.getCantidad() - cantidad);
        return inventarioRepository.save(inventario);
    }
}
