package com.example.fresco.ingredient.domain;

import com.example.fresco.ingredient.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryCache {

    private final CategoryRepository categoryRepository;
    private final Map<String, Long> categoryNameToIdMap = new ConcurrentHashMap<>();
    private final Map<Long, String> categoryIdToNameMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initCache() {
        log.info("카테고리 캐시 초기화 시작");
        refreshCache();
        log.info("카테고리 캐시 초기화 완료: {} 개", categoryNameToIdMap.size());
    }

    public void refreshCache() {
        List<Category> categories = categoryRepository.findAll();

        // 기존 캐시 클리어
        categoryNameToIdMap.clear();
        categoryIdToNameMap.clear();

        // 새 데이터로 캐시 구성
        categories.forEach(category -> {
            categoryNameToIdMap.put(category.getName(), category.getId());
            categoryIdToNameMap.put(category.getId(), category.getName());
        });
    }

    public Long getCategoryId(String categoryName) {
        return categoryNameToIdMap.get(categoryName);
    }

    public String getCategoryName(Long categoryId) {
        return categoryIdToNameMap.get(categoryId);
    }

    public Map<String, Long> getCategoryIds(List<String> categoryNames) {
        return categoryNames.stream()
                .filter(categoryNameToIdMap::containsKey)
                .collect(Collectors.toMap(
                        name -> name,
                        categoryNameToIdMap::get
                ));
    }

    public boolean containsCategory(String categoryName) {
        return categoryNameToIdMap.containsKey(categoryName);
    }

    public Set<String> getAllCategoryNames() {
        return new HashSet<>(categoryNameToIdMap.keySet());
    }
}
