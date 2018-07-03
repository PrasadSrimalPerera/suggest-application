package com.task.suggest.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prasad on 7/1/18.
 * IndexDocument represents the document structure used in storing and indexing entities with SuggestIndex
 */
public class IndexDocument extends IndexField {
    private static final long serialVersionUID = -9021246815272177792L;
    private final String documentId;
    private Map<String, IndexField> documentFields = new HashMap<>();

    public Map<String, IndexField> getDocumentFields() {
        return documentFields;
    }

    private IndexDocument(String documentId, Object value, boolean toIndex) {
        super(null, value, toIndex);
        this.documentId = documentId;
    }

    public IndexDocument(String documentId) {
        super(null, null, false);
        this.documentId = documentId;
    }

    public void setDocumentFields(Map<String, IndexField> documentFields) {
        this.documentFields = documentFields;
    }

    String getDocumentId() {
        return documentId;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("getValue is not supported for IndexDocument");
    }

    @Override
    public void setValue(Object value) {
        // Nothing to do here
    }

    @Override
    public List<SuggestToken> createSuggestTokens(SuggestIndex suggestIndex) throws IOException {
        throw new UnsupportedOperationException("createSuggestTokens is not supported for IndexDocument");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexDocument that = (IndexDocument) o;
        return documentId.equals(that.documentId);
    }

    @Override
    public int hashCode() {
        return documentId.hashCode();
    }
}