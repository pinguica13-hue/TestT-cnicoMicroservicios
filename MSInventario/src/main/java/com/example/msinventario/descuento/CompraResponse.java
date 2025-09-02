package com.example.msinventario.descuento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompraResponse 
{
    private Long compraId;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
}
