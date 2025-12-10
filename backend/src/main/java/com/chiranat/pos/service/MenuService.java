package com.chiranat.pos.service;

import com.chiranat.pos.model.Category;
import com.chiranat.pos.model.Product;
import com.chiranat.pos.repository.CategoryRepository;
import com.chiranat.pos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    public List<Product> getAvailableProducts() {
        return productRepository.findByIsAvailableTrue();
    }
}
