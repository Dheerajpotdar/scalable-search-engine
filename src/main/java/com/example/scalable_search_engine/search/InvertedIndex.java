package com.example.scalable_search_engine.search;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InvertedIndex {

    private final Map<String, Set<Long>> index = new HashMap<>();

    public void addDocument(Long documentId, List<String> tokens) {

        for (String token : tokens) {

            index
                    .computeIfAbsent(token, key -> new HashSet<>())
                    .add(documentId);
        }
    }

    public Set<Long> search(String word) {

        return index.getOrDefault(
                word.toLowerCase(),
                Collections.emptySet()
        );
    }

    public Map<String, Set<Long>> getIndex() {
        return index;
    }
    public int getInstanceId() {
        return System.identityHashCode(this);
    }
}