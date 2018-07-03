package com.task.suggest.listeners;

import com.task.suggest.daos.SuggestDocumentDAO;
import com.task.suggest.models.SuggestDocument;
import com.task.suggest.services.SuggestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.List;


/**
 * Created by prasad on 7/1/18.
 * SuggestIndexInitializerListener implements ApplicationListener with ContextRefreshedEvent.
 * The implementation of onApplication event provides SuggestIndex initialization by creating new or reading from disk
 */
@Component
class SuggestIndexInitializerListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestIndexInitializerListener.class);
    @Autowired
    private SuggestDocumentDAO suggestDocumentDAO;

    @Autowired
    private SuggestService suggestService;

    @Value("${index.path}")
    private String path;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        File file = new File(path);
        if (!file.exists()) {
            LOGGER.debug("No index found! Creating new at: {}",path);
            List<SuggestDocument> suggestDocumentList = suggestDocumentDAO.retrieveSuggestDocuments();
            LOGGER.debug("Retrieved {} SuggestDocument to index", suggestDocumentList.size());
            if (suggestService.createIndex()) {
                suggestDocumentList.stream().map(
                        suggestDocument -> suggestService.suggestDocToIndexDoc(suggestDocument))
                        .forEach(indexDocument -> {
                            try {
                                suggestService.indexDoc(indexDocument);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                LOGGER.debug("new Index built at: {}", path);
                suggestService.persistIndex(path);
            }
        } else {
            LOGGER.debug("Index found at= {} loading the existing index", path);
            suggestService.createIndex(path);
        }
    }
}
