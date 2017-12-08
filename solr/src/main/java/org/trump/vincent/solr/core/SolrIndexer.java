package org.trump.vincent.solr.core;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Vincent on 2017/12/7 0007.
 */
public class SolrIndexer {
    private SolrClient coreClient;
    private Logger logger = LoggerFactory.getLogger(SolrIndexer.class);

    private String solrUrl;
    private String coreName;
    public SolrIndexer(String solrUrl,String coreName){
        this.solrUrl = solrUrl;
        this.coreName = coreName;
        this.coreClient = SolrConnection.getCoreClient(this.solrUrl,this.coreName);
    }

    public void index(Collection<SolrInputDocument> documents) throws SolrServerException, IOException {

        UpdateResponse response = coreClient.add(documents);
        coreClient.commit();
        logger.debug("[SolrIndex index Request]:" + response.getRequestUrl());
    }

    public void index(SolrInputDocument document) throws SolrServerException, IOException {
        UpdateResponse response = coreClient.add(document);
        coreClient.commit();
        logger.debug("[SolrIndex index Request]:" + response.getRequestUrl());
    }

    public void delete(String id) throws SolrServerException, IOException {
        UpdateResponse response = coreClient.deleteById(id);
        coreClient.commit();
        logger.debug("[SolrIndex Delete Request]:" + response.getRequestUrl());
    }

    public void delete(List<String> ids) throws SolrServerException, IOException {
        UpdateResponse response = coreClient.deleteById(ids);
        coreClient.commit();
        logger.debug("[SolrIndex Delete Request]:" + response.getRequestUrl());
    }

    public void deleteByQuery(String query) throws SolrServerException, IOException {
        UpdateResponse response = coreClient.deleteByQuery(query);
        coreClient.commit();
        logger.debug("[SolrIndex Delete Request]:" + response.getRequestUrl());
    }

    public void deleteAll()throws SolrServerException, IOException {
        deleteByQuery("*");
    }

    public void update(SolrInputDocument document)throws SolrServerException, IOException {
       String id = (String) document.getField("id").getValue();
       delete(id);
       index(document);
    }
    public void update(Collection<SolrInputDocument> documents)throws SolrServerException, IOException {
        if(documents!=null&&documents.size()>0){
            List<String> ids = new ArrayList<>();
            for(SolrInputDocument document:documents){
                String id = (String) document.getField("id").getValue();
                ids.add(id);
            }
            delete(ids);
            index(documents);
        }
    }

    /**
     * Base setter and getter
     * @return
     */
    public String getSolrUrl() {
        return solrUrl;
    }

    public SolrIndexer setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
        return this;
    }

    public String getCoreName() {
        return coreName;
    }

    public SolrIndexer setCoreName(String coreName) {
        this.coreName = coreName;
        return this;
    }
}
