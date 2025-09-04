package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.refrigerator.domain.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
    @Query("""
        select r.id
        from Refrigerator r
        where r.creator = :userId
          and r.id in :ids
    """)
    List<Long> findIdsByCreatorAndIds(Long userId, List<Long> ids);
}