/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package job;

import java.util.Iterator;
import java.util.List;
import models.Tw;
import play.jobs.OnApplicationStart;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;

/**
 *
 * @author waxzce
 */
@OnApplicationStart
public class PopulateJob extends play.jobs.Job {

    @Override
    public void doJob() throws Exception {
        Twitter tt = new Twitter();
        Query q = new Query("#nantes OR #Paris");
        q.setRpp(100);

        QueryResult qr = tt.search(q);
        List<Tweet> lt = qr.getTweets();
        for (Iterator<Tweet> it = lt.iterator(); it.hasNext();) {
            Tweet tweet = it.next();
            Tw tw = new Tw();
            tw.text = tweet.getText();
            tw.username = tweet.getFromUser();
            tw.save();
        }
    }
}
