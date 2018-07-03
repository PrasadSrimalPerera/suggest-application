package com.task.suggest.index.search;

import com.google.common.collect.Lists;
import com.task.suggest.index.IndexDocument;
import com.task.suggest.index.IndexField;
import com.task.suggest.index.SuggestIndex;
import com.task.suggest.index.SuggestToken;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by prasad on 7/1/18.
 * SuggestTermQuery is a concrete implementation of SuggestQuery where SuggestQueries can be used in retrieving suggestions
 * for a given query + rank the suggestions based on 'substring similarity'
 */
public class SuggestTermQuery extends SuggestQuery {
    private String term;
    private QueryScorer<String, IndexField.StringIndexField> queryScorer;

    public SuggestTermQuery(String term, QueryScorer<String, IndexField.StringIndexField> queryScorer) {
        this.term = term;
        this.queryScorer = queryScorer;
    }

    public SuggestTermQuery(String term) {
        this.term = term;
        this.queryScorer = new TermQueryScorer();
    }

    @Override
    public List<IndexDocument> getDocs(SuggestIndex suggestIndex) {
        try {
            // For term queries, we find the matching docs based on suggest tokens and return them
            List<SuggestToken> suggestTokens = suggestIndex.getSuggestSearchTokens(term, getField());
            List<IndexDocument> indexDocumentList = Lists.newArrayList();
            suggestTokens.stream().map(suggestIndex::get).filter(Objects::nonNull).forEach(indexDocumentList::addAll);
            return indexDocumentList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double getScore(IndexDocument indexDocument) {
        return queryScorer.calculateScore(term,
                (IndexField.StringIndexField) indexDocument.getDocumentFields().get(this.getField()));
    }
}
