package aiwi.code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import aiwi.Messages;

public class ReadURL {
	public static void main(String[] args) {

		URL url;
		int j=528000;
		//save to this filename
		String fileName = Messages.ReadURL_SAVE_URL_LOCATION;
		try {
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);

			for(int number=j;number<530000;number+=1 ){
				// get URL content

				url = new URL("http://www.stockstobuynow.in/applications/api/stockapp/quotes/"+number+".xml"); //$NON-NLS-1$ //$NON-NLS-2$
				URLConnection conn = url.openConnection();

				// open the stream and put it into BufferedReader
				BufferedReader br = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));

				String inputLine=br.readLine();
				if(inputLine.length() < 50)
					continue;


				boolean done=false;
				do{
					int startIndex1=inputLine.indexOf("<scripCode>"); //$NON-NLS-1$
					int endIndex1 = inputLine.indexOf("</scripCode>"); //$NON-NLS-1$
					String substring = inputLine.substring(startIndex1,endIndex1+12);
					bw.write(substring);

					startIndex1=inputLine.indexOf("<scripName>"); //$NON-NLS-1$
					endIndex1 = inputLine.indexOf("</scripName>"); //$NON-NLS-1$
					substring = inputLine.substring(startIndex1,endIndex1+12);
					bw.write(substring + "\n"); //$NON-NLS-1$
					done=true;
				}while (((inputLine = br.readLine()) != null)||(!done));
				br.close();
			}
			bw.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("Done"); //$NON-NLS-1$
		}
	}
}