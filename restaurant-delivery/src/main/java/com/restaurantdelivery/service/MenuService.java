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

    public Menu updateMenu(Long id, Menu menu) {
        return menuRepository.save(menu);
    }

    public void deleteMenuById(Long id) {
        menuRepository.deleteById(id);
    }
}
