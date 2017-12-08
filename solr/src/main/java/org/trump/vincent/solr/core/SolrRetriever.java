package org.trump.vincent.solr.core;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2017/12/7 0007.
 */
public class SolrRetriever {

    private SolrClient coreClient;

    public SolrRetriever(String solrUrl,String coreName){
        this.solrUrl = solrUrl;
        this.coreName = coreName;
        this.coreClient = SolrConnection.getCoreClient(this.solrUrl,this.coreName);
    }

    public List<SolrDocument> query(SolrQuery query)throws SolrServerException, IOException {
        QueryResponse response = coreClient.query(query, SolrRequest.METHOD.POST);
        if(response!=null){
            return response.getResults();
        }
        return null;
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public SolrRetriever setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
        return this;
    }

    public String getCoreName() {
        return coreName;
    }

    public SolrRetriever setCoreName(String coreName) {
        this.coreName = coreName;
        return this;
    }

    private String solrUrl;
    private String coreName;
}
