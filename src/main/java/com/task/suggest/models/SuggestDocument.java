package com.task.suggest.models;

import com.google.common.collect.Lists;
import com.task.suggest.index.GeoLocation;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by prasad on 6/29/18.
 * SuggestDocument is the dto mapping for the given suggest sources.
 * All the data fields are populated for the SuggestDocument with respect to extensibility of the application/project
 */
public class SuggestDocument {
    private String id;
    private String name;
    private String nameAscii;
    private String nameAlternative;
    private GeoLocation geoLocation;
    private char featureClass;
    private String featureCode;
    private String country;
    private String cc2;
    private List<String> adminValues;
    private long population;
    private long elevation;
    private long dem;
    private String countryZipCode;
    private DateTime modifiedAt;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String NAME_ASCII = "name_ascii";
    public static final String NAME_ALT = "name_alt";
    public static final String GEO_LOC = "geo_loc";
    public static final String FEAT_CLASS = "feature_class";
    public static final String FEATURE_CODE = "feature_code";
    public static final String COUNTRY = "country";
    public static final String CC2 = "cc2";
    public static final String ADMIN_VALS = "admin_vals";
    public static final String POPULATION = "population";
    public static final String ELEVATION = "elevation";
    public static final String DEM = "dem";
    public static final String COUNTRY_ZIP = "country_zip";
    public static final String DATE_MODIFIED = "date_modified";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAscii() {
        return nameAscii;
    }

    public void setNameAscii(String nameAscii) {
        this.nameAscii = nameAscii;
    }

    public String getNameAlternative() {
        return nameAlternative;
    }

    public void setNameAlternative(String nameAlternative) {
        this.nameAlternative = nameAlternative;
    }

    public GeoLocation getSuggestGeoLocation() {
        return geoLocation;
    }

    public void setSuggestGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public char getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(char featureClass) {
        this.featureClass = featureClass;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCc2() {
        return cc2;
    }

    public void setCc2(String cc2) {
        this.cc2 = cc2;
    }

    public List<String> getAdminValues() {
        return adminValues;
    }

    public void setAdminValues(List<String> adminValues) {
        this.adminValues = adminValues;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getElevation() {
        return elevation;
    }

    public void setElevation(long elevation) {
        this.elevation = elevation;
    }

    public long getDem() {
        return dem;
    }

    public void setDem(long dem) {
        this.dem = dem;
    }

    public String getCountryZipCode() {
        return countryZipCode;
    }

    public void setCountryZipCode(String countryZipCode) {
        this.countryZipCode = countryZipCode;
    }

    public DateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(DateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }


    /**
     * Create SuggestDocument from the given set of values. Here, we expect all the array elements are provided and
     * in expected order
     * @param values    array of values
     * @return  created SuggestDocument
     */
    public static SuggestDocument create(String[] values) {
        SuggestDocument suggestDocument = new SuggestDocument();
        suggestDocument.setId(values[0]);
        suggestDocument.setName(values[1]);
        suggestDocument.setNameAscii(values[2]);
        suggestDocument.setNameAlternative(values[3]);
        suggestDocument.setSuggestGeoLocation(new GeoLocation(Double.parseDouble(values[4]), Double.parseDouble(values[5])));
        suggestDocument.setFeatureClass(values[6].charAt(0));
        suggestDocument.setFeatureCode(values[7]);
        suggestDocument.setCountry(values[8]);
        suggestDocument.setCc2(values[9]);
        suggestDocument.setAdminValues(Lists.newArrayList(values[10], values[11], values[12], values[13]));
        if (!StringUtils.isEmpty(values[14]))
            suggestDocument.setPopulation(Long.parseLong(values[14]));
        if (!StringUtils.isEmpty(values[15]))
            suggestDocument.setElevation(Long.parseLong(values[15]));
        if (!StringUtils.isEmpty(values[16]))
            suggestDocument.setDem(Long.parseLong(values[16]));
        suggestDocument.setCountryZipCode(values[17]);
        suggestDocument.setModifiedAt(DateTime.parse(values[18]));
        return suggestDocument;
    }
}
