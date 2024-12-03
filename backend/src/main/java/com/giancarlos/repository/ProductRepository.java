package com.giancarlos.repository;

import com.giancarlos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceLessThanEqual(BigDecimal price);

    List<Product> findByCategory(String categoryName);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE LOWER(p.name) " +
            "LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.category) " +
            "LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchByNameAndCategory(@Param("search") String search);
}
