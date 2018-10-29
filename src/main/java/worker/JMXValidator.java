package worker;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class JMXValidator {
	private static Logger logger = LogManager.getLogger("mylog");
	
	private Document doc;

	public JMXValidator(Document doc) {
		this.doc = doc;
	}
	/**
	 * validate duration setting not conflict with loop counts, if set duration value , must set loops = -1
	 * in a JMX file, loops always high setting priority than duration.
	 * @param targetJMXFile 
	 * @param schedulerXpath
	 * @param durationXpath
	 * @param loopCountXpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public boolean validateThreadsDurationAndLoopsByXpath(String suiteName, String schedulerXpath,String durationXpath,String loopCountXpath) throws XPathExpressionException {
		String schedulerValue = getXmlValueByXpath(doc, schedulerXpath);
		String durationValue =  getXmlValueByXpath(doc, durationXpath);
		String loopCountValue = getXmlValueByXpath(doc,loopCountXpath);
		if(schedulerValue.trim().equals("true") && !durationValue.trim().equals("")&& !loopCountValue.trim().equals("-1")) {
			logger.warn(suiteName+"scheduler is true  and duration value is not null, but loops count is not -1 , Must set loops = -1 if you use duration setting");
			return false;
		}
		return true;
	}

	public boolean validateThreadsDurationAndLoopsByValue(String suiteName, String schedulerValue,String durationValue,String loopCountValue) {
		if(schedulerValue.trim().equals("true") && !durationValue.trim().equals("")&& !loopCountValue.trim().equals("-1")) {
			logger.warn(suiteName+"scheduler is true  and duration value is not null, but loops count is not -1 , Must set loops = -1 if you use duration setting");
			return false;
		}
		return true;
	}
	
	public String getXmlValueByXpath(Document doc,String xpath) throws XPathExpressionException {
		XPath XPathLocator = XPathFactory.newInstance().newXPath();
		Node node = (Node) XPathLocator.compile(xpath).evaluate(doc, XPathConstants.NODE);
		String value = node.getNodeValue();
		return value;
	}

}
