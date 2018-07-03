package com.task.suggest.index.search;

import com.task.suggest.index.GeoLocation;
import com.task.suggest.index.IndexField;

/**
 * Created by prasad on 7/1/18.
 * GeoLocationQueryScorer is an implementation of QueryScorer interface.
 * GeoLocationQueryScorer calculates the ranking score with respect to a given geo location.
 * Here, the score is calculated as the inverse of Euclidean distance between to geo points
 */
class GeoLocationQueryScorer implements QueryScorer<GeoLocation, IndexField.GeoIndexField> {

    @Override
    public double calculateScore(GeoLocation geoLocation, IndexField.GeoIndexField geoIndexField) {
        return 1/ (Math.sqrt(Math.pow(geoLocation.getLongitude() - geoIndexField.getValue().getLongitude(), 2) +
                Math.pow(geoLocation.getLatitude() - geoIndexField.getValue().getLatitude(), 2)) + 1);
    }
}
