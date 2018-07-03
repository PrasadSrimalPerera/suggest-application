package com.task.suggest.index;

import java.io.Serializable;

/**
 * Created by prasad on 6/29/18.
 * SuggestToken used as the indexing key value for SuggestIndex
 */
public class SuggestToken implements Serializable {
    private static final long serialVersionUID = 2881957978310331318L;
    private String token;
    private long tokenLength;
    private String field;

    SuggestToken(String token, String field) {
        this.token = token;
        this.field = field;
    }

    public String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }

    public long getTokenLength() {
        return tokenLength;
    }

    void setTokenLength(long tokenLength) {
        this.tokenLength = tokenLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuggestToken that = (SuggestToken) o;
        return token.equals(that.token) && field.equals(that.field);
    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }
}
