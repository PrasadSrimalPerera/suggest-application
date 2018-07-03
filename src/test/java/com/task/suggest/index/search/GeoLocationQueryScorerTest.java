package com.task.suggest.index.search;

import com.task.suggest.index.GeoLocation;
import com.task.suggest.index.IndexField;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prasad on 7/2/18.
 * GeoLocationQueryScorer unit tests
 */
public class GeoLocationQueryScorerTest {
    @Test
    public void calculateScoreSimilar() throws Exception {
        GeoLocation geoLocation = new GeoLocation(12.12, 22.22);
        IndexField.GeoIndexField geoIndexField = new IndexField.GeoIndexField("geo", new GeoLocation(12.12, 22.22), false);
        GeoLocationQueryScorer geoLocationQueryScorer = new GeoLocationQueryScorer();
        Assert.assertEquals(geoLocationQueryScorer.calculateScore(geoLocation, geoIndexField), 1.0, 0.0001);
    }

    @Test
    public void calculateScore() throws Exception {
        // 12^2 + 5^2 = 13^2
        GeoLocation geoLocation = new GeoLocation(20.5, 30.4);
        IndexField.GeoIndexField geoIndexField = new IndexField.GeoIndexField("geo", new GeoLocation(25.5, 42.4), false);
        // According to the scoring technique score = 1 / (13 + 1)
        GeoLocationQueryScorer geoLocationQueryScorer = new GeoLocationQueryScorer();
        Assert.assertEquals(1 / geoLocationQueryScorer.calculateScore(geoLocation, geoIndexField), 14.0, 0.0001);
    }
}