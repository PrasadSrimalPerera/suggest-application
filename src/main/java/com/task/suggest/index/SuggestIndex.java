package com.task.suggest.index;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.task.suggest.index.search.SuggestRequest;
import com.task.suggest.index.search.SuggestResponse;
import org.apache.logging.log4j.util.Strings;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by prasad on 6/29/18.
 * SuggestIndex class provides the operations relates to the inverted suggest index created by IndexDocument type
 * Here, we manage the flat inverted index map along with SuggestToken keys, initialize and use analyzers,
 * provide suggest operation for a given suggest queries, persist & retrieve operation for index from disk etc.
 */
public class SuggestIndex implements Serializable {
    private static final long serialVersionUID = 6540716430610410417L;
    private Map<SuggestToken, Set<IndexDocument>> suggestTokenListMap;
    private transient Analyzer analyzer;
    private transient Analyzer searchAnalyzer;
    private static transient Analyzer defaultAnalyzer = createAnalyzer();
    private static transient Analyzer defaultSearchAnalyzer = createSearchAnalyzer();
    private static final int EDGE_GRAM_MIN = 3;
    private static final int EDGE_GRAM_MAX = 20;

    /**
     * Constructor for SuggestIndex
     *
     * @param analyzer       index analyzer for SuggestIndex
     * @param searchAnalyzer search analyzer for SuggestIndex
     */
    public SuggestIndex(Analyzer analyzer, Analyzer searchAnalyzer) {
        this.analyzer = analyzer;
        this.searchAnalyzer = searchAnalyzer;
        suggestTokenListMap = Maps.newHashMap();
    }

    /**
     * Constructor for SuggestIndex
     *
     * @param suggestIndex existing suggest index (deserialized)
     */
    private SuggestIndex(SuggestIndex suggestIndex) {
        this.analyzer = defaultAnalyzer;
        this.searchAnalyzer = defaultSearchAnalyzer;
        this.suggestTokenListMap = suggestIndex.suggestTokenListMap;
    }

    /**
     * Default constructor with default analyzers
     */
    public SuggestIndex() {
        this(defaultAnalyzer, defaultSearchAnalyzer);
    }

    /**
     * Create default index analyzer with lowercase filtering + edge n-grams with {3, 20} token length
     *
     * @return created analyzer
     */
    private static Analyzer createAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new EdgeNGramTokenizer(EDGE_GRAM_MIN, EDGE_GRAM_MAX);
                TokenFilter tokenFilter = new LowerCaseFilter(tokenizer);
                return new TokenStreamComponents(tokenizer, tokenFilter);
            }
        };
    }

    /**
     * Create default searh analyzer with lowercase filtering
     *
     * @return created analyzer
     */
    private static Analyzer createSearchAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new KeywordTokenizer();
                TokenFilter tokenFilter = new LowerCaseFilter(tokenizer);
                return new TokenStreamComponents(tokenizer, tokenFilter);
            }
        };
    }

    /**
     * Add Indexdocument to the SugegstIndex
     *
     * @param indexDocument input document
     * @return true if succeed
     * @throws IOException throw IOException
     */
    public boolean add(IndexDocument indexDocument) throws IOException {
        final SuggestIndex suggestIndex = this;
        indexDocument.getDocumentFields().forEach((s, indexField) -> {
            if (indexField.isToIndex()) {
                List<SuggestToken> suggestTokenList = null;
                try {
                    suggestTokenList = indexField.createSuggestTokens(suggestIndex);
                    suggestTokenList.forEach(suggestToken -> {
                        if (suggestTokenListMap.containsKey(suggestToken)) {
                            suggestTokenListMap.get(suggestToken).add(indexDocument);
                        } else {
                            Set<IndexDocument> suggestDocumentList = Sets.newHashSet();
                            suggestDocumentList.add(indexDocument);
                            suggestTokenListMap.put(suggestToken, suggestDocumentList);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    /**
     * Retrieve the matching IndexDocuments for the given SuggestToken
     *
     * @param suggestToken input SuggestToken
     * @return List of IndexDocuments found
     */
    public List<IndexDocument> get(SuggestToken suggestToken) {
        return Lists.newArrayList(this.suggestTokenListMap.get(suggestToken));
    }

    /**
     * Create and retrieve SuggestTokens for given content (indexing time)
     *
     * @param content content (text)
     * @param field   index field id
     * @return List of tokens created
     * @throws IOException throw IOException
     */
    List<SuggestToken> getSuggestIndexTokens(String content, String field) throws IOException {
        List<SuggestToken> suggestTokenList = Lists.newArrayList();
        TokenStream tokenStream = analyzer.tokenStream(Strings.EMPTY, new StringReader(content));
        CharTermAttribute termAtt = tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            String token = termAtt.toString();
            SuggestToken suggestToken = new SuggestToken(token, field);
            suggestToken.setToken(token);
            suggestToken.setTokenLength(token.length());
            suggestTokenList.add(suggestToken);
        }
        tokenStream.close();
        return suggestTokenList;
    }

    /**
     * Create and retrieve SuggestTokens for given content (search time)
     *
     * @param content content (text)
     * @param field   query field id
     * @return List of tokens created
     * @throws IOException throw IOException
     */
    public List<SuggestToken> getSuggestSearchTokens(String content, String field) throws IOException {
        List<SuggestToken> suggestTokenList = Lists.newArrayList();
        TokenStream tokenStream = searchAnalyzer.tokenStream(Strings.EMPTY, new StringReader(content));
        CharTermAttribute termAtt = tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            String token = termAtt.toString();
            SuggestToken suggestToken = new SuggestToken(token, field);
            suggestToken.setToken(token);
            suggestToken.setTokenLength(token.length());
            suggestTokenList.add(suggestToken);
        }
        tokenStream.close();
        return suggestTokenList;
    }

    /**
     * Persist serialized suggest index in the given path
     *
     * @param suggestIndex suggest index to serialize
     * @param path         index path
     */
    public static void persistIndex(SuggestIndex suggestIndex, String path) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(suggestIndex);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize and retrieve suggest index from a given path
     *
     * @param path index path
     * @return deserialized index
     */
    public static SuggestIndex retrieveIndex(String path) {
        FileInputStream fileInputStream = null;
        SuggestIndex suggestIndex = null;
        try {
            fileInputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            suggestIndex = new SuggestIndex((SuggestIndex) objectInputStream.readObject());
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return suggestIndex;
    }

    /**
     * Suggest operation for a SuggestRequest
     *
     * @param suggestRequest input SuggestRequest
     * @return SuggestResponse containing results
     */
    public SuggestResponse suggest(SuggestRequest suggestRequest) {
        // Retrieve all the matching docs
        List<IndexDocument> indexDocumentList = suggestRequest.getSuggestQuery().getDocs(this);
        // calculate scores for matching docs
        Map<IndexDocument, Double> scores = indexDocumentList.stream().collect(
                Collectors.toMap(indexDocument -> indexDocument, suggestRequest.getSuggestQuery()::getScore));
        // sort them based on score
        Set<Map.Entry<IndexDocument, Double>> sortedResults = Sets.newTreeSet(
                (d1, d2) -> d1.getValue() < d2.getValue() ? 1 : -1);
        sortedResults.addAll(scores.entrySet());
        // create suggest response
        SuggestResponse suggestResponse = new SuggestResponse();
        suggestResponse.setTotalHits(sortedResults.size());
        // add all the results as suggest hits
        List<SuggestResponse.SuggestHits> suggestHitsList = sortedResults.stream()
                .map(scoreIndexDocumentEntry -> {
                    SuggestResponse.SuggestHits suggestHits = new SuggestResponse.SuggestHits();
                    suggestHits.setId(scoreIndexDocumentEntry.getKey().getDocumentId());
                    // From index fields,
                    suggestHits.getResponseFields().putAll(
                            suggestRequest.getFetchFields()
                                    .stream()
                                    .filter(scoreIndexDocumentEntry.getKey().getDocumentFields()::containsKey)
                                    .collect(Collectors.toMap(responseField -> responseField,
                                            scoreIndexDocumentEntry.getKey().getDocumentFields()::get)));
                    suggestHits.setScore(scoreIndexDocumentEntry.getValue());
                    return suggestHits;
                }).collect(Collectors.toList());
        suggestResponse.setSearchHitsList(suggestHitsList);
        return suggestResponse;
    }
}
