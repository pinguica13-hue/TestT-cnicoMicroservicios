package com.example.msinventario.service;

public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precio;

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor con parámetros
    public ProductoDTO(Long id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(Double precio) { this.precio = precio; }
}