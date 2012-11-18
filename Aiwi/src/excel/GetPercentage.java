package excel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
//	    String[] names1= new String[]{"CINEMAXIN","HOTELRUGB","RASOYPR","THEBYKE"};

		List<String> tickrList=new ArrayList<String>();
		String getPercentage_NSELIST = Messages.GetPercentage_NSELIST;
		if(Activator.OS.equalsIgnoreCase("Linux")){
			getPercentage_NSELIST = Messages.GetPercentage_NSELIST_LINUX;
		}
		File f=new File(getPercentage_NSELIST);
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(f));
			while(scanner.hasNext()){
				String next = scanner.next();
				if(next.length() > 9 )
					next=next.substring(0,9);
				tickrList.add(next);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double percent=0.0;
		for (String string : tickrList) {
			percent=getStockPerformance(string.trim());
			if(percent>5.0)
			System.out.println(string+" :: "+ percent); //$NON-NLS-1$
		}
	
		

	}
	
	@SuppressWarnings("finally")
	public static double getStockPerformance(String stockSymbol){
		double percent=0.0;
		
			URL url = null;
			try {
				url=new URL("http://download.finance.yahoo.com/d/quotes.csv?s=%22"+stockSymbol+".NS%22&f=sohgpl1c1p2d1t1k3&e=.csv"); //$NON-NLS-1$ //$NON-NLS-2$
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
				System.out.println("File " + file + " could not be created"); //$NON-NLS-1$ //$NON-NLS-2$
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


		    BufferedReader reader=null;
		    try {
		    	reader = new BufferedReader(new FileReader(file));
		    	String line = "";   //$NON-NLS-1$
		    	while ((line = reader.readLine()) != null) {  
		    		StringTokenizer tokenizer = new StringTokenizer(line,",");   //$NON-NLS-1$
		    		int columnIndex = 0;  
		    		while (tokenizer.hasMoreTokens()) {  

		    			switch (columnIndex) {  
		    			case 7:  
		    				String per=tokenizer.nextToken();
		    				per=per.substring(1,per.length()-2);
		    				 double parseDouble =0.0;
		    				 try{
		    					 parseDouble=Double.parseDouble(per);
		    				 }catch(Exception e){
		    					 
		    				 }finally{
		    					 return parseDouble;
		    				 }
		    			default:
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
		
		
		return percent;
	}
}
