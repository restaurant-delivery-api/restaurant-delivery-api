package com.restaurantdelivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.restaurantdelivery.entity.Section;
import com.restaurantdelivery.repository.SectionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;

    public Section getById(Long id) {
        Optional<Section> section = sectionRepository.findById(id);
        return section.orElse(null);
    }

    public Section changeName(Long sectionId, String name) {
        Optional<Section> optionalSection = sectionRepository.findById(sectionId);
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            section.setName(name);
            return sectionRepository.save(section);
        } else {
            return null;
        }
    }

    public void delete(Section section) {
        if (section.getCategories().size() == 0) {
            sectionRepository.deleteById(section.getId());
        }
    }

    public List<Section> getAll() {
        return sectionRepository.findAll();
    }

    public List<Section> getAllSortedByName() {
        List<Section> sections = sectionRepository.findAll();
        sections.sort(Comparator.comparing(Section::getName));
        return sections;
    }

    public Section getByName(String name) {
        Optional<Section> section = sectionRepository.findByName(name);
        return section.orElse(null);
    }

    public Section save(Section section) {
        return sectionRepository.save(section);
    }
}