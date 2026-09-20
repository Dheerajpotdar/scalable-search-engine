package com.example.scalable_search_engine.controller;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.search.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.scalable_search_engine.search.SearchResult;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<SearchResult> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "AND") String mode,
            @RequestParam(defaultValue = "10") int limit) {

        return searchService.search(
                q,
                mode,
                limit
        );
    }
}