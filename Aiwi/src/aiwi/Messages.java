package aiwi;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "aiwi.messages"; //$NON-NLS-1$
	/*
	 * Windows paths
	 */
	public static String CsvToXls_NSEFOLDER;
	public static String CsvToXls_XLS_NSEFOLDER;
	public static String GetPercentage_NSEFOLDER;
	public static String GetPercentage_NSELIST;
	public static String ReadURL_SAVE_URL_LOCATION;
	public static String XlsDataReader_ALL_NSE_XLS;
	public static String XlsDataReader_XLS_NSEFOLDER;
	public static String YURLTry_NSEFOLDER;
	public static String YURLTry_NSELIST;
	public static String IntegerReading_NUMBER_LOCATION;
	public static String StockLoader_LOOKUP_TXT;
	public static String StockPoll_NUMBER_READING;
	public static String StockPoll_STOCK_XML;
	/*
	 * Linux paths
	 */
	public static String CsvToXls_NSEFOLDER_LINUX;
	public static String CsvToXls_XLS_NSEFOLDER_LINUX;
	public static String GetPercentage_NSEFOLDER_LINUX;
	public static String GetPercentage_NSELIST_LINUX;
	public static String ReadURL_SAVE_URL_LOCATION_LINUX;
	public static String XlsDataReader_ALL_NSE_XLS_LINUX;
	public static String XlsDataReader_XLS_NSEFOLDER_LINUX;
	public static String YURLTry_NSEFOLDER_LINUX;
	public static String YURLTry_NSELIST_LINUX;
	public static String IntegerReading_NUMBER_LOCATION_LINUX;
	public static String StockLoader_LOOKUP_TXT_LINUX;
	public static String StockPoll_NUMBER_READING_LINUX;
	public static String StockPoll_STOCK_XML_LINUX;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
