package com.gestionapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionapp.dto.ProductoDTO;
import com.gestionapp.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoDTO productoDTO;
    private List<ProductoDTO> productoDTOList;

    @BeforeEach
    void setUp() {
        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Laptop HP");
        productoDTO.setDescripcion("Laptop HP 15 pulgadas 8GB RAM");
        productoDTO.setPrecio(new BigDecimal("899.99"));
        productoDTO.setStock(10);
        productoDTO.setCategoria("Electrónica");
        productoDTO.setFechaCreacion(LocalDateTime.now());

        ProductoDTO productoDTO2 = new ProductoDTO();
        productoDTO2.setId(2L);
        productoDTO2.setNombre("Monitor Dell");
        productoDTO2.setDescripcion("Monitor Dell 24 pulgadas Full HD");
        productoDTO2.setPrecio(new BigDecimal("199.99"));
        productoDTO2.setStock(15);
        productoDTO2.setCategoria("Electrónica");
        productoDTO2.setFechaCreacion(LocalDateTime.now());

        productoDTOList = Arrays.asList(productoDTO, productoDTO2);
    }

    @Test
    void getAllProductos() throws Exception {
        when(productoService.getAllProductos()).thenReturn(productoDTOList);

        mockMvc.perform(get("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laptop HP"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Monitor Dell"));
    }

    @Test
    void getProductoById() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(productoDTO);

        mockMvc.perform(get("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"))
                .andExpect(jsonPath("$.precio").value(899.99))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void createProducto() throws Exception {
        when(productoService.createProducto(any(ProductoDTO.class))).thenReturn(productoDTO);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"))
                .andExpect(jsonPath("$.precio").value(899.99));
    }

    @Test
    void updateProducto() throws Exception {
        when(productoService.updateProducto(eq(1L), any(ProductoDTO.class))).thenReturn(productoDTO);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"))
                .andExpect(jsonPath("$.precio").value(899.99));
    }

    @Test
    void deleteProducto() throws Exception {
        doNothing().when(productoService).deleteProducto(1L);

        mockMvc.perform(delete("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchProductos() throws Exception {
        when(productoService.searchProductos("Laptop")).thenReturn(Arrays.asList(productoDTO));

        mockMvc.perform(get("/api/productos/search")
                        .param("nombre", "Laptop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laptop HP"));
    }

    @Test
    void getProductosByCategoria() throws Exception {
        when(productoService.getProductosByCategoria("Electrónica")).thenReturn(productoDTOList);

        mockMvc.perform(get("/api/productos/categoria/Electrónica")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoria").value("Electrónica"))
                .andExpect(jsonPath("$[1].categoria").value("Electrónica"));
    }

    @Test
    void getProductosByPriceRange() throws Exception {
        when(productoService.getProductosByPriceRange(
                new BigDecimal("100.00"), new BigDecimal("1000.00")))
                .thenReturn(productoDTOList);

        mockMvc.perform(get("/api/productos/precio")
                        .param("min", "100.00")
                        .param("max", "1000.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}