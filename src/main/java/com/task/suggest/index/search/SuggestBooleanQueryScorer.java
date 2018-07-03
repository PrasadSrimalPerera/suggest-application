package com.task.suggest.index.search;

import com.task.suggest.index.IndexDocument;
import java.util.List;

/**
 * Created by prasad on 7/1/18.
 * SuggestBooleanQueryScorer is an implementation of QueryScorer for the Boolean type suggest queries
 */
class SuggestBooleanQueryScorer implements QueryScorer<List<SuggestQuery>, IndexDocument> {
    @Override
    public double calculateScore(List<SuggestQuery> suggestQueries, IndexDocument indexDocument) {
        return suggestQueries.stream().mapToDouble(suggestQuery -> suggestQuery.getScore(indexDocument))
                .sum();
    }
}
