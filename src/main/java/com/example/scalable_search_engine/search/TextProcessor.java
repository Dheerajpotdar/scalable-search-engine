package com.example.scalable_search_engine.search;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TextProcessor {

    public List<String> tokenize(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        return Arrays.stream(
                        text.toLowerCase()
                                .replaceAll("[^a-z0-9\\s]", " ")
                                .split("\\s+")
                )
                .filter(word -> !word.isBlank())
                .collect(Collectors.toList());
    }
}