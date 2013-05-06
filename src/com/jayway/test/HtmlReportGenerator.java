package com.jayway.test;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HtmlReportGenerator {


	static File file = null;
	static BufferedWriter bw = null;
	static int serialNumbers;
	
	public static void createHTMLFile(HashMap<Integer, ArrayList<String>> testComplete, String fileName) throws IOException{
		
		file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		bw = new BufferedWriter(new FileWriter(fileName));
		startHtml(fileName);
		
		Iterator iter = testComplete.entrySet().iterator();
		
		int testCasePassCount = 0, testCaseFailCount = 0;
		
		while(iter.hasNext()){
			Map.Entry<Integer, ArrayList<String>> testCompleteItr = (Map.Entry<Integer, ArrayList<String>>) iter.next(); 
			ArrayList<String> testCaseInfo1 = testCompleteItr.getValue();
			int errorCount = 0;
			int parameterCount = 0;
			int actualCount = 0;
			int expectedCount = 0;
			int rowSpan = 0;
			
			ArrayList<String> parameterlist = new ArrayList<String>();
			String[] parameters=null;
			String timestamp = null;
			ArrayList<String> errors = new ArrayList<String>();
			ArrayList<String> actualValues = new ArrayList<String>();
			ArrayList<String> expectedValues = new ArrayList<String>();
			
			for(String info : testCaseInfo1){
				if(info.contains(WeatherConstants.TEXT_ERROR_LOGS)){
					info = info.substring(info.indexOf("] - ")+4, info.length());
					errors.add(info);
					errorCount++;
				}else if(info.contains(WeatherConstants.TC_PARAMETERS)){
					info = info.substring(info.indexOf(WeatherConstants.TC_PARAMETERS)+(WeatherConstants.TC_PARAMETERS.length()), info.length());
					parameters = info.split(";");
					parameterCount = parameters.length;
				}else if(info.contains(WeatherConstants.TEXT_NO_PARAMETERS)){
					info = info.substring(info.indexOf("] - ")+4, info.length());
					parameterlist.add(info);
				}else if(info.contains(WeatherConstants.TEXT_ACTUAL_VALUES)){
					info = info.substring(info.indexOf("] - ")+(WeatherConstants.TEXT_ACTUAL_VALUES.length())+4, info.length());
					actualValues.add(info);
					actualCount++;
				}else if(info.contains(WeatherConstants.TEXT_EXPECTED_VALUES)){
					info = info.substring(info.indexOf("] - ")+(WeatherConstants.TEXT_EXPECTED_VALUES.length())+4, info.length());
					expectedValues.add(info);
					expectedCount++;
				}else if(info.contains(WeatherConstants.TC_TIMESTAMP)){
					info = info.substring(info.indexOf("] - ")+(WeatherConstants.TC_TIMESTAMP.length())+4, info.length());
					timestamp = info;
				}
			}
			
			
			rowSpan = Collections.max(Arrays.asList(errorCount, parameterCount, actualCount, expectedCount));
			/*if(errorCount >= parameterCount){
				rowSpan = errorCount;
			}else{
				rowSpan = parameterCount;
			}*/
			
			//bw.write("<tr>");
			serialNumbers = serialNumbers + 1;
			//writeToHtml(String.valueOf(serialNumbers),rowSpan);
			
			boolean noParameterFlag = false;
			for(int i = -1; i <= testCaseInfo1.size(); i++){
				if(i == -1){
					bw.write("<tr>");
					writeToHtml(String.valueOf(serialNumbers),rowSpan);
				}else if(i == 0){
					String methodName = testCaseInfo1.get(i);
					methodName = methodName.substring(methodName.indexOf("] - ")+4, methodName.length());
					writeToHtml(methodName, rowSpan);
				}else if(i == 1){
					
					if(!parameterlist.isEmpty() && parameterlist.get(0).contains(WeatherConstants.TEXT_NO_PARAMETERS)){
						// need to verify 
						noParameterFlag=true;
						writeToHtml(parameterlist.get(0), rowSpan);
					}else{
						writeToHtml(parameters[0], 0);
					}
				}else if(i == 2){
					if(errorCount >= 1){
						writeToHtml(WeatherConstants.TEXT_FAIL,rowSpan);
						writeToHtml(errors.get(0), 0);
						testCaseFailCount++;
					}else {
						writeToHtml(WeatherConstants.TEXT_PASS, rowSpan);
						writeToHtml("-", 0);
						testCasePassCount++;
					}
				}else if(i == 3){
					if(!actualValues.isEmpty()){
						writeToHtml(actualValues.get(0), 0);
					}else{
						writeToHtml("-", 0);
					}
				}else if(i == 4){
					if(!expectedValues.isEmpty()){
						writeToHtml(expectedValues.get(0), 0);
					}else{
						writeToHtml("-", 0);
					}
				}else if(i == 5){
					if(null!=timestamp){
						writeToHtml(timestamp, rowSpan);
					}
					bw.write("</tr>");
					break;
				}
				
			}
			
			for(int l = 1; l < rowSpan; l++){
				bw.write("<tr>");
				if(l <= (parameterCount-1)){
					if(parameters.length != 0){
						writeToHtml(parameters[l], 0);
					}
				}else if(!noParameterFlag){
				     writeToHtml("-", 0);
			    }
				
				if(l <= (errorCount - 1)){
					if(errors.size() != 0){
						writeToHtml(errors.get(l), 0);
					}
				}else{
					writeToHtml("-", 0);
				}
				
				if(l <= (actualCount - 1)){
					if(actualValues.size() != 0){
						writeToHtml(actualValues.get(l), 0);
					}
				}else{
					writeToHtml("-", 0);
				}
				
				if(l <= (expectedCount - 1)){
					if(expectedValues.size() != 0){
						writeToHtml(expectedValues.get(l), 0);
					}
				}else{
					writeToHtml("-", 0);
				}
				
				bw.write("</tr>");
			}
			
		}
		
		bw.write("</table>");
		
		summary(testCasePassCount + testCaseFailCount, testCasePassCount, testCaseFailCount);
		endHtml(fileName);
	}
	
	public static void writeToHtml(String text, int rowSpan) throws IOException{
		if(WeatherConstants.TEXT_FAIL.equalsIgnoreCase(text)){
			bw.write("<td rowspan = " +rowSpan+">");
			bw.write("<font color='red'>");
		}else if(WeatherConstants.TEXT_PASS.equalsIgnoreCase(text)){
			bw.write("<td rowspan = " +rowSpan+">");
			bw.write("<font color='green'>");
		}else{
			bw.write("<td rowspan = " +rowSpan+">");
			bw.write("<font>");
		}
		
		bw.write(text);
		bw.write("</font>");
		bw.write("</td>");
	}
	
	public static void startHtml(String fileName) throws IOException{
		bw.write("<html>");
		bw.write("<head>");
		bw.write("<title>Test Case Execution Report.</title>");
		bw.write("<link rel='stylesheet' type='text/css' href='report.css'/>");
		bw.write("</head>");
		bw.write("<h1 align = 'center'>Test Case Execution Report</h1>");
		bw.write("<body>");
		bw.write("<table border = '2px' class='hovertable' align = 'center'>");
		bw.write("<tr>");
		bw.write("<th><b>TC_No.</b></th>");
		bw.write("<th><b>TC_Name</b></th>");
		bw.write("<th><b>Parameters</b></th>");
		bw.write("<th><b>Test Case Status</b></th>");
		bw.write("<th><b>Error Message</b></th>");
		bw.write("<th><b>Actual Value</b></th>");
		bw.write("<th><b>Expected Value</b></th>");
		bw.write("<th><b>Execution Time (In seconds)</b></th>");
		bw.write("</tr>");
	}
	
	public static void summary(int totalTestCases, int passCount, int failCount) throws IOException{
		bw.write("<br><br>");
		
		bw.write("<table border = '2px' class='hovertable' align = 'center'>");
		bw.write("<tr>");
		bw.write("<th colspan = '3'><b>Summary</b></th>");
		bw.write("</tr>");
		
		bw.write("<tr>");
		bw.write("<th><b>Total test cases</b></th>");
		bw.write("<th><b>Passed</b></th>");
		bw.write("<th><b>Failed</b></th>");
		bw.write("</tr>");
		
		bw.write("<tr>");
		bw.write("<td align = 'center'>");
		bw.write(String.valueOf(totalTestCases));
		bw.write("</td>");
		
		bw.write("<td align = 'center'>");
		bw.write("<font color='green'>");
		bw.write(String.valueOf(passCount));
		bw.write("</font>");
		bw.write("</td>");
		
		bw.write("<td align = 'center'>");
		bw.write("<font color='red'>");
		bw.write(String.valueOf(failCount));
		bw.write("</font>");
		bw.write("</td>");
		
		bw.write("</tr>");
		bw.write("</table>");
	}
	
	public static void endHtml(String fileName) throws IOException{
		bw.write("</body>");
		bw.write("</html>");
		bw.close();
	}


}
