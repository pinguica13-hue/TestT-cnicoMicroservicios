package com.example.msinventario.controller;

import com.example.msinventario.descuento.CompraRequest;
import com.example.msinventario.descuento.CompraResponse;
import com.example.msinventario.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraController 
{
    private final CompraService compraService;
    @PostMapping
    public ResponseEntity<CompraResponse> comprar(@Valid @RequestBody CompraRequest request) 
    {
        CompraResponse response = compraService.realizarCompra(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
