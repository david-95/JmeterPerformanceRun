package mytest;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import testbase.TestSuite;

import worker.JMXWriter;
import worker.RunningCommandsGenerator;
import worker.TasksConfigReader;

public class TasksConfigReaderWriterTest {
	
	public void testReader1() throws DocumentException {
		TasksConfigReader reader = new TasksConfigReader("C:\\mavenprj\\JmeterPerfRun\\src\\main\\resources\\DemoSimpleRunningConfigs.xml");
		reader.getSuites();
	}
	
	
	public void testFolderAction() {
		RunningCommandsGenerator testClz = new RunningCommandsGenerator("C:\\mavenprj\\JmeterPerfRun\\src\\main\\resources\\DemoSimpleRunningConfigs.xml");
		
	}
	
	@Test
	public void testSuite() throws DocumentException {

		TasksConfigReader reader = new TasksConfigReader("/home/jdan/javaWorkspace/JmeterPerformanceRun/src/main/resources/B2B-SimpleRunningConfigs.xml");
		reader.setJMX_PropertiesSettingPath("/home/jdan/javaWorkspace/JmeterPerformanceRun/src/main/resources/JmxPropertiesSetting.xml");
		ArrayList<TestSuite> result = reader.getSuites();
		JMXWriter taskWriter = new JMXWriter("/tmp/B2B-Login_mainPage_Logout.jmx");
	
		for(TestSuite x:result) {
			System.out.println(x.getSuiteName()+ ","+ x.getTemplatePath());
			try {
				taskWriter.writeParasFromSuite(x);
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}

	public static void main(String[] args) {
		TasksConfigReaderWriterTest tester = new TasksConfigReaderWriterTest();
		try {
			tester.testSuite();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
}
