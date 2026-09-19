package com.example.scalable_search_engine.service;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.repository.DocumentRepository;
import com.example.scalable_search_engine.search.InvertedIndex;
import com.example.scalable_search_engine.search.TextProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TextProcessor textProcessor;
    private final InvertedIndex invertedIndex;

    public DocumentService(
            DocumentRepository documentRepository,
            TextProcessor textProcessor,
            InvertedIndex invertedIndex) {

        this.documentRepository = documentRepository;
        this.textProcessor = textProcessor;
        this.invertedIndex = invertedIndex;
    }

    public Document createDocument(Document document) {

        // 1. Save document to PostgreSQL
        Document savedDocument = documentRepository.save(document);

        // 2. Convert document text into tokens
        List<String> tokens = textProcessor.tokenize(
                document.getTitle() + " " + document.getContent()
        );

        // 3. Add document to inverted index
        invertedIndex.addDocument(
                savedDocument.getId(),
                tokens
        );

        return savedDocument;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Document not found with id: " + id
                        ));
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}