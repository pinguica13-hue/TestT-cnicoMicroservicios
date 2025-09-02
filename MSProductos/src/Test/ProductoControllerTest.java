package com.example.msproductos.controller;

import com.example.msproductos.model.Producto;
import com.example.msproductos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository repo;

    @Test
    void crearProducto() throws Exception {
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Mouse\",\"precio\":50.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mouse"));
    }
}
