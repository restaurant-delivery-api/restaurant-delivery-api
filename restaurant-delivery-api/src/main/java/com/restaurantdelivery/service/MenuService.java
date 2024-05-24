package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Category;
import com.restaurantdelivery.entity.Menu;
import com.restaurantdelivery.entity.Product;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.CategoryRepository;
import com.restaurantdelivery.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MenuService {

    private MenuRepository menuRepository;
    public CategoryRepository categoryRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu getMenuByIdOrThrow(Long id) {
        return menuRepository.findById(id).orElseThrow(() ->
                new ServerException(HttpStatus.NOT_FOUND,
                        "Menu with id " + id + " does not exist")
        );
    }

    public void nonExistOrThrow(Menu menu) {
        menuRepository.findById(menu.getId()).ifPresent(usr -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Menu with id " + usr.getId() + " already exists");
        });
    }

    private Category getCategoryFromMenuById(Menu menu, Long categoryId) {
        List<Category> category = menu.getCategories().stream().filter((cat -> cat.getId().equals(categoryId))).toList();
        if (category.size() > 1) {
            throw new ServerException(HttpStatus.CONFLICT,
                    "Got more than one category with the same ids");
        }
        return category.get(0);
    }

    private Product getProductFromCategoryById(Category category, Long productId) {
        List<Product> product = category.getProducts().stream().filter((prod -> prod.getId().equals(productId))).toList();
        if (product.size() > 1) {
            throw new ServerException(HttpStatus.CONFLICT,
                    "Got more than one category with the same ids");
        }
        return product.get(0);
    }

    public Menu getMenuById(Long id) {
        return getMenuByIdOrThrow(id);
    }

    public Category getCategoryFromMenuByIds(Long menuId, Long categoryId) {
        Menu menu = getMenuByIdOrThrow(menuId);
        return getCategoryFromMenuById(menu, categoryId);
    }

    public Menu addMenu(Menu menu) {
        nonExistOrThrow(menu);
        return menuRepository.save(menu);
    }

    public Category addCategoryToMenu(Long menuId, Category category) {
        Menu menu = getMenuByIdOrThrow(menuId);
        menu.getCategories().add(category);
        menuRepository.saveAndFlush(menu);
        return category;
    }

    public Product addProductToCategoryInMenu(Long menuId, Long categoryId, Product product) {
        Menu menu = getMenuByIdOrThrow(menuId);
        Category category = getCategoryFromMenuById(menu, categoryId);
        category.getProducts().add(product);
        menuRepository.saveAndFlush(menu);
        return product;
    }

    public Menu updateMenu(Long id, Menu menu) {
        return menuRepository.save(menu);
    }


    public Category updateCategoryInMenu(Long menuId, Long categoryId, Category updCategory) {
        Menu menu = getMenuByIdOrThrow(menuId);
        List<Category> categories = new ArrayList<>(menu.getCategories()); // Create a modifiable list

        categories.removeIf(cat -> cat.getId().equals(categoryId)); // Remove the old category
        categories.add(updCategory); // Add the updated category

        menu.setCategories(categories); // Update the categories in the menu
        menuRepository.saveAndFlush(menu); // Save the changes

        return updCategory;
    }

    public Product updateProductInCategory(Long menuId, Long categoryId, Long productId, Product updProduct) {
        Menu menu = getMenuByIdOrThrow(menuId);
        Category category = getCategoryFromMenuById(menu, categoryId);

        List<Product> products = new ArrayList<>(category.getProducts());

        products.removeIf(prod -> prod.getId().equals(productId));
        products.add(updProduct);
        category.setProducts(products);

        menuRepository.saveAndFlush(menu);
        return updProduct;
    }


    public void deleteMenuById(Long id) {
        menuRepository.deleteById(id);
    }

    public void deleteCategoryByIdFromMenu(Long menuId, Long categoryId) {
        Menu menu = getMenuByIdOrThrow(menuId);
        List<Category> categories = new ArrayList<>(menu.getCategories()); // Create a modifiable list
        categories.removeIf(category -> category.getId().equals(categoryId)); // Remove the category
        menu.setCategories(categories);
        menuRepository.saveAndFlush(menu);
    }


    public void deleteProductByIdFromCategory(Long menuId, Long categoryId, Long productId) {
        Menu menu = getMenuByIdOrThrow(menuId);
        Category category = getCategoryFromMenuById(menu, categoryId);
        List<Product> products = new ArrayList<>(category.getProducts()); // Create a modifiable list
        products.removeIf(product -> product.getId().equals(productId)); // Remove the product
        category.setProducts(products);
        menuRepository.saveAndFlush(menu);
    }
}
