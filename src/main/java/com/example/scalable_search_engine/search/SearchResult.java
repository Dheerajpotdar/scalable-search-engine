package com.example.scalable_search_engine.search;

import com.example.scalable_search_engine.entity.Document;

public class SearchResult {

    private final Document document;
    private final double score;

    public SearchResult(Document document, double score) {
        this.document = document;
        this.score = score;
    }

    public Document getDocument() {
        return document;
    }

    public double getScore() {
        return score;
    }
}