package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CommonStocks {
	static double BASE_PERCENT=4.90;
	public static Set<String> getStockLookup(String filePath){
		Set<String> nameList=new HashSet<String>();

		try {
			Scanner scanner=new Scanner(new File(filePath));
			while(scanner.hasNextLine()){
//				VAIBHAV GEMS,11.96275071633239 , 0.5039596832253338 , 0.0 , 0.8714596949891191 , 0.0 , 
				String nextLine = scanner.nextLine();
				int indexComma = nextLine.indexOf(',');
				String name = nextLine.substring(0,indexComma);
				nameList.add(name);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return nameList;
		
	}

	 public static Set<String> getIntersection(Set<String> set1, Set<String> set2) {
	        boolean set1IsLarger = set1.size() > set2.size();
	        Set<String> cloneSet = new HashSet<String>(set1IsLarger ? set2 : set1);
	        cloneSet.retainAll(set1IsLarger ? set1 : set2);
	        return cloneSet;
	    }

	 
		public static Set<String> getCommonStockDetails(String filePath,Set<String> commonStocks){
			Set<String> nameList=new HashSet<String>();

			try {
				Scanner scanner=new Scanner(new File(filePath));
				while(scanner.hasNextLine()){
					//VAIBHAV GEMS,11.96275071633239 , 0.5039596832253338 , 0.0 , 0.8714596949891191 , 0.0 , 
					String nextLine = scanner.nextLine();
					int indexComma = nextLine.indexOf(',');
					String stkName = nextLine.substring(0,indexComma);
					if(commonStocks.contains(stkName)){
						String[] split = nextLine.split(",");
						if(split.length < 3) 
							continue;
						if(Double.parseDouble(split[1]) < BASE_PERCENT)
							continue;
						String details=split[0]+","+split[1]+","+split[2];
						nameList.add(details);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return nameList;
			
		}
		
		public static List<String> getIntraDayStocks(boolean useCache){
			List<String> result1Stocks=new ArrayList<String>();
			if(!useCache)
				StockPoll.poll();
			String prevDayFile="C:\\temp\\data\\stock_2012_11_07.txt";
			String curDayFile="C:\\temp\\data\\stock_2012_11_08.txt";
			Set<String> stockNames1 =getStockLookup(prevDayFile);
			Set<String> stockNames2 = getStockLookup(curDayFile);
			Set<String> commonStocks = getIntersection(stockNames1, stockNames2);
			Set<String> commonStockDetails = getCommonStockDetails(curDayFile, commonStocks);	
			for (String string : commonStockDetails) {
				System.out.println(string);
				result1Stocks.add(string.substring(0,string.indexOf(",")));
			}
			if(result1Stocks.size() > 5){
				BASE_PERCENT+=1.0;
				return getIntraDayStocks(true);
			}
			return result1Stocks;
		}
		
		public static void main(String[] args) {
//			StockPoll.poll();
//			String prevDayFile="C:\\temp\\data\\stock_2012_11_01.txt";
//			String curDayFile="C:\\temp\\data\\stock_2012_11_02.txt";
//			Set<String> stockNames1 =getStockLookup(prevDayFile);
//			Set<String> stockNames2 = getStockLookup(curDayFile);
//			Set<String> commonStocks = getIntersection(stockNames1, stockNames2);
//			Set<String> commonStockDetails = getCommonStockDetails(curDayFile, commonStocks);	
//			for (String string : commonStockDetails) {
//				System.out.println(string);
//			}
			
			
			
			String[] names=new String[]{"VKS PROJECTS","BROOKS LABS","CINEPRO","REL BANK ETF"};
for (String stkName : names) {
	String scriptCode = StockLoader.provider.getScriptCode(stkName);
	System.out.println(StockPoll.getPerformancePercent(scriptCode));
	
}
		}
}
