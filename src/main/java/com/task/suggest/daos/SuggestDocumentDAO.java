package com.task.suggest.daos;

import com.task.suggest.models.SuggestDocument;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by prasad on 7/1/18.
 * SuggestDocumentDAO provides the implementation to read the provided tsv file with data
 */
@Component
public class SuggestDocumentDAO {
    @Value("${data.path}")
    private String path;
    private static final String ENCODING = "UTF-8";

    /**
     * Retrieve suggest documents read the tsv file in the given path and create SuggestDocuments from found entries
     * @return  List of Created SuggestDocuments
     */
    public List<SuggestDocument> retrieveSuggestDocuments() {
        TsvParserSettings settings = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings);
        List<String[]> allRows = parser.parseAll(new File(path), ENCODING);
        // Ignore first entries (column names)
        return allRows.subList(1, allRows.size()).stream().map(SuggestDocument::create).collect(Collectors.toList());
    }
}
