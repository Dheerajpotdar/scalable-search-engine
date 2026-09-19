package com.example.scalable_search_engine.controller;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.search.InvertedIndex;
import com.example.scalable_search_engine.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final InvertedIndex invertedIndex;

    public DocumentController(
            DocumentService documentService,
            InvertedIndex invertedIndex) {

        this.documentService = documentService;
        this.invertedIndex = invertedIndex;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Document createDocument(@RequestBody Document document) {
        return documentService.createDocument(document);
    }

    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }

    @GetMapping("/search-index")
    public Object getIndex() {

        System.out.println(
                "INDEX ENDPOINT INSTANCE = "
                        + invertedIndex.getInstanceId()
        );

        return invertedIndex.getIndex();
    }
}