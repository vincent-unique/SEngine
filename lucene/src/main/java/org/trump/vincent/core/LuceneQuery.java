package org.trump.vincent.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * Created by Vincent on 2017/12/7 0007.
 */
public class LuceneQuery extends QueryParser {

    public LuceneQuery(Version matchVersion, String field, Analyzer analyzer){
        super(matchVersion,field,analyzer);
    }
}
