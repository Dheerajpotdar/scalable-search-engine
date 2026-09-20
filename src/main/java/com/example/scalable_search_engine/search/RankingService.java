package com.example.scalable_search_engine.search;

import org.springframework.stereotype.Service;

@Service
public class RankingService {

    private final InvertedIndex invertedIndex;

    public RankingService(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    /**
     * Calculate Term Frequency (TF)
     *
     * TF = number of times the term appears in the document
     */
    public int calculateTermFrequency(String text, String term) {

        if (text == null || text.isBlank()) {
            return 0;
        }

        String lowerText = text.toLowerCase();
        String lowerTerm = term.toLowerCase();

        String[] words = lowerText.split("\\s+");

        int count = 0;

        for (String word : words) {

            word = word.replaceAll("[^a-z0-9]", "");

            if (word.equals(lowerTerm)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Calculate Inverse Document Frequency (IDF)
     *
     * IDF = log(totalDocuments / documentsContainingTerm)
     */
    public double calculateInverseDocumentFrequency(
            String term,
            long totalDocuments) {

        if (totalDocuments == 0) {
            return 0.0;
        }

        // Get posting list from inverted index
        int documentsContainingTerm =
                invertedIndex.search(term).size();

        if (documentsContainingTerm == 0) {
            return 0.0;
        }

        return Math.log(
                (double) totalDocuments
                        / documentsContainingTerm
        );
    }

    /**
     * Calculate TF-IDF score
     */
    public double calculateTfIdf(
            String text,
            String term,
            long totalDocuments) {

        int tf = calculateTermFrequency(text, term);

        double idf =
                calculateInverseDocumentFrequency(
                        term,
                        totalDocuments
                );

        return tf * idf;
    }
}