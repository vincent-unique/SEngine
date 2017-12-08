package org.trump.vincent.solr.core;

import com.google.gson.Gson;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vincent on 2017/12/7 0007.
 */
public class SolrConnection {
    public static String ConnetionUrl = "http://127.0.0.1:80/solr";
    public static String LOG_CORE = "logcore";

    private static SolrClient solrClient;

    private static ReentrantLock lock = new ReentrantLock(true);

    public static SolrClient getSolrClient(){
        return getCoreClient(ConnetionUrl,LOG_CORE);
    }

    public static SolrClient getCoreClient(String connetionUrl,String coreName){
        if(solrClient==null) {
            try {
                URL coreConnectUrl = new URL(connetionUrl + "/" + coreName);
                lock.lock();
                if(solrClient==null) {
                    solrClient = new HttpSolrClient.Builder(coreConnectUrl.toString()).build();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        return solrClient;
    }

    public static void main(String[] args)throws SolrServerException,IOException {
//       testIndex();
        testRetrieve();
    }

    public static void testIndex() throws SolrServerException,IOException{
        SolrIndexer indexer = new SolrIndexer("http://100.0.10.201:8080/solr4cre","metacore");
        indexer.delete("4324FF49-AE24-48EC-AF21-6D5749ACA4DA");
    }

    public static void testRetrieve() throws SolrServerException,IOException{
        SolrRetriever retriever = new SolrRetriever("http://100.0.10.201:8080/solr4cre","metacore");
        SolrQuery query = new SolrQuery("*:*");
        List<SolrDocument> documents = retriever.query(query);
        if(documents!=null&&documents.size()>0){
            for(SolrDocument document:documents) {
                System.out.println(new Gson().toJson(document));
            }
        }
    }
}
