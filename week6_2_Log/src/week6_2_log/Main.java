package week6_2_log;

import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		System.gc();
		long preUseMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();
		
		GalileoData data = new GalileoData();	
		ArrayList<String> readData = data.readFile(new File("src/week6_2_log/galileo.log"));
		data.writeFile(new File("src/week6_2_log/data.log"), readData);
		
		GalileoDataMinute datam = new GalileoDataMinute();
		ArrayList<String> readDatam = datam.readFile(new File("src/week6_2_log/data.log"));
		datam.writeFile(new File("src/week6_2_log/dataMinute.log"), readDatam);
		
		long end = System.currentTimeMillis();
		
		System.gc();
		long aftUseMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println("실행 시간 : "+(end-start));
		System.out.println("메모리 사용량 : "+(aftUseMemory-preUseMemory));
	}//end main

}
