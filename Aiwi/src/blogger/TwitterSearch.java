package blogger;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterSearch {
	
	public static String searchTweet(String searchString) {
			StringBuilder tweeted=new StringBuilder();
			Twitter twitter = new TwitterFactory().getInstance();
	        try {
	            Query query = new Query(searchString);
	            QueryResult result;
//	            do {
	                result = twitter.search(query);
	                List<Tweet> tweets = result.getTweets();
	                for (Tweet tweet : tweets) {
	                	if(tweet.getIsoLanguageCode().equals("en")){
	                		String s=("@" + tweet.getFromUser() + " - " + tweet.getText());
	                		tweeted.append(s+"\n");
	                	}
	                }
//	            } while ((result.getQuery()) != null);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	        }
	        return tweeted.toString();
	}
	
	public static void main(String[] args) {
		String searchTweet = searchTweet("RAJ TELE");
		System.out.println(searchTweet);
	}
}
