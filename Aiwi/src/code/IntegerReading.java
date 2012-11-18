package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aiwi.Activator;
import aiwi.Messages;

public class IntegerReading {

	public static List<Integer> getIntegersFromFile(String filePath){
		List<Integer> numberList=new ArrayList<Integer>();

		try {
			Scanner scanner=new Scanner(new File(filePath));
			while(scanner.hasNextInt(10)){
				numberList.add(scanner.nextInt());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberList;
		
	}
	
	public static void main(String[] args) {
		String integerReading_NUMBER_LOCATION = Messages.IntegerReading_NUMBER_LOCATION;
		if(Activator.OS.equalsIgnoreCase("Linux")){
			integerReading_NUMBER_LOCATION = Messages.IntegerReading_NUMBER_LOCATION_LINUX;
		}
		List<Integer> integersFromFile = getIntegersFromFile(integerReading_NUMBER_LOCATION);
		for (Integer integer : integersFromFile) {
			System.out.println(integer);
		}
	}
}
