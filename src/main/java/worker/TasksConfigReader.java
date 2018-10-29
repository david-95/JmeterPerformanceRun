package worker;

import java.io.File;
import java.util.ArrayList;

import java.util.Iterator;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import testbase.TestSuite;


public class TasksConfigReader {
	private Logger logger = LogManager.getLogger("mylog");
	private String tasksConfigPath = "DemoSimpleRunningConfigs.xml";
	private SAXReader reader = new SAXReader();
	private Document doc;
	private Element root;
	private String JMX_Properties_Setting_File="JmxPropertiesSetting.xml";

	public TasksConfigReader(String tasksConfigPath) throws DocumentException {
		this.tasksConfigPath = tasksConfigPath;
		logger.info("Read config file:"+tasksConfigPath);
		File configFile = new File(tasksConfigPath);
		this.doc = reader.read(configFile);
		this.root = doc.getRootElement();
	}
	
	public String getSuitesGroupName() {
		return this.root.attributeValue("name");
	}
	
	/**
	 * read xpath value and properties into object (TestSuite) lists
	 * @return
	 */
	public ArrayList<TestSuite> getSuites() {

		ArrayList<TestSuite> result = new ArrayList<TestSuite>();

		Iterator<?> suiteElements = root.elementIterator("suite");

		while (suiteElements.hasNext()) {
			TestSuite tmpSuite = new TestSuite(JMX_Properties_Setting_File);   
			Element valueSuite = (Element) suiteElements.next();
			
			//// Read suite properties
			//name
			String name = valueSuite.attribute("name").getText();
			tmpSuite.setSuiteName(name);
			//templatePath
			String templatePath=valueSuite.element("templatePath").getStringValue().trim();
			tmpSuite.setTemplatePath(templatePath);
			//jmxFolder
			String jmxFolder=valueSuite.element("jmxFolder").getStringValue().trim();
			tmpSuite.setJmxFolder(jmxFolder);
			//outputFolder
			String outputFolder=valueSuite.element("outputFolder").getStringValue().trim();
			tmpSuite.setOuputFolder(outputFolder);
			
			//Read JMX Properties
			
			tmpSuite.setTestSuitePropertiesValues(valueSuite.element("JmxPropertiesSetting"));
		
			
			//
			result.add(tmpSuite);
		}

		return result;
	}

	public void setJMX_PropertiesSettingPath(String filePath) {
		this.JMX_Properties_Setting_File=filePath;
	}
}
