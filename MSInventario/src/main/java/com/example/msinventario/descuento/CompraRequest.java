package com.example.msinventario.descuento;

import lombok.Data;

@Data
public class CompraRequest 
{
    private Long productoId;
    private Integer cantidad;
}
