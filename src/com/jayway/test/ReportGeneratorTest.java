package com.jayway.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;

import com.jayway.test.HtmlReportGenerator;
import com.jayway.test.TextFileReader;

public class ReportGeneratorTest extends NotePadTest {


	public ReportGeneratorTest() throws ClassNotFoundException {
		super();
	}

	public void createHtmlReport(){

		try {
			HashMap<Integer, ArrayList<String>> testComplete = TextFileReader.readFromFile(Environment.getExternalStorageDirectory() + File.separator +"/Report & Logs/" + "myapp.log");
			HtmlReportGenerator.createHTMLFile(testComplete, Environment.getExternalStorageDirectory() + File.separator +"/Report & Logs/" + "Report.html");
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} 
	}
}
