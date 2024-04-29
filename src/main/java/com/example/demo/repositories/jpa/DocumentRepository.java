package com.example.demo.repositories.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Document;
import com.example.demo.models.Request;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
    Optional<Document> findByRequest(Request request);
    void deleteByRequest(Request request);
}
