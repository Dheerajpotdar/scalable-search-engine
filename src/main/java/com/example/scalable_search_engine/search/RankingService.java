package com.example.scalable_search_engine.search;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final DocumentRepository documentRepository;

    public RankingService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public int calculateTermFrequency(
            String text,
            String term) {

        if (text == null || text.isBlank()) {
            return 0;
        }

        String lowerText = text.toLowerCase();
        String lowerTerm = term.toLowerCase();

        String[] words = lowerText.split("\\s+");

        int count = 0;

        for (String word : words) {

            word = word.replaceAll(
                    "[^a-z0-9]",
                    ""
            );

            if (word.equals(lowerTerm)) {
                count++;
            }
        }

        return count;
    }

    public double calculateInverseDocumentFrequency(
            String term) {

        // Total number of documents
        long totalDocuments =
                documentRepository.count();

        if (totalDocuments == 0) {
            return 0.0;
        }

        // Number of documents containing the term
        List<Document> documents =
                documentRepository.findAll();

        long documentsContainingTerm = 0;

        for (Document document : documents) {

            String text =
                    document.getTitle()
                            + " "
                            + document.getContent();

            if (calculateTermFrequency(text, term) > 0) {
                documentsContainingTerm++;
            }
        }

        if (documentsContainingTerm == 0) {
            return 0.0;
        }

        return Math.log(
                (double) totalDocuments
                        / documentsContainingTerm
        );
    }
}