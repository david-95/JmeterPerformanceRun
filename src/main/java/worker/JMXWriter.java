package worker;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import testbase.TestSuite;

/**
 * 
 * @author David An
 * This class is used to write properties to .jmx file
 */
public class JMXWriter {

	private String targetJMXpath;
	private Logger logger = LogManager.getLogger("mylog");

	public JMXWriter(String targetJMXpath) {
		this.targetJMXpath = targetJMXpath;
	}

	/**
	 * Function get suite instance
	 * Read testsuites xpath , open  target jmx  , locate component by xpath and set it's value 
	 * for example
	 * //		XPath xPath = XPathFactory.newInstance().newXPath();
	 * //		Node num_threadNode = (Node) xPath.compile("//ThreadGroup/stringProp[@name='ThreadGroup.num_threads']")
	 * //				.evaluate(doc, XPathConstants.NODE);
	 * //		num_threadNode.setTextContent(value_num_thread);
	 * //
	 * //		xPath = XPathFactory.newInstance().newXPath();
	 * //		Node ramp_timeNode = (Node) xPath.compile("//ThreadGroup/stringProp[@name='ThreadGroup.ramp_time']")
	 * //				.evaluate(doc, XPathConstants.NODE);
	 * //		ramp_timeNode.setTextContent(value_ramp_time);
	 * 
	 * 
	 * @param suite
	 * @throws XPathExpressionException 
	 * @throws NullPointerException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws Exception 
	 */
	public void writeParasFromSuite(TestSuite suite) throws NullPointerException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, SAXException, IOException  {


		File targetJMXFile = new File(targetJMXpath);
		if (targetJMXpath.trim().equals("") || targetJMXpath == null || targetJMXFile==null || !targetJMXFile.exists()) {
			throw new FileNotFoundException("Cannot find target jmx file!");
		}

		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = f.newDocumentBuilder();
		Document doc = b.parse(targetJMXFile);
		
		HashMap<String, String> valueMap = suite.getSuitePropertiesValueMap();
		HashMap<String, String> xpathMap = suite.getSuitePropertiesXpathMap();

		Set<String> keySet = valueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = valueMap.get(key);
			logger.debug("JMXWriter get key"+ key);
			String xpath = xpathMap.get(key).trim();
			logger.debug("JMXWriter get key"+ key+",xpath="+xpath+",value="+value);
			if (xpathMap.containsKey(key) && xpath != null) {
				doc = setXMLValueByXpath(doc,xpath ,value);
			}

		}
		validateJMX(suite,valueMap,doc);
		writeXmlDoc(doc,targetJMXFile);
	}
	/**
	 * validate the JMX  and print warning to log if have invalid setting
	 * @param suite
	 * @param valueMap
	 * @param doc
	 */
	public void validateJMX(TestSuite suite,HashMap<String, String> valueMap,Document doc ) {
		JMXValidator validator= new JMXValidator(doc);
		String schedulerValue = valueMap.get("ThreadGroup.scheduler");
		String durationValue= valueMap.get("ThreadGroup.duration");
		String loopCountValue = valueMap.get("LoopController.loops");
		validator.validateThreadsDurationAndLoopsByValue(suite.getSuiteName(),schedulerValue, durationValue, loopCountValue);
		
	}

	/**
	 *  open XML Document ， locate xpath  and  set it's value
	 * @param doc
	 * @param xpath
	 * @param value
	 * @return
	 * @throws NullPointerException
	 * @throws XPathExpressionException
	 */
	public Document setXMLValueByXpath(Document doc, String xpath, String value ) throws NullPointerException, XPathExpressionException {
		if(doc ==null || xpath == null ||  value == null || xpath.trim() =="") {
			 throw new NullPointerException("Document or Xpath or value is not valid ,maybe null");
		}
		XPath XPathLocator = XPathFactory.newInstance().newXPath();
		Node node = (Node) XPathLocator.compile(xpath).evaluate(doc, XPathConstants.NODE);
		node.setTextContent(value);
		return doc;
	}
	
	
	/**
	 * write xml document values to JMX file 
	 * @param doc
	 * @param targetJMXFile
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void writeXmlDoc(Document doc ,File targetJMXFile) throws TransformerFactoryConfigurationError, TransformerException {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		DOMSource domSource = new DOMSource(doc);
		StreamResult sr = new StreamResult(targetJMXFile);
		tf.transform(domSource, sr);
	}
}
