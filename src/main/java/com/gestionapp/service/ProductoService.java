package com.gestionapp.service;

import com.gestionapp.dto.ProductoDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    List<ProductoDTO> getAllProductos();
    ProductoDTO getProductoById(Long id);
    ProductoDTO createProducto(ProductoDTO productoDTO);
    ProductoDTO updateProducto(Long id, ProductoDTO productoDTO);
    void deleteProducto(Long id);
    List<ProductoDTO> searchProductos(String nombre);
    List<ProductoDTO> getProductosByCategoria(String categoria);
    List<ProductoDTO> getProductosByPriceRange(BigDecimal min, BigDecimal max);
}