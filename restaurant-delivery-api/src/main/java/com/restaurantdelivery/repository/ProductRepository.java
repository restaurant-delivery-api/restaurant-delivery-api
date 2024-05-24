package com.restaurantdelivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.restaurantdelivery.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseOrInfoContainingIgnoreCase(String name, String info);

    default List<Product> findBySearchTerms(List<String> searchTerms) {
        if (searchTerms == null || searchTerms.isEmpty()) {
            return Collections.emptyList();
        }

        String combinedSearchTerm = String.join(" ", searchTerms);
        List<Product> products = findByNameContainingIgnoreCaseOrInfoContainingIgnoreCase(combinedSearchTerm, combinedSearchTerm);

        Map<Product, Integer> productMatchCountMap = new HashMap<>();

        for (String term : searchTerms) {
            for (Product product : products) {
                int matchCount = countMatches(product.getName(), term) + countMatches(product.getInfo(), term);
                productMatchCountMap.put(product, productMatchCountMap.getOrDefault(product, 0) + matchCount);
            }
        }

        List<Product> sortedProducts = productMatchCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedProducts;
    }

    private int countMatches(String str, String term) {
        String[] words = str.split("\\s+");
        int count = 0;
        for (String word : words) {
            if (word.equalsIgnoreCase(term)) {
                count++;
            }
        }
        return count;
    }
}


/*
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:searchTerms IS NULL OR " +
            "(p.name IS NOT NULL AND LOWER(p.name) LIKE CONCAT('%', LOWER(:searchTerms), '%')) OR " +
            "(p.info IS NOT NULL AND LOWER(p.info) LIKE CONCAT('%', LOWER(:searchTerms), '%'))) " +
            "GROUP BY p " +
            "ORDER BY COUNT(*) DESC")
    Page<Product> findBySearchTerms(@Param("searchTerms") List<String> searchTerms, Pageable pageable);
}
*/