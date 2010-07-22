package controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import models.Tw;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import play.db.jpa.JPA;
import play.mvc.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void search(String query) throws ParseException {

        EntityManager em = JPA.entityManagerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        org.hibernate.Session s = ((org.hibernate.Session)JPA.em().getDelegate()) ;
        FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(s);
        Transaction tx = fullTextSession.beginTransaction();

// create native Lucene query
        String[] fields = new String[]{"text", "username"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
        org.apache.lucene.search.Query queryl = parser.parse(query);

// wrap Lucene query in a org.hibernate.Query
        org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(queryl, Tw.class);

// execute search
        List result = hibQuery.list();

        tx.commit();
//        session.close();

        renderJSON(result);
    }
}
