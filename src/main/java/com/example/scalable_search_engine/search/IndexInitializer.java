package com.example.scalable_search_engine.search;

import com.example.scalable_search_engine.entity.Document;
import com.example.scalable_search_engine.repository.DocumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexInitializer implements CommandLineRunner {

    private final DocumentRepository documentRepository;
    private final TextProcessor textProcessor;
    private final InvertedIndex invertedIndex;

    public IndexInitializer(
            DocumentRepository documentRepository,
            TextProcessor textProcessor,
            InvertedIndex invertedIndex) {

        this.documentRepository = documentRepository;
        this.textProcessor = textProcessor;
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void run(String... args) {

        System.out.println("Building search index...");

        List<Document> documents =
                documentRepository.findAll();

        for (Document document : documents) {

            String text =
                    document.getTitle()
                            + " "
                            + document.getContent();

            List<String> tokens =
                    textProcessor.tokenize(text);

            invertedIndex.addDocument(
                    document.getId(),
                    tokens
            );
        }

        System.out.println(
                "Search index built successfully. Documents indexed: "
                        + documents.size()
        );
    }
}