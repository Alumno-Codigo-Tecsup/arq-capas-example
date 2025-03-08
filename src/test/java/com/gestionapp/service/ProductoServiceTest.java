package com.gestionapp.service;

import com.gestionapp.dto.ProductoDTO;
import com.gestionapp.exception.ResourceNotFoundException;
import com.gestionapp.model.Producto;
import com.gestionapp.repository.ProductoRepository;
import com.gestionapp.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop HP");
        producto.setDescripcion("Laptop HP 15 pulgadas 8GB RAM");
        producto.setPrecio(new BigDecimal("899.99"));
        producto.setStock(10);
        producto.setCategoria("Electrónica");
        producto.setFechaCreacion(now);

        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Laptop HP");
        productoDTO.setDescripcion("Laptop HP 15 pulgadas 8GB RAM");
        productoDTO.setPrecio(new BigDecimal("899.99"));
        productoDTO.setStock(10);
        productoDTO.setCategoria("Electrónica");
        productoDTO.setFechaCreacion(now);
    }

    @Test
    void getAllProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));

        List<ProductoDTO> result = productoService.getAllProductos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(producto.getId(), result.get(0).getId());
        assertEquals(producto.getNombre(), result.get(0).getNombre());
    }

    @Test
    void getProductoById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoDTO result = productoService.getProductoById(1L);

        assertNotNull(result);
        assertEquals(producto.getId(), result.getId());
        assertEquals(producto.getNombre(), result.getNombre());
    }

    @Test
    void getProductoById_NotFound() {
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.getProductoById(1L));
    }

    @Test
    void createProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoDTO result = productoService.createProducto(productoDTO);

        assertNotNull(result);
        assertEquals(producto.getId(), result.getId());
        assertEquals(producto.getNombre(), result.getNombre());
    }

    @Test
    void updateProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoDTO result = productoService.updateProducto(1L, productoDTO);

        assertNotNull(result);
        assertEquals(productoDTO.getNombre(), result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void updateProducto_NotFound() {
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.updateProducto(1L, productoDTO));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void deleteProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        doNothing().when(productoRepository).delete(any(Producto.class));

        productoService.deleteProducto(1L);

        verify(productoRepository, times(1)).delete(producto);
    }

    @Test
    void deleteProducto_NotFound() {
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.deleteProducto(1L));
        verify(productoRepository, never()).delete(any(Producto.class));
    }

    @Test
    void searchProductos() {
        when(productoRepository.findByNombreContaining("Laptop"))
                .thenReturn(Arrays.asList(producto));

        List<ProductoDTO> result = productoService.searchProductos("Laptop");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(producto.getId(), result.get(0).getId());
        assertEquals(producto.getNombre(), result.get(0).getNombre());
    }

    @Test
    void getProductosByCategoria() {
        when(productoRepository.findByCategoria("Electrónica"))
                .thenReturn(Arrays.asList(producto));

        List<ProductoDTO> result = productoService.getProductosByCategoria("Electrónica");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(producto.getId(), result.get(0).getId());
        assertEquals("Electrónica", result.get(0).getCategoria());
    }

    @Test
    void getProductosByPriceRange() {
        BigDecimal min = new BigDecimal("500.00");
        BigDecimal max = new BigDecimal("1000.00");

        when(productoRepository.findByPrecioBetween(min, max))
                .thenReturn(Arrays.asList(producto));

        List<ProductoDTO> result = productoService.getProductosByPriceRange(min, max);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(producto.getId(), result.get(0).getId());
        assertEquals(0, producto.getPrecio().compareTo(result.get(0).getPrecio()));
    }
}