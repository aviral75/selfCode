package excel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import aiwi.Activator;
import aiwi.Messages;

public class GetPercentage {
	public static void main(String[] args) {
	
		evaluateStockMarketData();
//		getBlogStokPercentage();
	}
		public static void evaluateStockMarketData(){
		String NL=System.getProperty("line.separator");
		List<String> tickrList=new ArrayList<String>();
		String getPercentage_NSELIST = Messages.GetPercentage_NSELIST;
		if(Activator.OS.equalsIgnoreCase("Linux")){
			getPercentage_NSELIST = Messages.GetPercentage_NSELIST_LINUX;
		}
		File f=new File(getPercentage_NSELIST);
		Scanner scanner;
		try {
			String fileName = "goodStocks.txt";
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			scanner = new Scanner(new FileInputStream(f));
			while(scanner.hasNext()){
				String next = scanner.next();
				if(next.length() > 9 )
					next=next.substring(0,9);
				tickrList.add(next);
			}

			double percent=0.0;
			for (String string : tickrList) {
				string=string.trim();
				percent=getStockPerformance(string);
				if(percent>5.0){
					bw.append(string+","+percent+NL);
					System.out.print(string+" : "+percent+" ");
				}
			}
			bw.close();
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		




	}
	
	public static double getBlogStokPercentage(){
		String[] names1= new String[]{"CINEMAXIN","RASOYPR","THEBYKE"};
		double percent=0.0;
		for (String string : names1) {
			string=string.trim();
			percent+=getStockPerformance(string);
			System.out.println(getStockPerformance(string));
		}
		System.out.println(percent);
		return percent;
	}

	public static double getStockPerformance(String stockSymbol){
		URL url = null;
		try {
			url=new URL("http://download.finance.yahoo.com/d/quotes.csv?s=%22"+stockSymbol+".NS%22&f=sohgpl1c1p2d1t1k3&e=.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		URLConnection openConnection = null;
		try {
			openConnection = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		InputStream inputStream = null;
		try {
			inputStream = openConnection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedInputStream bis = new BufferedInputStream(inputStream);

		String getPercentage_NSEFOLDER = Messages.GetPercentage_NSEFOLDER;
			if(Activator.OS.equalsIgnoreCase("Linux")){
				getPercentage_NSEFOLDER = Messages.GetPercentage_NSEFOLDER_LINUX;
			}
			File file = new File(getPercentage_NSEFOLDER+stockSymbol+".csv"); //$NON-NLS-2$
		try {
			file.createNewFile();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(!file.exists())
		{
			System.out.println("File " + file + " could not be created");
		}


		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] byteArray = new byte[300];
		try {
			while(bis.read(byteArray) != -1)
			{
				fileOutputStream.write(byteArray);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				bis.close();
				inputStream.close();
				fileOutputStream.flush();
				fileOutputStream.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		double parseDouble =0.0;
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";  

			while ((line = reader.readLine()) != null) {  
				StringTokenizer tokenizer = new StringTokenizer(line,",");  
				int columnIndex = 0;  
				while (tokenizer.hasMoreTokens()) {  

					switch (columnIndex) {  
					case 7:  
						String per=tokenizer.nextToken();
						per=per.substring(1,per.length()-2);
						try{
							parseDouble=Double.parseDouble(per);
						}catch(Exception e){
							parseDouble=0.0;
						}
					case 8:
						if(parseDouble>5.0){
							 tokenizer.nextToken();
						}
					default:
						if(tokenizer.hasMoreTokens())
							tokenizer.nextToken();
					}  

					columnIndex++;  
				}  

			}  
			reader.close();  
			file.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


		return parseDouble;
	}
}
