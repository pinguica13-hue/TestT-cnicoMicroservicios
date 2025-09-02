package com.example.msproductos.Service;

import com.example.msproductos.entity.Producto;
import com.example.msproductos.repository.ProductoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public Producto crear(Producto p) {
        return repo.save(p);
    }

    public Optional<Producto> obtener(Long id) {
        return repo.findById(id);
    }

    public List<Producto> listar() {
        return repo.findAll();
    }
}