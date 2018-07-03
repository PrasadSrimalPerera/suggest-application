package com.task.suggest.index.search;

import com.task.suggest.index.IndexDocument;
import com.task.suggest.index.SuggestIndex;

import java.util.List;

/**
 * Created by prasad on 7/1/18.
 * Abstract SuggestQuery provides the required abstraction with required basic fields to create concrete suggest queries
 */
public abstract class SuggestQuery {
    private String field;
    public abstract List<IndexDocument> getDocs(SuggestIndex suggestIndex);
    public abstract double getScore(IndexDocument indexDocument);

    String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
