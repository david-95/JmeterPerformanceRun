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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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

	public void writeParasFromSuite(TestSuite suite,int suiteNum) throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException, TransformerException {


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
				XPath xPath = XPathFactory.newInstance().newXPath();
				Node node = (Node) xPath.compile(xpath).evaluate(doc, XPathConstants.NODE);
				node.setTextContent(value);
			}

		}

//		XPath xPath = XPathFactory.newInstance().newXPath();
//		Node num_threadNode = (Node) xPath.compile("//ThreadGroup/stringProp[@name='ThreadGroup.num_threads']")
//				.evaluate(doc, XPathConstants.NODE);
//		num_threadNode.setTextContent(value_num_thread);
//
//		xPath = XPathFactory.newInstance().newXPath();
//		Node ramp_timeNode = (Node) xPath.compile("//ThreadGroup/stringProp[@name='ThreadGroup.ramp_time']")
//				.evaluate(doc, XPathConstants.NODE);
//		ramp_timeNode.setTextContent(value_ramp_time);


		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		DOMSource domSource = new DOMSource(doc);
		StreamResult sr = new StreamResult(targetJMXFile);
		tf.transform(domSource, sr);
	}

}
