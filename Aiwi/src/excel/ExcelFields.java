package excel;

public class ExcelFields {
	private String sheetName;
	private double totalDailyReturn;
	private double avg;
	private double stdDev;
	private double sharpeRatio;
	private double price;
	
	public ExcelFields() {
	}
	public ExcelFields(String s,double td,double a,double sdev,double sr) {
		this.sheetName=s;
		this.totalDailyReturn=td;
		this.avg=a;
		this.stdDev=sdev;
		this.sharpeRatio=sr;
	}
	
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName.substring(0,sheetName.indexOf('.'));
	}

	public double getTotalDailyReturn() {
		return totalDailyReturn;
	}

	public void setTotalDailyReturn(double totalDailyReturn) {
		this.totalDailyReturn = totalDailyReturn;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	public double getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
