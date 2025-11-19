package com.example.fresco.history.domain.repository;

import com.example.fresco.history.domain.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findAllByRefrigeratorIngredient_Refrigerator_Id(Long refrigeratorId, Pageable pageable);
}
