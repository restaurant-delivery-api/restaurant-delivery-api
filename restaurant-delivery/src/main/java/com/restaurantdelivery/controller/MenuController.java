package com.restaurantdelivery.controller;

import com.restaurantdelivery.dto.MenuDto;
import com.restaurantdelivery.entity.Menu;
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

    @GetMapping("/{id}")
    @ResponseBody
    public MenuDto getMenu(@PathVariable("id") Long id) {
        return convertToDto(menuService.getMenuById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MenuDto addMenu(@RequestBody MenuDto menuDto) {
        return convertToDto(menuService.addMenu(convertToEntity(menuDto)));
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

    private Menu convertToEntity(MenuDto menuDto) {
        return menuMapper.map(menuDto, Menu.class);
    }
}
