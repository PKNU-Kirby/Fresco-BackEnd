package com.example.fresco.history.domain.repository;

import com.example.fresco.history.domain.History;
import com.example.fresco.refrigerator.domain.Refrigerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findAllByRefrigeratorId(Long refrigeratorId, Pageable pageable);
}
