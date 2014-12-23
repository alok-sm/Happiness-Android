package sri.sri.happy;


import java.io.File;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtils {

    public static String PostToTwitter(File file, String message, ConfigurationBuilder cb){
        Twitter twitter =  new TwitterFactory(cb.build()).getInstance();
        StatusUpdate statusUpdate;
        Status status = null;
        try{
            statusUpdate = new StatusUpdate(message);
            statusUpdate.setMedia(file);
            status = twitter.updateStatus(statusUpdate);
        }catch(TwitterException e){
            e.printStackTrace();
        }
        String ret;
        try{
             ret = status.getMediaEntities()[0].getMediaURL();
        }catch(NullPointerException e){
            ret =  "";
        }
        return ret;
    }

}
