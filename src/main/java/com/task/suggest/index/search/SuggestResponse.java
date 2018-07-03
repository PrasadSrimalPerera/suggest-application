package com.task.suggest.index.search;

import com.google.common.collect.Lists;
import com.task.suggest.index.IndexField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prasad on 7/1/18.
 * SuggestResponse represents the SuggestIndex suggest response containing hit results
 */
public class SuggestResponse {
    private long totalHits;
    private List<SuggestHits> searchHitsList = Lists.newArrayList();

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public List<SuggestHits> getSearchHitsList() {
        return searchHitsList;
    }

    public void setSearchHitsList(List<SuggestHits> searchHitsList) {
        this.searchHitsList = searchHitsList;
    }

    /**
     * SuggestHits contain the suggest result fields, including document id, score and requested fields.
     */
    public static class SuggestHits {
        private String id;
        private double score;
        private Map<String, IndexField> responseFields = new HashMap<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public Map<String, IndexField> getResponseFields() {
            return responseFields;
        }

        public void setResponseFields(Map<String, IndexField> responseFields) {
            this.responseFields = responseFields;
        }
    }
}
