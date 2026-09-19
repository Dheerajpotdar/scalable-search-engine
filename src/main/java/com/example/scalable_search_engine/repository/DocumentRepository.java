package com.example.scalable_search_engine.repository;

import com.example.scalable_search_engine.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}