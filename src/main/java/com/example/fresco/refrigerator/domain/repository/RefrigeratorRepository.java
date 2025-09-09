package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorEditableResponse;
import com.example.fresco.refrigerator.domain.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
    @Query("""
        select new com.example.fresco.refrigerator.controller.dto.response.RefrigeratorEditableResponse(
            r.id,
            case when r.creator = :userId then true else false end
        )
        from Refrigerator r
        where r.id in :ids
    """)
    List<RefrigeratorEditableResponse> findEditableRowsByIds(@Param("ids") List<Long> ids,
                                                             @Param("userId") Long userId);
}