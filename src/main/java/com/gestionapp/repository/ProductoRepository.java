package com.gestionapp.repository;

import com.gestionapp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);
}