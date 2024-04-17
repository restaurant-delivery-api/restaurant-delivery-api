package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Menu;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MenuService {

    private MenuRepository menuRepository;

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

    public Menu getMenuById(Long id) {
        return getMenuByIdOrThrow(id);
    }

    public Menu addMenu(Menu menu) {
        nonExistOrThrow(menu);
        return menuRepository.save(menu);
    }

    public Menu updateMenu(Long id, Menu menu) {
        nonExistOrThrow(menu);
        return menuRepository.save(menu);
    }

    public void deleteMenuById(Long id) {
        menuRepository.deleteById(id);
    }
}
