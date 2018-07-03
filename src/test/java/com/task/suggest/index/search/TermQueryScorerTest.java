package com.task.suggest.index.search;

import com.task.suggest.index.IndexField;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prasad on 7/2/18.
 * TermQueryScorer unit tests
 */
public class TermQueryScorerTest {
    @Test
    public void calculateScoreSimilar() throws Exception {
        TermQueryScorer termQueryScorer = new TermQueryScorer();
        String term = "Montreal";
        IndexField.StringIndexField stringIndexField = new IndexField.StringIndexField("term", "Montreal", false);
        Assert.assertEquals(1.0, termQueryScorer.calculateScore(term, stringIndexField), 0.0001);
    }

    @Test
    public void calculateScore() throws Exception {
        TermQueryScorer termQueryScorer = new TermQueryScorer();
        String term = "Mont";
        IndexField.StringIndexField stringIndexField = new IndexField.StringIndexField("term", "Montreal", false);
        Assert.assertEquals(0.2, termQueryScorer.calculateScore(term, stringIndexField), 0.0001);
    }
}