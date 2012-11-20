package excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aiwi.Activator;
import aiwi.Messages;

public class YURLTry {
	
	public static List<String> readInputFromFile() throws FileNotFoundException{
		List<String> tickrList=new ArrayList<String>();
		String yURLTry_NSELIST = Messages.YURLTry_NSELIST;
		if(Activator.OS.equalsIgnoreCase("Linux")){
			yURLTry_NSELIST= Messages.YURLTry_NSELIST_LINUX;
		}
		File f=new File(yURLTry_NSELIST);
		Scanner scanner=new Scanner(new FileInputStream(f));
		while(scanner.hasNext()){
			String next = scanner.next();
			if(next.length() > 9 )
				next=next.substring(0,9);
			if(!next.startsWith("^")) //$NON-NLS-1$
					next=next+".NS"; //$NON-NLS-1$
			tickrList.add(next);
		}
		return tickrList;
	}
	
	
	public static void main(String[] args) {
		List<String> readInputFromFile=new ArrayList<String>();
		try {
			readInputFromFile = readInputFromFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (String tickr : readInputFromFile) {
			writeData(tickr);
		}
	}

	
public static void writeData(String stockSymbol) {
	 	String sD="&a=0&b=1&c=2012"; //0 means month jan;1 is 1st jan 2012 is year //$NON-NLS-1$
    	String eD="&d=10&e=16&f=2012";//16-11-12 //$NON-NLS-1$
		URL url = null;
		try {
			url= new URL("http://ichart.finance.yahoo.com/table.csv?s="+stockSymbol+sD+eD+"&ignore=.csv"); //$NON-NLS-1$ //$NON-NLS-2$
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
			return;
		}

		InputStream inputStream = null;
		try {
			inputStream = openConnection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		BufferedInputStream bis = new BufferedInputStream(inputStream);
		String yURLTry_NSEFOLDER = Messages.YURLTry_NSEFOLDER;
		if(Activator.OS.equalsIgnoreCase("Linux")){
			yURLTry_NSEFOLDER = Messages.YURLTry_NSEFOLDER_LINUX;
		}
		File file = new File(yURLTry_NSEFOLDER+stockSymbol+".csv"); //$NON-NLS-2$
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
			return;
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

	}
}
