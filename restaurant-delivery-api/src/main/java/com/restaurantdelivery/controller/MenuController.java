package com.restaurantdelivery.controller;

import com.restaurantdelivery.api.MenuApi;
import com.restaurantdelivery.dto.CategoryDto;
import com.restaurantdelivery.dto.MenuDto;
import com.restaurantdelivery.dto.ProductDto;
import com.restaurantdelivery.entity.Category;
import com.restaurantdelivery.entity.Menu;
import com.restaurantdelivery.entity.Product;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.MenuService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
public class MenuController implements MenuApi {

    private MenuService menuService;
    private ModelMapper menuMapper;

    public List<MenuDto> getMenus() {
        return menuService.getAllMenus().stream().map(this::convertToDto).toList();
    }

    public MenuDto getMenu(Long id) {
        return convertToDto(menuService.getMenuById(id));
    }

    public CategoryDto getCategory(Long menuId, Long categoryId) {
        return convertToDto(menuService.getCategoryFromMenuByIds(menuId, categoryId));
    }

    public MenuDto addMenu(MenuDto menuDto) {
        return convertToDto(menuService.addMenu(convertToEntity(menuDto)));
    }

    public CategoryDto addCategoryToMenu(Long menuId, CategoryDto categoryDto) {
        return convertToDto(menuService.addCategoryToMenu(menuId, convertToEntity(categoryDto)));
    }

    public ProductDto addProductToCategory(Long menuId,
                                           Long categoryId,
                                           ProductDto productDto) {
        return convertToDto(menuService.addProductToCategoryInMenu(menuId, categoryId, convertToEntity(productDto)));
    }

    public MenuDto updateMenu(Long id,
                              MenuDto menuDto) {
        if (!Objects.equals(id, menuDto.getId())) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(menuService.updateMenu(id, convertToEntity(menuDto)));
    }

    public CategoryDto updateCategoryInMenu(Long menuId,
                                            Long categoryId,
                                            CategoryDto categoryDto) {
        return convertToDto(menuService.updateCategoryInMenu(menuId, categoryId, convertToEntity(categoryDto)));
    }

    public ProductDto updateProductInCategory(Long menuId,
                                              Long categoryId,
                                              Long productId,
                                              ProductDto productDto) {
        return convertToDto(menuService.updateProductInCategory(menuId, categoryId, productId, convertToEntity(productDto)));
    }

    public void deleteMenu(Long id) {
        menuService.deleteMenuById(id);
    }


    public void deleteCategory(Long menuId,
                               Long categoryId) {
        menuService.deleteCategoryByIdFromMenu(menuId, categoryId);
    }


    public void deleteProduct(Long menuId,
                              Long categoryId,
                              Long productId) {
        menuService.deleteProductByIdFromCategory(menuId, categoryId, productId);
    }

    private MenuDto convertToDto(Menu menu) {
        return menuMapper.map(menu, MenuDto.class);
    }

    private CategoryDto convertToDto(Category category) {
        return menuMapper.map(category, CategoryDto.class);
    }

    private ProductDto convertToDto(Product product) {
        return menuMapper.map(product, ProductDto.class);
    }

    private Menu convertToEntity(MenuDto menuDto) {
        return menuMapper.map(menuDto, Menu.class);
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        return menuMapper.map(categoryDto, Category.class);
    }

    private Product convertToEntity(ProductDto productDto) {
        return menuMapper.map(productDto, Product.class);
    }
}
