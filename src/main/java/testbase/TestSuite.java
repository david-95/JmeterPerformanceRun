package testbase;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.io.SAXReader;

public class TestSuite {
	private static Logger logger = LogManager.getLogger("mylog");
	private SAXReader reader = new SAXReader();

	HashMap<String, String> TestSuitePropertiesXpath = null;
	HashMap<String, String> TestSuitePropertiesValue = null;
	private Document doc;
	private Element root;

	private String templatePath;
	private String name;
	private String outputFolder;
	private String jmxFolder;
	
	public TestSuite(String jMX_Properties_Setting_File) {
		try {
			init(jMX_Properties_Setting_File);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// for hash map TestSuitePropertiesXpath
	private void setTestSuitePropertyXpath(String key, String value) {
		if (TestSuitePropertiesXpath == null) {
			TestSuitePropertiesXpath = new HashMap<String, String>();
		}
		TestSuitePropertiesXpath.put(key, value);
	}

	private String getTestSuitePropertyXpath(String key) {
		if (TestSuitePropertiesValue == null || !TestSuitePropertiesXpath.containsKey(key)) {
			logger.info("Cannot get the key: " + key + "in suite TestSuitePropertiesXpath hash map");
			return null;
		}
		return TestSuitePropertiesXpath.get(key);
	}

	// for hash map TestSuitePropertiesValue
	public void setTestSuitePropertyValue(String key, String value) {
		if (TestSuitePropertiesValue == null) {
			TestSuitePropertiesValue = new HashMap<String, String>();
		}
		TestSuitePropertiesValue.put(key, value);
	}

	public String getTestSuitePropertyValue(String key) {
		if (TestSuitePropertiesValue == null || !TestSuitePropertiesValue.containsKey(key)) {
			logger.info("Cannot get the key: " + key + "in suite TestSuitePropertiesValue hash map");
			return null;
		}
		return TestSuitePropertiesValue.get(key);
	}

	// init,
	public void init(String jMX_Properties_Setting_File) throws Exception {
		File jmx_prosFile = new File(jMX_Properties_Setting_File);

		this.doc = reader.read(jmx_prosFile);
		this.root = ((Document) doc).getRootElement();
		
		setTestSuitePropertiesXpaths(root);
	}

	public void setTestSuitePropertiesXpaths(Element suiteElement) {
		Iterator<Element> nodeIterator = suiteElement.elementIterator();
		while (nodeIterator.hasNext()) {
			Element node = nodeIterator.next();
			String propName = node.getName();
			String xpath = node.element("JMX_XPath").getStringValue();
 			setTestSuitePropertyXpath(propName, xpath.trim());
 			logger.debug("working on " + propName + ",xpath="+xpath.trim());
		}
	}

	public int getTestThreadGroupPropertiesCount() {
		return this.TestSuitePropertiesXpath.size();
	}

	public HashMap<String, String> getSuitePropertiesValueMap() {
		// TODO Auto-generated method stub
		return this.TestSuitePropertiesValue;
	}
	public HashMap<String, String> getSuitePropertiesXpathMap() {
		// TODO Auto-generated method stub
		return this.TestSuitePropertiesXpath;
	}



	public void setSuiteName(String name) {
		this.name=name;	
	}
	public String getSuiteName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	public void setTemplatePath(String template) {
		this.templatePath=template;
	}

	public String getTemplatePath() {
		return this.templatePath;
	}

	public void setOuputFolder(String outputFolder) {
		this.outputFolder=outputFolder;
	}
	public String getOutputFolder() {
		return this.outputFolder;
	}
	public void setJmxFolder(String jmxFolder) {
		this.jmxFolder=jmxFolder;
	}
	public String getJmxFolder() {
		return this.jmxFolder;
	}
	
	public void setTestSuitePropertiesValues(Element valueSuite) {
		Iterator<Element> nodeIterator = valueSuite.elementIterator();
		while (nodeIterator.hasNext()) {
			Element node = nodeIterator.next();
			String propName = node.getName();
			String value = node.getStringValue();
 			setTestSuitePropertyValue(propName, value.trim());
 			logger.debug("working on " + propName + ",value="+value.trim());
		}
		
	}


	

}