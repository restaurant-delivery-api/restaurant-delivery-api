package com.restaurantdelivery.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.restaurantdelivery.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.restaurantdelivery.repository.ProductRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Value("${upload.path}")
    private String uploadPath;

    private final ProductRepository productRepository;

    public Product getById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {return productRepository.save(product);}

    public void delete(Product product) {
        for (String filename : product.getPhotoList()) {
            String filePath = uploadPath + "/" + filename;
            File fileToDelete = new File(filePath);
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
        }
        productRepository.deleteById(product.getId());
    }

    public List<Product> findFirst3BySearchTerms(List<String> words) {
        return productRepository.findBySearchTerms(words);
    }
}