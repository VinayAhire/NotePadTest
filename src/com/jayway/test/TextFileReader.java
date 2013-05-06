package com.jayway.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class TextFileReader {

	
	static File file = null;
	static BufferedReader br = null;
	
	public static HashMap<Integer, ArrayList<String>> readFromFile(String fileName) throws IOException{
		
		System.out.println("Method Name: "+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		br = new BufferedReader(new FileReader(fileName));
		
		String currentLine = null;
		
		boolean flagTestCaseStatus = false; 
		
		ArrayList<String> testCaseInfo = new ArrayList<String>();
		
		HashMap<Integer, ArrayList<String>> testComplete = new HashMap<Integer, ArrayList<String>>();
		
		int key = 0;
		
		while(null!=(currentLine=br.readLine())){
			if(currentLine.contains("---------------") && flagTestCaseStatus == false ){
				flagTestCaseStatus = true;
				continue;
			} else if(currentLine.contains("---------------") && flagTestCaseStatus == true ){
				flagTestCaseStatus = false;
			}
			
			if(flagTestCaseStatus){
				testCaseInfo.add(currentLine);
			}else{
				
				if(currentLine.contains("---------------")){
					key++;
					testComplete.put(key, testCaseInfo);
					testCaseInfo = new ArrayList<String>();
				}
			}
		}
		
		Iterator iter = testComplete.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<Integer, ArrayList<String>> testCompleteItr = (Map.Entry<Integer, ArrayList<String>>) iter.next(); 
			System.out.println("--------------------------------------------");
			System.out.println("Key: " + testCompleteItr.getKey());
			System.getProperty("line.separator");
			System.out.println("Value: " + testCompleteItr.getValue());
			System.getProperty("line.separator");
			ArrayList<String> testCaseInfo1 = testCompleteItr.getValue();
			for(int i = 0; i < testCaseInfo1.size(); i++){
				System.out.println("Key: "+key+" ArrayList: "+testCaseInfo1.get(i));
				System.getProperty("line.separator");
			}
			System.out.println("--------------------------------------------");
		}
		
		return testComplete;
	}


}
