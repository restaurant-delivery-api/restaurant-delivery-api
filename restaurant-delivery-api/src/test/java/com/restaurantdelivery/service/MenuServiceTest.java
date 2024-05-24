package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Category;
import com.restaurantdelivery.entity.Menu;
import com.restaurantdelivery.entity.Product;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.CategoryRepository;
import com.restaurantdelivery.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMenus() {
        Menu menu1 = new Menu();
        Menu menu2 = new Menu();
        List<Menu> menuList = Arrays.asList(menu1, menu2);

        when(menuRepository.findAll()).thenReturn(menuList);

        List<Menu> result = menuService.getAllMenus();

        assertEquals(2, result.size());
        assertEquals(menuList, result);
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    void testGetMenuByIdOrThrow() {
        Menu menu = new Menu();
        menu.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        Menu result = menuService.getMenuByIdOrThrow(1L);

        assertEquals(menu, result);
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMenuByIdOrThrow_NotFound() {
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        ServerException exception = assertThrows(ServerException.class, () -> {
            menuService.getMenuByIdOrThrow(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Menu with id 1 does not exist", exception.getMessage());
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow() {
        Menu menu = new Menu();
        menu.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> menuService.nonExistOrThrow(menu));
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow_AlreadyExists() {
        Menu menu = new Menu();
        menu.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        ServerException exception = assertThrows(ServerException.class, () -> {
            menuService.nonExistOrThrow(menu);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Menu with id 1 already exists", exception.getMessage());
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void testAddMenu() {
        Menu menu = new Menu();
        menu.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.empty());
        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.addMenu(menu);

        assertEquals(menu, result);
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    void testAddCategoryToMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setCategories(new ArrayList<>());

        Category category = new Category();
        category.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(menu)).thenReturn(menu);

        Category result = menuService.addCategoryToMenu(1L, category);

        assertTrue(menu.getCategories().contains(category));
        assertEquals(category, result);
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).saveAndFlush(menu);
    }


    @Test
    void testAddProductToCategoryInMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        Category category = new Category();
        category.setId(1L);

        menu.setCategories(Arrays.asList(category));
        Product product = new Product();
        product.setId(1L);
        category.setProducts(new ArrayList<>());

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(menu)).thenReturn(menu);

        Product result = menuService.addProductToCategoryInMenu(1L, 1L, product);

        assertTrue(category.getProducts().contains(product));
        assertEquals(product, result);
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).saveAndFlush(menu);
    }

    @Test
    void testUpdateMenu() {
        Menu menu = new Menu();
        menu.setId(1L);

        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.updateMenu(1L, menu);

        assertEquals(menu, result);
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    void testUpdateCategoryInMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        Category category = new Category();
        category.setId(1L);
        menu.setCategories(Arrays.asList(category));
        Category updCategory = new Category();
        updCategory.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(menu)).thenReturn(menu);

        Category result = menuService.updateCategoryInMenu(1L, 1L, updCategory);

        assertTrue(menu.getCategories().contains(updCategory));
        assertEquals(updCategory, result);
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).saveAndFlush(menu);
    }

    @Test
    void testUpdateProductInCategory() {
        Menu menu = new Menu();
        menu.setId(1L);
        Category category = new Category();
        category.setId(1L);
        Product product = new Product();
        product.setId(1L);
        category.setProducts(Arrays.asList(product));
        menu.setCategories(Arrays.asList(category));
        Product updProduct = new Product();
        updProduct.setId(1L);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(menu)).thenReturn(menu);

        Product result = menuService.updateProductInCategory(1L, 1L, 1L, updProduct);

        assertTrue(category.getProducts().contains(updProduct));
        assertEquals(updProduct, result);
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).saveAndFlush(menu);
    }

    @Test
    void testDeleteMenuById() {
        doNothing().when(menuRepository).deleteById(1L);

        menuService.deleteMenuById(1L);

        verify(menuRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategoryByIdFromMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        Category category = new Category();
        category.setId(1L);
        menu.setCategories(Arrays.asList(category));

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(menu)).thenReturn(menu);

        menuService.deleteCategoryByIdFromMenu(1L, 1L);

        assertFalse(menu.getCategories().contains(category));
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).saveAndFlush(menu);
    }

    @Test
    void testDeleteProductByIdFromCategory() {
        Menu menu = new Menu();
        menu.setId(1L);
        Category category = new Category();
        category.setId(1L);
        Product product = new Product();
        product.setId(1L);
        category.setProducts(Arrays.asList(product));
        menu.setCategories(Arrays.asList(category));

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.saveAndFlush(menu)).thenReturn(menu);

        menuService.deleteProductByIdFromCategory(1L, 1L, 1L);

        assertFalse(category.getProducts().contains(product));
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).saveAndFlush(menu);
    }
}
