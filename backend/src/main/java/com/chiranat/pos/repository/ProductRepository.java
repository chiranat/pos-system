package com.chiranat.pos.repository;

import com.chiranat.pos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryIdAndIsAvailableTrue(UUID categoryId);
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByIsAvailableTrue();
}
