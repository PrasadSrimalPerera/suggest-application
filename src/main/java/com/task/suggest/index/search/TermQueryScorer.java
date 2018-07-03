package com.task.suggest.index.search;

import com.task.suggest.index.IndexField;

/**
 * Created by prasad on 7/1/18.
 * TermQueryScorer is a concrete implementation of QueryScorer that calculates the similarity between term matches based
 * on the character length difference between the given term and indexed field term.
 */
class TermQueryScorer implements QueryScorer<String, IndexField.StringIndexField> {
    @Override
    public double calculateScore(String term1, IndexField.StringIndexField term2) {
        return 1 / (Math.abs(term1.length() - term2.getValue().length()) + 1);
    }
}
