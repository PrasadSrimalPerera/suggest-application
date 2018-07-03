package com.task.suggest.index;

import com.task.suggest.index.IndexField.StringIndexField;
import com.task.suggest.index.search.SuggestRequest;
import com.task.suggest.index.search.SuggestTermQuery;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

/**
 * SuggestIndex unit tests
 */
public class SuggestIndexTest {

  @Test
  public void add() throws IOException {
    SuggestIndex suggestIndex = new SuggestIndex();
    IndexDocument indexDocument = new IndexDocument("id1");
    // Add a field with string length = 1
    indexDocument.getDocumentFields().put("f1", new StringIndexField("f1", "abc", true));
    Assert.assertEquals(Boolean.TRUE, suggestIndex.add(indexDocument));
    // Verify index retrieve one document
    Assert.assertEquals(1, suggestIndex.get(new SuggestToken("abc", "f1")).size());
  }

  @Test
  public void get() throws IOException {
    SuggestIndex suggestIndex = new SuggestIndex();
    IndexDocument indexDocument = new IndexDocument("id1");
    // Add a field with string length = 1
    indexDocument.getDocumentFields().put("f1", new StringIndexField("f1", "abcd", true));
    Assert.assertEquals(Boolean.TRUE, suggestIndex.add(indexDocument));
    // Verify index retrieve one document
    Assert.assertEquals(1, suggestIndex.get(new SuggestToken("abc", "f1")).size());
    Assert.assertEquals(1, suggestIndex.get(new SuggestToken("abcd", "f1")).size());
  }

  @Test
  public void getSuggestIndexTokens() throws IOException {
    // SuggestIndex with default analyzers
    SuggestIndex suggestIndex = new SuggestIndex();
    List<SuggestToken> suggestTokenList = suggestIndex.getSuggestIndexTokens("MontreaL", "f1");
    org.hamcrest.MatcherAssert.assertThat(suggestTokenList,
        containsInAnyOrder(
            new SuggestToken("mon", "f1"),
            new SuggestToken("mont", "f1"),
            new SuggestToken("montr", "f1"),
            new SuggestToken("montre", "f1"),
            new SuggestToken("montrea", "f1"),
            new SuggestToken("montreal", "f1")
        ));
  }

  @Test
  public void getSuggestSearchTokens() throws IOException {
    // SuggestIndex with default analyzers
    SuggestIndex suggestIndex = new SuggestIndex();
    List<SuggestToken> suggestTokenList = suggestIndex.getSuggestSearchTokens("MontrE", "f1");
    org.hamcrest.MatcherAssert.assertThat(suggestTokenList,
        containsInAnyOrder(
            new SuggestToken("montre", "f1")
        ));
  }

  @Test
  public void persistIndex() throws IOException {
    SuggestIndex suggestIndex = new SuggestIndex();
    IndexDocument indexDocument = new IndexDocument("id1");
    // Add a field with string length = 1
    indexDocument.getDocumentFields().put("f1", new StringIndexField("f1", "abcd", true));
    suggestIndex.add(indexDocument);
    SuggestIndex.persistIndex(suggestIndex, "/tmp/index.ser");
    Assert.assertTrue(Files.exists(Paths.get("/tmp/index.ser")));
    Files.delete(Paths.get("/tmp/index.ser"));
  }

  @Test
  public void retrieveIndex() throws IOException {
    SuggestIndex suggestIndex = new SuggestIndex();
    IndexDocument indexDocument = new IndexDocument("id1");
    // Add a field with string length = 1
    indexDocument.getDocumentFields().put("f1", new StringIndexField("f1", "abcd", true));
    suggestIndex.add(indexDocument);
    SuggestIndex.persistIndex(suggestIndex, "/tmp/index.ser");
    Assert.assertTrue(Files.exists(Paths.get("/tmp/index.ser")));
    // Now Retrieve the index
    SuggestIndex suggestIndexFromDisk = SuggestIndex.retrieveIndex("/tmp/index.ser");
    // Verify index retrieve one document
    Assert.assertEquals(1, suggestIndex.get(new SuggestToken("abc", "f1")).size());
    Files.delete(Paths.get("/tmp/index.ser"));
  }

  @Test
  public void suggest() throws IOException {
    SuggestIndex suggestIndex = new SuggestIndex();
    SuggestRequest suggestRequest = new SuggestRequest();
    SuggestTermQuery suggestTermQuery = new SuggestTermQuery("abc");
    suggestTermQuery.setField("f1");
    suggestRequest.setSuggestQuery(suggestTermQuery);
    Assert.assertEquals(0L, suggestIndex.suggest(suggestRequest).getTotalHits());

    // Now add 1 document and verify
    // Add a field with string length = 1
    IndexDocument indexDocument = new IndexDocument("id1");
    indexDocument.getDocumentFields().put("f1", new StringIndexField("f1", "abcd", true));
    suggestIndex.add(indexDocument);
    Assert.assertEquals(1, suggestIndex.get(new SuggestToken("abc", "f1")).size());
  }
}