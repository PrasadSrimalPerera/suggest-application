package com.task.suggest.index.search;

import com.task.suggest.index.IndexField;

/**
 * Created by prasad on 7/1/18.
 * QueryScorer provides an interface to define concrete suggest query scoring techniques for different data fields/types
 */
interface QueryScorer <Type1, Type2 extends IndexField> {
    double calculateScore(Type1 type1, Type2 type2);
}
