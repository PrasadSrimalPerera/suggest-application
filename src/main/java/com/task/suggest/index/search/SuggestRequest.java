package com.task.suggest.index.search;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by prasad on 7/1/18.
 * SuggestRequest used in creating suggest requests to the suggest index
 */
public class SuggestRequest {
    private SuggestQuery suggestQuery;
    private List<String> fetchFields = Lists.newArrayList();
    private int from;
    private int to;

    public SuggestQuery getSuggestQuery() {
        return suggestQuery;
    }

    public void setSuggestQuery(SuggestQuery suggestQuery) {
        this.suggestQuery = suggestQuery;
    }

    public List<String> getFetchFields() {
        return fetchFields;
    }

    public void setFetchFields(List<String> fetchFields) {
        this.fetchFields = fetchFields;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
