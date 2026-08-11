package com.example.planeo_back.infrastructure.adapter.repository.category;

import com.example.planeo_back.infrastructure.adapter.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
            SELECT c FROM Category c
            WHERE c.owner IS NULL
               OR c.owner = :username
            ORDER BY c.name
            """)
    List<Category> findVisibleForUser(@Param("username") String username);
}
