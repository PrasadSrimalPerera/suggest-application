package com.task.suggest.services;

import com.google.common.collect.Lists;
import com.task.suggest.index.GeoLocation;
import com.task.suggest.index.IndexDocument;
import com.task.suggest.index.IndexField;
import com.task.suggest.index.SuggestIndex;
import com.task.suggest.index.search.SuggestBooleanQuery;
import com.task.suggest.index.search.SuggestGeoLocationQuery;
import com.task.suggest.index.search.SuggestRequest;
import com.task.suggest.index.search.SuggestResponse;
import com.task.suggest.index.search.SuggestTermQuery;
import com.task.suggest.models.SuggestDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by prasad on 6/30/18.
 * SuggestService provides the search logic endpoint all relates to indexing and searching
 */
@Service
public class SuggestService {
    private SuggestIndex suggestIndex;

    /**
     * Index Indexdocument
     *
     * @param indexDocument input index document
     * @return true if succeed
     * @throws IOException throw IOException
     */
    public boolean indexDoc(IndexDocument indexDocument) throws IOException {
        return suggestIndex.add(indexDocument);
    }

    /**
     * suggest service call
     *
     * @param suggestRequest suggest request
     * @return suggest response
     */
    public SuggestAPIResponse suggest(SuggestRequest suggestRequest) {
        SuggestResponse suggestResponse = suggestIndex.suggest(suggestRequest);
        SuggestAPIResponse suggestAPIResponse = new SuggestAPIResponse();
        List<SuggestAPIResponse.SuggestAPIResponseEntries> suggestAPIResponseEntries =
                suggestResponse.getSearchHitsList().stream().map(suggestHits -> {
            SuggestAPIResponse.SuggestAPIResponseEntries apiResponseEntries = new SuggestAPIResponse.SuggestAPIResponseEntries();
            apiResponseEntries.setName(
                    suggestHits.getResponseFields().get(SuggestDocument.NAME).getValue().toString() + ", "
                            + suggestHits.getResponseFields().get(SuggestDocument.COUNTRY).getValue().toString());
            GeoLocation geoLocation = (GeoLocation) suggestHits.getResponseFields().get(SuggestDocument.GEO_LOC).getValue();
            apiResponseEntries.setLatitude(geoLocation.getLatitude());
            apiResponseEntries.setLongitude(geoLocation.getLongitude());
            apiResponseEntries.setScore(suggestHits.getScore());
            return apiResponseEntries;
        }).collect(Collectors.toList());
        suggestAPIResponse.setSuggestions(suggestAPIResponseEntries);
        return suggestAPIResponse;
    }

    public boolean createIndex() {
        this.suggestIndex = new SuggestIndex();
        return true;
    }

    public boolean createIndex(String path) {
        this.suggestIndex = SuggestIndex.retrieveIndex(path);
        return true;
    }

    public boolean persistIndex(String path) {
        SuggestIndex.persistIndex(this.suggestIndex, path);
        return true;
    }

    /**
     * Create and return a SuggestRequest from provided parameters
     *
     * @param query       input sugegst query
     * @param geoLocation input GeoLocation
     * @return created SuggestRequest
     */
    public SuggestRequest createSuggestRequest(String query, GeoLocation geoLocation) {
        // First create Term query
        SuggestTermQuery suggestTermQuery = new SuggestTermQuery(query);
        suggestTermQuery.setField(SuggestDocument.NAME);
        SuggestBooleanQuery suggestBooleanQuery = new SuggestBooleanQuery();
        suggestBooleanQuery.addMustQuery(suggestTermQuery);
        // If geolocation is not null, create one
        if (Objects.nonNull(geoLocation)) {
            SuggestGeoLocationQuery suggestGeoLocationQuery = new SuggestGeoLocationQuery(geoLocation);
            suggestGeoLocationQuery.setField(SuggestDocument.GEO_LOC);
            suggestBooleanQuery.addShouldQuery(suggestGeoLocationQuery);
        }
        SuggestRequest suggestRequest = new SuggestRequest();
        suggestRequest.setFetchFields(Lists.newArrayList(
                SuggestDocument.NAME,
                SuggestDocument.GEO_LOC,
                SuggestDocument.COUNTRY));
        suggestRequest.setSuggestQuery(suggestBooleanQuery);
        return suggestRequest;
    }

    /**
     * Create Indexable document from a SuggestDocument
     *
     * @param suggestDocument input suggest document
     * @return created IndexDocument
     */
    public IndexDocument suggestDocToIndexDoc(SuggestDocument suggestDocument) {
        IndexDocument indexDocument = new IndexDocument(suggestDocument.getId());
        indexDocument.getDocumentFields().put(SuggestDocument.NAME,
                new IndexField.StringIndexField(SuggestDocument.NAME, suggestDocument.getName(), true));
        if (Objects.nonNull(suggestDocument.getNameAscii()))
            indexDocument.getDocumentFields().put(SuggestDocument.NAME_ASCII,
                    new IndexField.StringIndexField(SuggestDocument.NAME_ASCII, suggestDocument.getNameAscii(), false));
        if (Objects.nonNull(suggestDocument.getNameAlternative()))
            indexDocument.getDocumentFields().put(SuggestDocument.NAME_ALT,
                    new IndexField.StringIndexField(SuggestDocument.NAME_ALT, suggestDocument.getNameAlternative(), false));
        if (Objects.nonNull(suggestDocument.getCc2()))
            indexDocument.getDocumentFields().put(SuggestDocument.CC2,
                    new IndexField.StringIndexField(SuggestDocument.CC2, suggestDocument.getCc2(), false));
        if (Objects.nonNull(suggestDocument.getCountry()))
            indexDocument.getDocumentFields().put(SuggestDocument.COUNTRY,
                    new IndexField.StringIndexField(SuggestDocument.COUNTRY, suggestDocument.getCountry(), false));
        if (Objects.nonNull(suggestDocument.getFeatureCode()))
            indexDocument.getDocumentFields().put(SuggestDocument.FEATURE_CODE,
                    new IndexField.StringIndexField(SuggestDocument.FEATURE_CODE, suggestDocument.getFeatureCode(), false));
        if (Objects.nonNull(suggestDocument.getId()))
            indexDocument.getDocumentFields().put(SuggestDocument.ID,
                    new IndexField.StringIndexField(SuggestDocument.ID, suggestDocument.getId(), false));
        if (Objects.nonNull(suggestDocument.getCountryZipCode()))
            indexDocument.getDocumentFields().put(SuggestDocument.COUNTRY_ZIP,
                    new IndexField.StringIndexField(SuggestDocument.COUNTRY_ZIP, suggestDocument.getCountryZipCode(), false));
        if (Objects.nonNull(suggestDocument.getSuggestGeoLocation()))
            indexDocument.getDocumentFields().put(SuggestDocument.GEO_LOC,
                    new IndexField.GeoIndexField(SuggestDocument.GEO_LOC, suggestDocument.getSuggestGeoLocation(), false));
        indexDocument.getDocumentFields().put(SuggestDocument.POPULATION,
                new IndexField.LongIndexField(SuggestDocument.POPULATION, suggestDocument.getPopulation(), false));
        return indexDocument;
    }
}
