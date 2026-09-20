package com.example.scalable_search_engine.search;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

@Service
public class SearchService {

    private final InvertedIndex invertedIndex;
    private final DocumentRepository documentRepository;
    private final TextProcessor textProcessor;
    private final RankingService rankingService;

    public SearchService(
            InvertedIndex invertedIndex,
            DocumentRepository documentRepository,
            TextProcessor textProcessor,
            RankingService rankingService) {

        this.invertedIndex = invertedIndex;
        this.documentRepository = documentRepository;
        this.textProcessor = textProcessor;
        this.rankingService = rankingService;
    }

    public List<SearchResult> search(
            String query,
            String mode,
            int limit) {

        // 1. Convert query into words
        List<String> words =
                textProcessor.tokenize(query);

        // 2. Empty query
        if (words.isEmpty()) {
            return List.of();
        }

        // 3. Get documents for first word
        Set<Long> matchingDocumentIds =
                new HashSet<>(
                        invertedIndex.search(words.get(0))
                );

        // 4. Process remaining words
        for (int i = 1; i < words.size(); i++) {

            Set<Long> documentIds =
                    invertedIndex.search(words.get(i));

            // OR search
            if ("OR".equalsIgnoreCase(mode)) {

                matchingDocumentIds.addAll(documentIds);

            } else {

                // AND search
                matchingDocumentIds.retainAll(documentIds);
            }
        }

        // 5. Fetch matching documents
        List<Document> documents =
                documentRepository.findAllById(
                        matchingDocumentIds
                );

        // 6. Total number of documents
        long totalDocuments =
                documentRepository.count();

        // 7. Calculate score for every document
        List<SearchResult> results =
                new ArrayList<>();

        for (Document document : documents) {

            String text =
                    document.getTitle()
                            + " "
                            + document.getContent();

            double totalScore = 0.0;

            // Calculate score for every query term
            for (String word : words) {

                double score =
                        rankingService.calculateTfIdf(
                                text,
                                word,
                                totalDocuments
                        );

                totalScore += score;
            }

            results.add(
                    new SearchResult(
                            document,
                            totalScore
                    )
            );
        }

        // 8. Get Top-K results
        return getTopK(results, limit);
    }

    private List<SearchResult> getTopK(
            List<SearchResult> results,
            int k) {

        // Min-heap based on score
        PriorityQueue<SearchResult> minHeap =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                SearchResult::getScore
                        )
                );

        // Process every result
        for (SearchResult result : results) {

            minHeap.offer(result);

            // Keep only K best results
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Convert heap to list
        List<SearchResult> topResults =
                new ArrayList<>(minHeap);

        // Sort highest score first
        topResults.sort(
                Comparator.comparingDouble(
                        SearchResult::getScore
                ).reversed()
        );

        return topResults;
    }
}