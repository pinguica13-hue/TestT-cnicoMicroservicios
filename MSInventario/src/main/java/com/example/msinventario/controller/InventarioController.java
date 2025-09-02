package com.example.msinventario.controller;

import com.example.msinventario.entity.Inventario;
import com.example.msinventario.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/inventario")
public class InventarioController 
{

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) 
    {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<?> getInventario(@PathVariable Long productoId) 
    {
        Optional<Inventario> inv = inventarioService.getInventarioPorProducto(productoId);
        if (inv.isPresent()) {
            return ResponseEntity.ok(inv.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{productoId}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long productoId,
                                                  @RequestParam Integer cantidad) 
    {
        if (!inventarioService.existeProducto(productoId)) 
        {
            return ResponseEntity.badRequest().body("El producto no existe en MSProductos");
        }
        Inventario actualizado = inventarioService.actualizarInventario(productoId, cantidad);
        return ResponseEntity.ok(actualizado);
    }
}
