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

public class StockPoll {
	public static double PERCENT=4.70;

	public static void main(String[] args) {
		poll();
	}

	public static Date poll() {
		URL url;
		long t=System.currentTimeMillis();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		Date date = new Date();
		String fileName = "C:\\temp\\data\\stock_"+dateFormat.format(date)+".txt";
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
			List<Integer> integersFromFile = IntegerReading.getIntegersFromFile("C:\\temp\\stock.txt");
			for (Integer number : integersFromFile) {
				url = new URL("http://www.stockstobuynow.in/applications/api/stockapp/quotes/"+number+".xml");
				URLConnection conn = url.openConnection();

				BufferedReader br = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));



				domParsing(conn.getInputStream(),bw);
				//			String inputLine=br.readLine();
				//
				br.close();
				System.out.println("number " + number);

			}
			System.out.println("Done in " + (System.currentTimeMillis()-t));
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

			NodeList nList = doc.getElementsByTagName("Scrip");
			List<Double> percentList=new ArrayList<Double>(); 
			String lastStock="";
			String scripCode="";
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode!=null && nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					scripCode = getTagValue("scripCode", eElement);
					
					String closeValue = getTagValue("close", eElement);
					double close = Double.parseDouble(closeValue);
					String prevCloseValue = getTagValue("prevClose", eElement);
					double prevClose = Double.parseDouble(prevCloseValue);
					double percent=((close-prevClose)/prevClose)*100;
					lastStock= getTagValue("scripName", eElement);
					percentList.add(percent);
				}
			}
			for (Double double1 : percentList) {
				if(double1 > PERCENT && percentList.get(0) >1.0){
					bw.write(lastStock+",");
					lastStock="";
					break;
				}
			}
			if(lastStock.isEmpty()){
				for (Double double1 : percentList) 
					bw.write(double1+",");
				if(percentList.size()>0){
					bw.write(scripCode);
					bw.write("\n");
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
			url = new URL("http://www.stockstobuynow.in/applications/api/stockapp/quotes/"+number+".xml");

			URLConnection conn = url.openConnection();

			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(conn.getInputStream());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Scrip");
			List<Double> percentList=new ArrayList<Double>(); 
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode!=null && nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String closeValue = getTagValue("close", eElement);
					double close = Double.parseDouble(closeValue);
					String prevCloseValue = getTagValue("prevClose", eElement);
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