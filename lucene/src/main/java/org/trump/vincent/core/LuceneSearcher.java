package org.trump.vincent.core;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.constants.LuceneConstants;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;

/**
 * Created by Vincent on 2017/12/4 0004.
 */
public class LuceneSearcher {

    private IndexSearcher searcher;
    private String indexDir;

    private Logger logger = LoggerFactory.getLogger(getClass());
    public LuceneSearcher(){

    }
    public LuceneSearcher(String directory){
        this.indexDir = directory;
        try {
            IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(FSDirectory.open(new File(directory))));
            this.searcher = indexSearcher;
        }catch (IOException e){
            logger.error("Exception occurs in building Lucene Searcher.");
        }
    }

    public Document[] find(String field,String query,Integer limit)throws IOException{
        QueryParser queryParser = new LuceneQuery(Version.LUCENE_CURRENT,field,new StandardAnalyzer(Version.LUCENE_CURRENT));
        try {
            Query _query = queryParser.parse(query);
            return find(_query,limit);
        }catch (ParseException e){
            logger.error("Exception occurs in parsing query.",e);
        }
        return null;
    }

    public void deleteByQuery(Term term,String indexDir)throws IOException{
        IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(indexDir)));
        indexReader.deleteDocuments(term);
    }

    public TopDocs query(Query query, Integer limit)throws IOException{
        if(query!=null){
            return this.searcher.search(query,limit!=null&&limit>0?limit: LuceneConstants.MAX_SEARCH);
        }
        return null;
    }

    public Document[] find(Query query, Integer limit)throws IOException{
        Document[] documents = new Document[limit!=null&&limit>0?limit: LuceneConstants.MAX_SEARCH];
        TopDocs topDocs = query(query,limit);
        ScoreDoc[] scoreHitDocs = topDocs==null?null:topDocs.scoreDocs;
        if(scoreHitDocs!=null&&scoreHitDocs.length>0){
            int len = scoreHitDocs.length;
            for(int i=0;i<len;i++){
                documents[i]=searcher.doc(scoreHitDocs[i].doc);
            }
        }else {
            documents=null;
        }
        return documents;
    }

    public void close()throws IOException{
        this.searcher.close();
    }

    public static void main(String[] args) {
        TermQuery query = new TermQuery(new Term("id",null));

    }
}