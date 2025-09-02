package com.example.msinventario.repository;

import com.example.msinventario.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> 
{
    Optional<Inventario> findByProductoId(Long productoId);
}
