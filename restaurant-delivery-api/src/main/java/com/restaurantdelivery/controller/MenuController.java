package com.restaurantdelivery.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private MenuService menuService;
    private ModelMapper menuMapper;

    @GetMapping("/all")
    @ResponseBody
    public List<MenuDto> getMenus() {
        return menuService.getAllMenus().stream().map(this::convertToDto).toList();
    }

    // get menu and all its inners
    @GetMapping("/{id}")
    @ResponseBody
    public MenuDto getMenu(@PathVariable("id") Long id) {
        return convertToDto(menuService.getMenuById(id));
    }

    // get category and its products
    @GetMapping("/{menu_id}/{category_id}")
    @ResponseBody
    public CategoryDto getCategory(@PathVariable("menu_id") Long menuId, @PathVariable("category_id") Long categoryId) {
        return convertToDto(menuService.getCategoryFromMenuByIds(menuId, categoryId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MenuDto addMenu(@RequestBody MenuDto menuDto) {
        return convertToDto(menuService.addMenu(convertToEntity(menuDto)));
    }

    @PostMapping("/{menu_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CategoryDto addCategoryToMenu(@PathVariable("menu_id") Long menuId, @RequestBody CategoryDto categoryDto) {
        return convertToDto(menuService.addCategoryToMenu(menuId, convertToEntity(categoryDto)));
    }

    @PostMapping("/{menu_id}/{category_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ProductDto addProductToCategory(@PathVariable("menu_id") Long menuId,
                                           @PathVariable("category_id") Long categoryId,
                                           @RequestBody ProductDto productDto) {
        return convertToDto(menuService.addProductToCategory(menuId, categoryId, convertToEntity(productDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MenuDto updateMenu(@PathVariable("id") Long id,
                              @RequestBody MenuDto menuDto) {
        if (!Objects.equals(id, menuDto.getId()))
        {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(menuService.updateMenu(id, convertToEntity(menuDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("id") Long id) {
        menuService.deleteMenuById(id);
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
