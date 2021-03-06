package excel;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import aiwi.Activator;
import aiwi.Messages;

public class CsvToXls
{
	public static void main(String args[]) throws IOException
	{
		String baseDir=Messages.CsvToXls_NSEFOLDER;
		if(Activator.OS.equalsIgnoreCase("Linux")){
			baseDir=Messages.CsvToXls_NSEFOLDER_LINUX;
		}
		File inputDir=new File(baseDir);
		String[] list = inputDir.list();
		for (String string : list) {
			convert(baseDir+File.separator+string,string);
			System.out.println("done with " + string); //$NON-NLS-1$
		}
	}
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void convert (String fName,String sheetName) throws IOException{
		ArrayList arList=null;
		ArrayList<Comparable> al=null;
		String thisLine;
		FileInputStream fis = new FileInputStream(fName);
		DataInputStream myInput = new DataInputStream(fis);
		int i=0;
		arList = new ArrayList<ArrayList<Comparable>>();
		while ((thisLine = myInput.readLine()) != null)
		{
			al = new ArrayList<Comparable>();
			String strar[] = thisLine.split(","); //$NON-NLS-1$
			for(int j=0;j<strar.length;j++)
			{
				if(j>0 && j<(strar.length-1)){
					continue;
				}
				if(j>0 && i>0){
					try{
						double parseDouble = Double.parseDouble(strar[j]);
						al.add(parseDouble);
					}catch(Exception e){
						continue;
					}
				}
				else
					al.add(strar[j]);
			}
			arList.add(al);
			i++;
		}

		try
		{
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet(sheetName);
			for(int k=0;k<arList.size();k++)
			{
				ArrayList<?> curData = (ArrayList<?>)arList.get(k);
				ArrayList<?> nextData= new ArrayList<Object>();
				if(k+1<arList.size())
					nextData=(ArrayList<?>)arList.get(k+1);
				HSSFRow row = sheet.createRow((short) 0+k);
				for(int p=0;p<curData.size();p++)
				{
					HSSFCell cell = row.createCell((short) p);
					cell.setCellValue(curData.get(p).toString());
					if(p==1){
					HSSFCell cell1 = row.createCell((short) p +1);
					if(k==0)
						cell1.setCellValue("daily return"); //$NON-NLS-1$
					else{
						try{
							double nV=Double.parseDouble(nextData.get(p).toString());
							double cV = Double.parseDouble(curData.get(p).toString());
							cell1.setCellValue((cV/nV)-1);
						}catch(Exception e){
							cell1.setCellValue(0.0);
						}
						
					}
					}
				}
			}
			writeEFCells(sheet,arList.size());
			String csvToXls_XLS_NSEFOLDER = Messages.CsvToXls_XLS_NSEFOLDER;
			if(Activator.OS.equalsIgnoreCase("Linux")){
				csvToXls_XLS_NSEFOLDER = Messages.CsvToXls_XLS_NSEFOLDER_LINUX;
			}
			FileOutputStream fileOut = new FileOutputStream(csvToXls_XLS_NSEFOLDER+sheetName+".xls"); //$NON-NLS-2$
			hwb.write(fileOut);
			fileOut.close();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} //main method ends
	}
	private static void writeEFCells(HSSFSheet sheet, int rows) {
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.createCell(5);
		cell.setCellValue("total return"); //$NON-NLS-1$
		
		row=sheet.getRow(1);
		cell=row.createCell(5);
		double tot=0.0;
		try{
			double hV = Double.parseDouble(row.getCell(1).getStringCellValue());
			double lV = Double.parseDouble(sheet.getRow(rows-2).getCell(1).getStringCellValue());
			tot=(hV/lV)-1;
		}catch(Exception e){
			
		}
		cell.setCellValue(tot);
//		if(tot > 1.0){
//			System.out.println(sheet.getSheetName() + " :: " +tot);
//		}
		
		row=sheet.getRow(2);
		cell=row.createCell(4);
		cell.setCellValue("avg daily return");
		cell=row.createCell(5);
		cell.setCellFormula("AVERAGE(C2:C"+(rows-2)+")"); //$NON-NLS-1$ //$NON-NLS-2$
	
		
		row=sheet.getRow(3);
		cell=row.createCell(4);
		cell.setCellValue("std dev"); //$NON-NLS-1$
		cell=row.createCell(5);
		cell.setCellFormula("STDEV(C2:C"+(rows-2)+")"); //$NON-NLS-1$ //$NON-NLS-2$
		
		
		row=sheet.getRow(5);
		cell=row.createCell(4);
		cell.setCellValue("sharpe ratio"); //$NON-NLS-1$
		cell=row.createCell(5);
		cell.setCellFormula("SQRT(250)*f3/f4"); //$NON-NLS-1$
//		double avg=(sheet.getRow(2).getCell(5).getNumericCellValue());
//		double stdDev=(sheet.getRow(3).getCell(5).getNumericCellValue());
//		double numericCellValue = Math.sqrt(250)*avg/stdDev;
//		if(numericCellValue > 1.0){
//			System.out.println(sheet.getSheetName() + " -> " +numericCellValue);
//		}
		
		sheet.setColumnWidth(0, sheet.getColumnWidth(0)+1000);
		sheet.setColumnWidth(4, sheet.getColumnWidth(4)+2000);
	}
}