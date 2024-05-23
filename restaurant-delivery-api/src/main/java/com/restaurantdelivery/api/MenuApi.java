package com.restaurantdelivery.api;

import com.restaurantdelivery.configuration.Constants;
import com.restaurantdelivery.dto.CategoryDto;
import com.restaurantdelivery.dto.MenuDto;
import com.restaurantdelivery.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/menu")
public interface MenuApi {


    @GetMapping("/all")
    @ResponseBody
    List<MenuDto> getMenus();

    // get menu and all its inners
    @GetMapping("/{id}")
    @ResponseBody
    MenuDto getMenu(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id);

    // get category and its products
    @GetMapping("/{menu_id}/category/{category_id}")
    @ResponseBody
    CategoryDto getCategory(@PathVariable("menu_id") Long menuId, @PathVariable("category_id") Long categoryId);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    MenuDto addMenu(@RequestBody MenuDto menuDto);

    @PostMapping("/{menu_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    CategoryDto addCategoryToMenu(@PathVariable("menu_id") Long menuId, @RequestBody CategoryDto categoryDto);

    @PostMapping("/{menu_id}/category/{category_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    ProductDto addProductToCategory(@PathVariable("menu_id") Long menuId,
                                    @PathVariable("category_id") Long categoryId,
                                    @RequestBody ProductDto productDto);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    MenuDto updateMenu(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id,
                       @RequestBody MenuDto menuDto);

    @PutMapping("/{menu_id}/category/{category_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CategoryDto updateCategoryInMenu(@PathVariable("menu_id") Long menuId,
                                            @PathVariable("category_id") Long categoryId,
                                            @RequestBody CategoryDto categoryDto);

    @PutMapping("/{menu_id}/category/{category_id}/product/{product_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ProductDto updateProductInCategory(@PathVariable("menu_id") Long menuId,
                                       @PathVariable("category_id") Long categoryId,
                                       @PathVariable("product_id") Long productId,
                                       @RequestBody ProductDto productDto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMenu(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id);

    @DeleteMapping("/{menu_id}/category/{category_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable("menu_id") Long menuId,
                        @PathVariable("category_id") Long categoryId);

    @DeleteMapping("/{menu_id}/category/{category_id}/product/{product_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProduct(@PathVariable("menu_id") Long menuId,
                       @PathVariable("category_id") Long categoryId,
                       @PathVariable("product_id") Long productId);

}
