package com.task.suggest.services;

import java.util.List;

/**
 * Created by prasad on 7/2/18.
 * SuggestAPIResponse contains the API response sent for suggest/ requests
 */
public class SuggestAPIResponse {
    private List<SuggestAPIResponseEntries> suggestions;

    public List<SuggestAPIResponseEntries> getSuggestions() {
        return suggestions;
    }

    void setSuggestions(List<SuggestAPIResponseEntries> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * Entries that matches the required API response
     */
    static class SuggestAPIResponseEntries {
        private String name;
        private double latitude;
        private double longitude;
        private double score;

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public double getLatitude() {
            return latitude;
        }

        void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getScore() {
            return score;
        }

        void setScore(double score) {
            this.score = score;
        }
    }
}
