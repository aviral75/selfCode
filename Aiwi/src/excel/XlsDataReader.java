package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import aiwi.Activator;
import aiwi.Messages;

public class XlsDataReader {
	
	public static void main(String[] args) {
		populateAllData();
	}
	public static void populateAllData(){
		try {
			String xlsDataReader_ALL_NSE_XLS = Messages.XlsDataReader_ALL_NSE_XLS;
			if(Activator.OS.equalsIgnoreCase("Linux")){
				xlsDataReader_ALL_NSE_XLS = Messages.XlsDataReader_ALL_NSE_XLS_LINUX;
			}
			FileOutputStream fileOut = new FileOutputStream(xlsDataReader_ALL_NSE_XLS);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet("AllStkData"); //$NON-NLS-1$
			
			writeHeaderData(workbook, worksheet);
			int i=1;
			String baseDir=Messages.XlsDataReader_XLS_NSEFOLDER;
			if(Activator.OS.equalsIgnoreCase("Linux")){
				baseDir = Messages.XlsDataReader_XLS_NSEFOLDER_LINUX;
			}
			File inputDir=new File(baseDir);
			String[] list = inputDir.list();
			for (String string : list) {
				ExcelFields data=getValuesFrom(baseDir+File.separator+string,string);
				writeDataFrom(data,workbook,worksheet,i++);
				System.out.println("done with " + string); //$NON-NLS-1$
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private static ExcelFields getValuesFrom(String xlsPath,String sheetName) {
		ExcelFields data=new ExcelFields();
		sheetName=sheetName.substring(0,sheetName.lastIndexOf('.'));
		try {
			FileInputStream fileInputStream = new FileInputStream(xlsPath);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(sheetName);
			data.setSheetName(sheetName);
			data.setPrice(Double.parseDouble(worksheet.getRow(1).getCell(1).getStringCellValue()));
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			data.setTotalDailyReturn(worksheet.getRow(1).getCell(5).getNumericCellValue());//1,5 2,5,3,5,5,5
			CellValue cellValue = evaluator.evaluate(worksheet.getRow(2).getCell(5));
			data.setAvg(cellValue.getNumberValue());
			cellValue =evaluator.evaluate(worksheet.getRow(3).getCell(5));
			data.setStdDev(cellValue.getNumberValue());
			cellValue =evaluator.evaluate(worksheet.getRow(5).getCell(5));
			data.setSharpeRatio(cellValue.getNumberValue());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	@SuppressWarnings("deprecation")
	private static void writeDataFrom(ExcelFields data, HSSFWorkbook workbook,
			HSSFSheet worksheet,int i) {

		HSSFRow row1 = worksheet.createRow((short) i);
 
		
		HSSFCell cellA1 = row1.createCell((short) 0);
		cellA1.setCellValue(data.getSheetName());

		HSSFCell cellB1 = row1.createCell((short) 1);
		cellB1.setCellValue(data.getTotalDailyReturn());

		HSSFCell cellC1 = row1.createCell((short) 2);
		cellC1.setCellValue(data.getAvg());

		HSSFCell cellD1 = row1.createCell((short) 3);
		cellD1.setCellValue(data.getStdDev());

		HSSFCell cellE1 = row1.createCell((short) 4);
		cellE1.setCellValue(data.getSharpeRatio());
		
		HSSFCell cellF1 = row1.createCell((short) 5);
		cellF1.setCellValue(data.getPrice());

	}

	@SuppressWarnings("deprecation")
	private static void writeHeaderData(HSSFWorkbook workbook,HSSFSheet worksheet) {

		HSSFRow row1 = worksheet.createRow((short) 0);
 
		
		HSSFCell cellA1 = row1.createCell((short) 0);
		cellA1.setCellValue("Symbol"); //$NON-NLS-1$

		HSSFCell cellB1 = row1.createCell((short) 1);
		cellB1.setCellValue("Total daily return"); //$NON-NLS-1$

		HSSFCell cellC1 = row1.createCell((short) 2);
		cellC1.setCellValue("Average Daily return"); //$NON-NLS-1$

		HSSFCell cellD1 = row1.createCell((short) 3);
		cellD1.setCellValue("Std dev"); //$NON-NLS-1$

		HSSFCell cellE1 = row1.createCell((short) 4);
		cellE1.setCellValue("Sharpe Ratio"); //$NON-NLS-1$
		
		HSSFCell cellF1 = row1.createCell((short) 5);
		cellF1.setCellValue("Close Price"); //$NON-NLS-1$

		worksheet.setColumnWidth(0, worksheet.getColumnWidth(0)+3000);
		worksheet.setColumnWidth(1, worksheet.getColumnWidth(1)+3000);
		worksheet.setColumnWidth(2, worksheet.getColumnWidth(2)+3000);
		worksheet.setColumnWidth(3, worksheet.getColumnWidth(3)+3000);
		worksheet.setColumnWidth(4, worksheet.getColumnWidth(4)+3000);
		worksheet.setColumnWidth(5, worksheet.getColumnWidth(5)+3000);
	}

	public static List<String> getSharpeStocks() {
		//Decided to keep it manual and may be change weekly after sorting on sharpe ratio e col
		List<String> sharpeList=new ArrayList<String>();
		sharpeList.add("CINEMAXIN"); //a2 //$NON-NLS-1$
		sharpeList.add("MINDACORP"); //a3 //$NON-NLS-1$
		sharpeList.add("RASOYPR"); //a4 //$NON-NLS-1$
		sharpeList.add("THEBYKE"); //a6 //$NON-NLS-1$
		return sharpeList;
	}
	

}

