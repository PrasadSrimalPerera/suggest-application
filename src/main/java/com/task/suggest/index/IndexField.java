package com.task.suggest.index;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by prasad on 7/1/18.
 * IndexField represent a feild in indexable documents. This is an abstract field and needs to extended for concrete
 * indexable fields such as, String, Double, Long, Geo types.
 */
public abstract class IndexField implements Serializable {

    private static final long serialVersionUID = -7068209809296771181L;
    private boolean toIndex;
    private String field;
    public abstract Object getValue() ;
    abstract void setValue(Object value);
    abstract List<SuggestToken> createSuggestTokens(SuggestIndex suggestIndex) throws IOException;


    /**
     * Constructor for IndexField
     * @param field field key
     * @param value field value
     * @param toIndex   to mark indexable field
     */
    IndexField(String field, Object value, boolean toIndex) {
        setValue(value);
        this.field = field;
        this.toIndex = toIndex;
    }

    boolean isToIndex() {
        return toIndex;
    }

    public void setToIndex(boolean toIndex) {
        this.toIndex = toIndex;
    }

    String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    /**
     * The concrete implementation of IndexField class for String type indexable data
     */
    public static class StringIndexField extends IndexField {
        private String value;

        @Override
        public void setValue(Object value) {
            this.value = value.toString();
        }

        public StringIndexField(String field, String value, boolean toIndex) {
            super(field, value, toIndex);
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public List<SuggestToken> createSuggestTokens(SuggestIndex suggestIndex) throws IOException {
            return suggestIndex.getSuggestIndexTokens(this.getValue(), getField());
        }
    }

    /**
     * Concrete implemenation of IndexField for Long type data
     */
    public static class LongIndexField extends IndexField {
        private Long value;

        public LongIndexField(String field, Long value, boolean toIndex) {
            super(field, value, toIndex);
        }

        @Override
        public Long getValue() {
            return this.value;
        }

        @Override
        public void setValue(Object value) {
            this.value = Long.parseLong(value.toString());
        }

        @Override
        public List<SuggestToken> createSuggestTokens(SuggestIndex suggestIndex) throws IOException {
            // Nothing to do here for now
            return null;
        }
    }

    /**
     * Concrete implementation of IndexField for GeoLocation type data
     */
    public static class GeoIndexField extends IndexField {
        private GeoLocation value;

        public GeoIndexField(String field, GeoLocation value, boolean toIndex) {
            super(field, value, toIndex);
        }

        @Override
        public GeoLocation getValue() {
            return value;
        }

        @Override
        public void setValue(Object value) {
            this.value = (GeoLocation) value;
        }

        @Override
        public List<SuggestToken> createSuggestTokens(SuggestIndex suggestIndex) throws IOException {
            // Nothing to do here for now
            return null;
        }
    }
}
