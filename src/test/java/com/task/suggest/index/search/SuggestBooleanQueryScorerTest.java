package com.task.suggest.index.search;

import com.google.common.collect.Lists;
import com.task.suggest.index.GeoLocation;
import com.task.suggest.index.IndexDocument;
import com.task.suggest.index.IndexField;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by prasad on 7/2/18.
 * SuggestBooleanQueryScorer unit tests
 */
public class SuggestBooleanQueryScorerTest {
    @Test
    public void calculateScore() throws Exception {
        // Here, we create two queries, one term and one geolocation and verify the score
        IndexDocument indexDocument = new IndexDocument("id1");

        indexDocument.getDocumentFields().put("f1", new IndexField.StringIndexField("f1", "Montreal", true));
        indexDocument.getDocumentFields().put("f2", new IndexField.GeoIndexField("f2",new GeoLocation(122.2, -445.22), false));
        SuggestQuery suggestQuery1 = new SuggestTermQuery("montreal");
        suggestQuery1.setField("f1");

        SuggestQuery suggestQuery2 = new SuggestGeoLocationQuery(new GeoLocation(122.2, -445.22));
        suggestQuery2.setField("f2");
        // For two queries combined, the scorer should return 2
        List<SuggestQuery> suggestQueryList = Lists.newArrayList(suggestQuery1, suggestQuery2);
        SuggestBooleanQueryScorer suggestBooleanQueryScorer = new SuggestBooleanQueryScorer();
        Assert.assertEquals(2.0, suggestBooleanQueryScorer.calculateScore(suggestQueryList, indexDocument), 0.00001);
    }
}