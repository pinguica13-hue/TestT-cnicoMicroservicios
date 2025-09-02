package com.example.msinventario.service;

import com.example.msinventario.descuento.CompraRequest;
import com.example.msinventario.descuento.CompraResponse;
import com.example.msinventario.entity.Compra;
import com.example.msinventario.entity.Inventario;
import com.example.msinventario.repository.CompraRepository;
import com.example.msinventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final InventarioRepository inventarioRepository;
    private final CompraRepository compraRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public CompraResponse realizarCompra(CompraRequest request) {

        // 1. Validar inventario
        Optional<Inventario> invOpt = inventarioRepository.findByProductoId(request.getProductoId());
        if (invOpt.isEmpty()) {
            throw new RuntimeException("Producto no existe en inventario");
        }

        Inventario inventario = invOpt.get();
        if (inventario.getCantidad() < request.getCantidad()) {
            throw new RuntimeException("Inventario insuficiente");
        }

        // 2. Consultar MSProductos
        String url = "http://msproductos:8080/productos/" + request.getProductoId();
        ProductoDTO producto = restTemplate.getForObject(url, ProductoDTO.class);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado en MSProductos");
        }

        // 3. Actualizar inventario
        inventario.setCantidad(inventario.getCantidad() - request.getCantidad());
        inventarioRepository.save(inventario);

        // 4. Registrar compra
        Compra compra = Compra.builder()
                .productoId(request.getProductoId())
                .cantidad(request.getCantidad())
                .precioUnitario(producto.getPrecio())
                .total(producto.getPrecio() * request.getCantidad())
                .fechaCompra(LocalDateTime.now())
                .build();

        compra = compraRepository.save(compra);

        // 5. Respuesta
        return CompraResponse.builder()
                .compraId(compra.getId())
                .productoId(producto.getId())
                .nombreProducto(producto.getNombre())
                .cantidad(compra.getCantidad())
                .precioUnitario(compra.getPrecioUnitario())
                .total(compra.getTotal())
                .build();
    }
}