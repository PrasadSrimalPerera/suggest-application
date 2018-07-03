package com.task.suggest.index.search;

import com.task.suggest.index.GeoLocation;
import com.task.suggest.index.IndexDocument;
import com.task.suggest.index.IndexField;
import com.task.suggest.index.SuggestIndex;

import java.util.List;

/**
 * Created by prasad on 7/1/18.
 * SuggestGeoLocationQuery is a concrete implementation of SuggestQuery and used in (for now) ranking the suggest queries
 */
public class SuggestGeoLocationQuery extends SuggestQuery {
    private GeoLocation geoLocation;
    private QueryScorer<GeoLocation, IndexField.GeoIndexField> queryScorer;

    public SuggestGeoLocationQuery(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
        this.queryScorer = new GeoLocationQueryScorer();
    }

    public SuggestGeoLocationQuery(GeoLocation geoLocation,
                                   QueryScorer<GeoLocation, IndexField.GeoIndexField> queryScorer) {
        this.geoLocation = geoLocation;
        this.queryScorer = queryScorer;
    }

    @Override
    public List<IndexDocument> getDocs(SuggestIndex suggestIndex) {
        // For now, no index is built based on geolocation values
        return null;
    }

    @Override
    public double getScore(IndexDocument indexDocument) {
        return queryScorer.calculateScore(geoLocation,
                (IndexField.GeoIndexField) indexDocument.getDocumentFields().get(getField()));
    }
}
