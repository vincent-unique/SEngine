package org.trump.vincent.core;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trump.vincent.constants.LuceneConstants;
import org.trump.vincent.filter.FileFilter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Vincent on 2017/12/4 0004.
 */
public class LuceneWriter {
    private IndexWriter writer;

    Logger logger = LoggerFactory.getLogger(getClass());

    public LuceneWriter(IndexWriter writer) {
        this.writer = writer;
    }

    public LuceneWriter(Directory indexDir) {
        try {
            IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(Version.LUCENE_CURRENT,new StandardAnalyzer(Version.LUCENE_CURRENT)));
            this.writer = indexWriter;
        } catch (IOException e) {
            logger.error("Exception occurs in building Lucene Index Writer.");
        }
    }

    public LuceneWriter(String directory) {
        try {
//            Directory indexDir = new RAMDirectory();
            Directory indexDir = FSDirectory.open(new File(directory));
            IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(Version.LUCENE_CURRENT,new StandardAnalyzer(Version.LUCENE_CURRENT)));
            this.writer = indexWriter;
        } catch (IOException e) {
            logger.error("Exception occurs in building Lucene Index Writer.");
        }
    }

    public boolean index(Document document){
        try{
            this.writer.addDocument(document);
            this.writer.commit();
            return true;
        }catch (IOException e){
            logger.error("Exception occurs in writing lucene document.");
        }
        return false;
    }

    public Integer index(Collection<Document> document){
        try{
            this.writer.addDocuments(document);
            this.writer.commit();
            return this.writer.numDocs();
        }catch (IOException e){
            logger.error("Exception occurs in writing lucene document.");
        }
        return -1;
    }

    public boolean update(Document document, Term term){
        try {
            this.writer.updateDocument(term, document);
            this.writer.commit();
            return true;
        }catch (IOException e){
            logger.error("Exception occurs in updating lucene document.");
        }
        return false;
    }
    public boolean update(Collection<Document> documents, Term term){
        try {
            this.writer.updateDocuments(term,documents);
            this.writer.commit();
            return true;
        }catch (IOException e){
            logger.error("Exception occurs in updating lucene document.");
        }
        return false;
    }

    public boolean delete(Term ...terms){
        try {
            this.writer.deleteDocuments(terms);
            this.writer.commit();
            return true;
        }catch (IOException e){
            logger.error("Exception occurs in deleting Lucene index.");
        }
        return false;
    }

    public boolean deleteAll(){
        try {
            this.writer.deleteAll();
            this.writer.commit();
            return true;
        }catch (IOException e){
            logger.error("Exception occurs in deleting Lucene index.");
        }
        return false;
    }

    public void close()throws IOException{
        if(this.writer!=null){
            this.writer.close();
        }
    }

    public boolean updateFile(File file)throws IOException{
        Document document = buildDocument(file);
        Term term = new Term(LuceneConstants.FILE_NAME,file.getName());
        return update(document,term);
    }

    public boolean indexFile(File file) {
        try {
            Document document = buildDocument(file);
            return index(document);
        } catch (IOException e) {
            logger.error("Exception occurs in writing lucene document.");
        }
        return false;
    }

    public Integer indexFiles(String dir, FileFilter fileFilter) throws IOException {
        File origin = new File(dir);
        if (origin.isDirectory()) {
            File[] files = origin.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        indexFiles(file.getPath(),fileFilter);
                    } else if (file.isFile() && file.exists() && file.canRead()) {
                        indexFile(file);
                    }
                }
            }
        } else {
            if (fileFilter.accept(origin)) {
                indexFile(origin);
            }
        }
        return this.writer.numDocs();
    }

    public Document buildDocument(File file) throws IOException {
        if (file != null) {
            Document document = new Document();
            if (file.isDirectory()) {
//                throw new IllegalArgumentException("Your file path must not present directory.");
                Field nameField = new Field(LuceneConstants.FILE_NAME, file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
                document.add(nameField);
                Field pathField = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED);
                document.add(pathField);
            } else {
                Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file));
                document.add(contentField);
                Field nameField = new Field(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);
                document.add(nameField);
                Field pathField = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED);
                document.add(pathField);
            }
            return document;
        }
        return null;
    }

    public Collection<Document> buildDocuments(File...dirs){
        //TODO

        return null;
    }

    public void finialize(){
        try {
            this.writer.close();
            this.writer = null;
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
