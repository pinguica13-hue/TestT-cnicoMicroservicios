package com.example.msinventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class Inventario 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    // Getters y Setters
    public Long getId() 
    { 
        return id; 
        }
    public void setId(Long id) 
    { 
        this.id = id; 
        }

    public Long getProductoId() 
    { 
        return productoId; 
        }
    public void setProductoId(Long productoId) 
    { this.productoId = productoId; 
        }

    public Integer getCantidad() 
    { 
        return cantidad; 
        }
    public void setCantidad(Integer cantidad) 
    { 
        this.cantidad = cantidad; 
        }
}
