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
//		TestSuite hello = new TestSuite("C:\\mavenprj\\JmeterPerfRun\\src\\main\\resources\\JmxPropertiesSetting.xml");
		TasksConfigReader reader = new TasksConfigReader("C:\\mavenprj\\JmeterPerfRun\\src\\main\\resources\\B2B-SimpleRunningConfigs.xml");
		reader.setJMX_PropertiesSettingPath("C:\\mavenprj\\JmeterPerfRun\\src\\main\\resources\\JmxPropertiesSetting.xml");
		ArrayList<TestSuite> result = reader.getSuites();
		JMXWriter taskWriter = new JMXWriter("c:\\tmp\\B2B_Login_mainPage_Logout_80t_2r_200.jmx");
	
		for(TestSuite x:result) {
			System.out.println(x.getSuiteName()+ ","+ x.getTemplatePath());
			try {
				taskWriter.writeParasFromSuite(x,1);
				
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
}
