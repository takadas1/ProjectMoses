import twitter4j.*;
import twitter4j.auth.AccessToken;
 
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
 

public class TweetUsingTwitter4jExample {
 
    public static void main(String[] args) throws IOException, TwitterException {
 
        //Your Twitter App's Consumer Key
        String consumerKey = "FNHYyyHAOIorxwGNWWX9A";
 
        //Your Twitter App's Consumer Secret
        String consumerSecret = "eB3GylcC54kCKwwdtRdUYb3HVJZLVQNGyNFZ01aKTyo";
 
        //Your Twitter Access Token
        String accessToken = "2357658374-U2q4lUn2ty2Q0zpIcXm7Wzve7rOTtUSm04FPlfH";
 
        //Your Twitter Access Token Secret
        String accessTokenSecret = "Tpz9heRnOq3a6L82FFoPoPgcZHLTWj6EyDOG27d0EAstw";
 
        //Instantiate a re-usable and thread-safe factory
        TwitterFactory twitterFactory = new TwitterFactory();
 
        //Instantiate a new Twitter instance
        Twitter twitter = twitterFactory.getInstance();
 
        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
 
        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
 
        //Instantiate and initialize a new twitter status update
        StatusUpdate statusUpdate = new StatusUpdate(
                //your tweet or status message
                "H-1B Transfer Jobs | Java Developer | Harrison, NY | 2 Years" +
                " - http://h1b-work-visa-usa.blogspot.com/2013/07/h-1b-transfer-jobs-java-developer_19.html");
       
       Status status = twitter.updateStatus(statusUpdate);
    }
 
}
