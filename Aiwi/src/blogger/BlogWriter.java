package blogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import code.CommonStocks;

import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.ServiceException;

import excel.XlsDataReader;

public class BlogWriter {

	private static final int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
	private static String feedUri;
	private static final String POSTS_FEED_URI_SUFFIX = "/posts/default";
	private static final String METAFEED_URL = 
		"http://www.blogger.com/feeds/default/blogs";

	private static final String FEED_URI_BASE = "http://www.blogger.com/feeds";
	public static final String PREDICTION_FILE = "predictions.txt";

	public static void main(String[] args) {
		BloggerService myService = new BloggerService("exampleCo-exampleApp-1");
		try {
			writeBlogPost(myService, args[0], args[1]);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeBlogPost(BloggerService myService, String userName,String userPassword) throws ServiceException, IOException {
		myService.setUserCredentials(userName, userPassword);

		// Get the blog ID from the metatfeed.
		String blogId = getBlogId(myService);
		feedUri = FEED_URI_BASE + "/" + blogId;

		// Demonstrate how to publish a public post.
		String dateForTitle = getDateForTitle(1);
		String title = "Free Intraday tips for "+dateForTitle+" using algorithms";	
		String content=getContent(dateForTitle);
		Entry publicPost = createPost(myService, title,content,
				"aviral", userName, false);
		System.out.println("Successfully created public post: "
				+ publicPost.getTitle().getPlainText());

//		System.out.println("Deleting published post");
//		deletePost(myService, publicPost.getEditLink().getHref());	// TODO Auto-generated method stub

	}

	private static String getContent(String dateForTitle) {
		String content=readContents();
		List<String> sharpeRatioStocks =XlsDataReader.getSharpeStocks();
		StringBuilder s1 = getHTMLStocks(sharpeRatioStocks);
		String today="2012_11_16";//getFileDate(getDateForTitle(0));//stock_2012_11_16.txt
		String yesterday="2012_11_15";//getFileDate(getDateForTitle(-1));
		List<String> intraDayStocks = CommonStocks.getIntraDayStocks(true,today,yesterday);
		StringBuilder s2 = getHTMLStocks(intraDayStocks);
		content=MessageFormat.format(content, dateForTitle,s1.toString(),s2.toString());
		writeToFile(sharpeRatioStocks,intraDayStocks);
		return content;
	}

	private static void writeToFile(List<String> sharpeRatioStocks,
			List<String> intraDayStocks) {
		try {
			File f=new File(PREDICTION_FILE);
			if(f.exists()){
				f.delete();
			}
			f.createNewFile();
			FileWriter fileWriter= new FileWriter(f);
			for (String string : sharpeRatioStocks) {
				fileWriter.write(string);
				fileWriter.write("\n");
			} 
			for (String string : intraDayStocks) {
				fileWriter.write(string);
				fileWriter.write("\n");
			} 

			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String getFileDate(String dateForTitle) {
		StringBuilder builder=new StringBuilder();
		String[] split = dateForTitle.split("-");
		builder.append(split[2]);
		builder.append("_");
		builder.append(split[1]);
		builder.append("_");
		builder.append(split[0]);
		return builder.toString();
	}

	private static StringBuilder getHTMLStocks(List<String> intraDayStocks) {
		StringBuilder s1=new StringBuilder();
		for (String string : intraDayStocks) {
			s1.append("<li>");
			s1.append(string);
			s1.append("</li>");
		}
		return s1;
	}

	/**
	 * @param num number of day previous to tomorrow
	 * @return tomorrow - num of days
	 * num 1 is tomorrow num 0 today num -1 yesterday 
	 */
	private static String getDateForTitle(int num) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy_MM_dd");
		Date date = new Date();
		String format = dateFormat.format(date.getTime());//+ MILLIS_IN_DAY);
		int day = dateFormat.getCalendar().get(Calendar.DAY_OF_WEEK);
		if(day == Calendar.FRIDAY ){
			format= dateFormat.format(date.getTime()+ 3*MILLIS_IN_DAY);
		}else if(day == Calendar.SATURDAY ){
			format= dateFormat.format(date.getTime()+ 2*MILLIS_IN_DAY);
		}else {//if(day == Calendar.SUNDAY )
			format= dateFormat.format(date.getTime()+ num*MILLIS_IN_DAY);
		}
		return format;
	}

	public static Entry createPost(BloggerService myService, String title,
			String content, String authorName, String userName, Boolean isDraft)
	throws ServiceException, IOException {
		// Create the entry to insert
		Entry myEntry = new Entry();
		myEntry.setTitle(new PlainTextConstruct(title));
		myEntry.setContent(new PlainTextConstruct(content));
		Person author = new Person(authorName, null, userName);
		myEntry.getAuthors().add(author);
		myEntry.setDraft(isDraft);

		// Ask the service to insert the new entry
		URL postUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
		return myService.insert(postUrl, myEntry);
	}

	public static void deletePost(BloggerService myService, String editLinkHref)
	throws ServiceException, IOException {
		URL deleteUrl = new URL(editLinkHref);
		myService.delete(deleteUrl);
	}

	private static String getBlogId(BloggerService myService)
	throws ServiceException, IOException {
		// Get the metafeed
		final URL feedUrl = new URL(METAFEED_URL);
		Feed resultFeed = myService.getFeed(feedUrl, Feed.class);

		// If the user has a blog then return the id (which comes after 'blog-')
		if (resultFeed.getEntries().size() > 0) {
			Entry entry = resultFeed.getEntries().get(0);
			return entry.getId().split("blog-")[1];
		}
		throw new IOException("User has no blogs!");
	}
	 private static String readContents() {
		 InputStream resourceAsStream = BlogWriter.class.getResourceAsStream("BlogContent.html");

		 final char[] buffer = new char[0x10000];
		 StringBuilder out = new StringBuilder();
		 Reader in=null;
		 try {
		 in = new InputStreamReader(resourceAsStream, "UTF-8");
		 } catch (UnsupportedEncodingException e2) {
		 }
		 int read = 0;
		 do {
		 try {
		 read = in.read(buffer, 0, buffer.length);
		 } catch (IOException e) {
		 }
		 if (read>0) {
		 out.append(buffer, 0, read);
		 }
		 } while (read>=0);
		 String result = out.toString();
		 return result;
		 }
}
