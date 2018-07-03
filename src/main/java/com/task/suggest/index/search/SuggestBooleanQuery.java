package com.task.suggest.index.search;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.task.suggest.index.IndexDocument;
import com.task.suggest.index.SuggestIndex;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by prasad on 7/1/18.
 * SuggestBooleanQuery provides a concrete extension of SuggestQuery that can be used to combine other primitive Query types
 */
public class SuggestBooleanQuery extends SuggestQuery {
    private List<SuggestQuery> mustQueryList = Lists.newArrayList();
    private List<SuggestQuery> shouldQueryList = Lists.newArrayList();
    private QueryScorer<List<SuggestQuery>, IndexDocument> queryScorer;

    public SuggestBooleanQuery(QueryScorer<List<SuggestQuery>, IndexDocument> queryScorer) {
        this.queryScorer = queryScorer;
    }

    public SuggestBooleanQuery() {
        this.queryScorer = new SuggestBooleanQueryScorer();
    }

    @Override
    public List<IndexDocument> getDocs(SuggestIndex suggestIndex) {
        // For boolean query get docs, we retrieve all the docs for all the must query matches and try
        // retaining the documents that found in all document sets
        Set<IndexDocument> indexDocuments = Sets.newHashSet();
        List<Set<IndexDocument>> resultList = mustQueryList.stream()
                .map(suggestQuery -> Sets.newHashSet(suggestQuery.getDocs(suggestIndex)))
                .collect(Collectors.toList());
        indexDocuments.addAll(resultList.get(0));
        // If this has more than one query, retain the matched documents for all
        if (resultList.size() > 1)
            resultList.forEach(indexDocuments::retainAll);
        return Lists.newArrayList(indexDocuments);
    }

    @Override
    public double getScore(IndexDocument indexDocument) {
        // For calculating the score, we combine all the query scores and take the avg
        return (queryScorer.calculateScore(this.mustQueryList, indexDocument) +
                queryScorer.calculateScore(this.shouldQueryList, indexDocument)) /
                (this.mustQueryList.size() + this.shouldQueryList.size());
    }

    public void addMustQuery(SuggestQuery suggestQuery) {
        this.mustQueryList.add(suggestQuery);
    }

    public void addShouldQuery(SuggestQuery suggestQuery) {
        this.shouldQueryList.add(suggestQuery);
    }
}
