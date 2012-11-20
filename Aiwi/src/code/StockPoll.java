package code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import aiwi.Activator;
import aiwi.Messages;

public class StockPoll {
	public static double PERCENT=4.70;

	public static void main(String[] args) {
		poll();
	}

	public static Date poll() {
		URL url;
		long t=System.currentTimeMillis();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd"); //$NON-NLS-1$
		Date date = new Date();
		String fileName = Messages.StockPoll_STOCK_XML+dateFormat.format(date)+".txt"; //$NON-NLS-2$
		if(Activator.OS.equalsIgnoreCase("Linux")){
			fileName = Messages.StockPoll_STOCK_XML_LINUX+dateFormat.format(date)+".txt"; //$NON-NLS-2$
		}
		File file = new File(fileName);
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		
			//use FileWriter to write file
			FileWriter fw;
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// get URL content
			String stockPoll_NUMBER_READING = Messages.StockPoll_NUMBER_READING;
			if(Activator.OS.equalsIgnoreCase("Linux")){
				stockPoll_NUMBER_READING = Messages.StockPoll_NUMBER_READING_LINUX;
			}
			List<Integer> integersFromFile = IntegerReading.getIntegersFromFile(stockPoll_NUMBER_READING);
			for (Integer number : integersFromFile) {
				url = new URL("http://www.stockstobuynow.in/applications/api/stockapp/quotes/"+number+".xml"); //$NON-NLS-1$ //$NON-NLS-2$
				URLConnection conn = url.openConnection();

				BufferedReader br = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));



				domParsing(conn.getInputStream(),bw);
				//			String inputLine=br.readLine();
				//
				br.close();
				System.out.println("number " + number); //$NON-NLS-1$

			}
			System.out.println("Done in " + (System.currentTimeMillis()-t)); //$NON-NLS-1$
			bw.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return date;
	}

	private static void domParsing(InputStream inputStream,BufferedWriter bw) {
		// TODO Auto-generated method stub
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Scrip"); //$NON-NLS-1$
			List<Double> percentList=new ArrayList<Double>(); 
			String lastStock=""; //$NON-NLS-1$
			String scripCode=""; //$NON-NLS-1$
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode!=null && nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					scripCode = getTagValue("scripCode", eElement); //$NON-NLS-1$
					
					String closeValue = getTagValue("close", eElement); //$NON-NLS-1$
					double close = Double.parseDouble(closeValue);
					String prevCloseValue = getTagValue("prevClose", eElement); //$NON-NLS-1$
					double prevClose = Double.parseDouble(prevCloseValue);
					double percent=((close-prevClose)/prevClose)*100;
					lastStock= getTagValue("scripName", eElement); //$NON-NLS-1$
					percentList.add(percent);
				}
			}
			for (Double double1 : percentList) {
				if(double1 > PERCENT && percentList.get(0) >1.0){
					bw.write(lastStock+","); //$NON-NLS-1$
					lastStock=""; //$NON-NLS-1$
					break;
				}
			}
			if(lastStock.isEmpty()){
				for (Double double1 : percentList) 
					bw.write(double1+","); //$NON-NLS-1$
				if(percentList.size()>0){
					bw.write(scripCode);
					bw.write("\n"); //$NON-NLS-1$
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double getPerformancePercent(String number){
		double percent=0.0;
		URL url;
		try {
			url = new URL("http://www.stockstobuynow.in/applications/api/stockapp/quotes/"+number+".xml"); //$NON-NLS-1$ //$NON-NLS-2$

			URLConnection conn = url.openConnection();

			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(conn.getInputStream());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Scrip"); //$NON-NLS-1$
			List<Double> percentList=new ArrayList<Double>(); 
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode!=null && nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String closeValue = getTagValue("close", eElement); //$NON-NLS-1$
					double close = Double.parseDouble(closeValue);
					String prevCloseValue = getTagValue("prevClose", eElement); //$NON-NLS-1$
					double prevClose = Double.parseDouble(prevCloseValue);
					percent=((close-prevClose)/prevClose)*100;
					percentList.add(percent);
					break;
				}
			}

			br.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return percent;
	}
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
}