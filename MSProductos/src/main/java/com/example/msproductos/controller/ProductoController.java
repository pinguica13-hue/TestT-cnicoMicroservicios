package com.example.msproductos.controller;

import com.example.msproductos.Service.ProductoService;
import com.example.msproductos.entity.Producto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/productos")
public class ProductoController 
{
    private final ProductoService service;

    @Value("${ms.api.key:}")
    private String serviceApiKey; // si está vacío no se obliga para llamada externa

    public ProductoController(ProductoService service) 
    {
        this.service = service;
    }

    private boolean validApiKey(String key) 
    {
    if (serviceApiKey == null || serviceApiKey.isBlank()) return true;
    return serviceApiKey.equals(key);
    }

    @PostMapping
    public ResponseEntity<Object> crearProducto(@RequestHeader(value = "X-API-KEY", required = false) String apiKey,
    @RequestBody Producto p) 
    {
    if (!validApiKey(apiKey)) 
        {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    .body(Map.of("errors", List.of(Map.of("detail","API key inválida"))));
        }

        Producto nuevo = service.crear(p);
        Map<String,Object> resource = Map.of(
            "type","producto",
            "id", nuevo.getId().toString(),
            "attributes", Map.of(
                "nombre", nuevo.getNombre(),
                "precio", nuevo.getPrecio(),
                "descripcion", nuevo.getDescripcion()
                 )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("data", resource));
     }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerProducto(@PathVariable Long id,
    @RequestHeader(value = "X-API-KEY", required = false) String apiKey) 
    {
    if (!validApiKey(apiKey)) 
        {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("errors", List.of(Map.of("detail","API key inválida"))));
        }

        return service.obtener(id)
        .<ResponseEntity<Object>>map(p ->
        {
            Map<String,Object> resource = Map.of(
                "type","producto",
                "id", p.getId().toString(),
                "attributes", Map.of(
                    "nombre", p.getNombre(),
                    "precio", p.getPrecio(),
                    "descripcion", p.getDescripcion()
                 )   
            );
        return ResponseEntity.ok(Map.of("data", resource));
        })
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("errors", List.of(Map.of("detail","Producto no encontrado")))));
    }

    @GetMapping
    public ResponseEntity<Object> listarProductos() 
    {
        var list = service.listar();
        List<Map<String,Object>> resources = new ArrayList<>();
        for (Producto p : list) 
        {
            resources.add(Map.of(
                "type","producto",
                "id", p.getId().toString(),
                "attributes", Map.of(
                    "nombre", p.getNombre(),
                    "precio", p.getPrecio(),
                    "descripcion", p.getDescripcion()
                )
            ));
        }
    return ResponseEntity.ok(Map.of("data", resources));
    }
}
