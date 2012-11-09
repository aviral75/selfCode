package feed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

public class FeedReader {
 public List<SyndEntryImpl> keepReadingFeeds(String url){
	 if(url==null){
		 url="http://www.moneycontrol.com/rss/buzzingstocks.xml";
	 }
	 FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
	 FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
	 SyndFeed feed=null;
	try {
		feed = feedFetcher.retrieveFeed(new URL(url));
		@SuppressWarnings("unchecked")
		List <SyndEntryImpl> entries = feed.getEntries();
		return entries;
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FeedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FetcherException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
 }
}
