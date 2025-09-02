package com.example.msinventario.controller;

import com.example.msinventario.entity.Inventario;
import com.example.msinventario.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Test
    void testGetInventarioPorProducto_Encontrado() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setCantidad(10);

        Mockito.when(inventarioService.getInventarioPorProducto(100L))
                .thenReturn(Optional.of(inventario));

        mockMvc.perform(get("/inventario/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(100))
                .andExpect(jsonPath("$.cantidad").value(10));
    }

    @Test
    void testGetInventarioPorProducto_NoEncontrado() throws Exception {
        Mockito.when(inventarioService.getInventarioPorProducto(200L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/inventario/200"))
                .andExpect(status().isNotFound());
    }
}
