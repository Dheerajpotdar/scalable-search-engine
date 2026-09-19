package com.example.scalable_search_engine.search;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {

    private final InvertedIndex invertedIndex;
    private final DocumentRepository documentRepository;
    private final TextProcessor textProcessor;

    public SearchService(
            InvertedIndex invertedIndex,
            DocumentRepository documentRepository,
            TextProcessor textProcessor) {

        this.invertedIndex = invertedIndex;
        this.documentRepository = documentRepository;
        this.textProcessor = textProcessor;
    }

    public List<Document> search(String query, String mode) {

        // 1. Convert search query into individual words
        List<String> words = textProcessor.tokenize(query);

        // 2. If query is empty, return empty result
        if (words.isEmpty()) {
            return List.of();
        }

        // 3. Get document IDs for the first word
        Set<Long> matchingDocumentIds =
                new HashSet<>(
                        invertedIndex.search(words.get(0))
                );

        // 4. Process remaining words
        for (int i = 1; i < words.size(); i++) {

            // Get document IDs containing current word
            Set<Long> documentIds =
                    invertedIndex.search(words.get(i));

            // OR search
            if ("OR".equalsIgnoreCase(mode)) {

                // Union of both sets
                matchingDocumentIds.addAll(documentIds);

            } else {

                // AND search
                // Keep only IDs present in both sets
                matchingDocumentIds.retainAll(documentIds);
            }
        }

        // 5. Fetch all matching documents in batch
        return documentRepository.findAllById(matchingDocumentIds);
    }
}