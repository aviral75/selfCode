package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

import aiwi.Messages;

public enum StockLoader {
provider;

Map<String,String> stockCodeMap=new HashMap<String,String>();
public  String getScriptCode(String stkName){
	String code=stockCodeMap.get(stkName);
	if(code!=null)
		return code;
	try {
		Scanner scanner=new Scanner(new File(Messages.StockLoader_LOOKUP_TXT));
		while(scanner.hasNextLine()){
			String nextLine = scanner.nextLine();
			int indexComma = nextLine.indexOf(',');
			String stockCode = nextLine.substring(0,indexComma);
			String stockName=nextLine.substring(indexComma+1).trim();
			stockCodeMap.put(stockName, stockCode);
		}
	}catch(FileNotFoundException e){
		e.getMessage();
	}
	return stockCodeMap.get(stkName);
	}
}
