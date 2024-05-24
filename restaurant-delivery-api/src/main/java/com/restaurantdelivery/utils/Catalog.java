package com.restaurantdelivery.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;
import com.restaurantdelivery.dto.CatalogDto;
import com.restaurantdelivery.entity.Category;
import com.restaurantdelivery.entity.Section;
import com.restaurantdelivery.service.SectionService;
import com.restaurantdelivery.service.UserService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Catalog {
    public static void setCatalog(ModelAndView modelAndView, SectionService sectionService) {
        CatalogDto catalogDto = new CatalogDto();
        Map<Section, List<Category>> catalog = new LinkedHashMap<>();
        for (Section section: sectionService.getAllSortedByName()) {
            catalog.put(section, section.getCategories());
        }
        catalogDto.setCatalog(catalog);
        modelAndView.getModel().put("catalogDto", catalogDto);
    }
}