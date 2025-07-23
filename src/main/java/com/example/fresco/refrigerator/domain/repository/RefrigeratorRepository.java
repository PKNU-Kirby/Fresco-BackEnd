package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.refrigerator.domain.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
}