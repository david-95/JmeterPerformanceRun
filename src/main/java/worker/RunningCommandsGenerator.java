package worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;

import org.xml.sax.SAXException;

import testbase.TestSuite;

public class RunningCommandsGenerator {
	private Logger logger = LogManager.getLogger("mylog");
	private String taskConfigPath = null;   
	TasksConfigReader tasksConfigReader = null;
	JMXWriter tasksConfigWriter = null;

	public RunningCommandsGenerator(String tasksConfigPath) {
		this.taskConfigPath = tasksConfigPath;
		try {
			this.tasksConfigReader = new TasksConfigReader(taskConfigPath);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String generateCommand(TestSuite aSuite) {

		String tmplateStr = aSuite.getTemplatePath();
		
		String outputFolder = aSuite.getOutputFolder();
		String jmxFolder=aSuite.getJmxFolder();
		String suiteName = aSuite.getSuiteName();
		String folderSeperator = File.separator.toString();
		String sample_result_path = outputFolder + folderSeperator + suiteName + ".csv";
		String sample_log = outputFolder + folderSeperator + suiteName + ".log";
		String stastic_output = outputFolder + folderSeperator + suiteName;
		String targetJMXPathStr = jmxFolder+folderSeperator+aSuite.getSuiteName() + ".jmx";
		// clear file
		this._createOutputFolder(jmxFolder);
		this._createOutputFolder(outputFolder);
		_removeFileIfExist(sample_result_path);
		_removeFileIfExist(sample_log);
		_removeFileIfExist(stastic_output);
		//
		String commandStr = "jmeter -n -t " + targetJMXPathStr + " -l " + sample_result_path + " -j " + sample_log
				+ " -e -o " + stastic_output;
		return commandStr;
	}

	public File generateRunningJMX(TestSuite aSuite, int suiteNum) throws IOException {
		// generate targeJmx file by copy from template
		String tmplateStr = aSuite.getTemplatePath();
		String jmxFolder=aSuite.getJmxFolder();
		String folderSeperator= File.separator.toString();
		String targetJMXPathStr = jmxFolder+folderSeperator+aSuite.getSuiteName() + ".jmx";
		this._createOutputFolder(jmxFolder);
		if (tmplateStr == null || tmplateStr.trim().equals("")) {
			throw new IOException("Suite don't have template jmx file path sett!");
		}
		File tmplateFile = new File(tmplateStr);
		File runingJmxFile = new File(targetJMXPathStr);
		_removeFileIfExist(targetJMXPathStr);

		logger.info("Copy file from " + tmplateStr + " to " + targetJMXPathStr);
		Files.copy(tmplateFile.toPath(), runingJmxFile.toPath());

		// write parameters target jmx file
		this.tasksConfigWriter = new JMXWriter(targetJMXPathStr);
		try {
			tasksConfigWriter.writeParasFromSuite(aSuite,suiteNum);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return runingJmxFile;
	}

	public ArrayList<String> generateCommands() throws IOException {
		ArrayList<TestSuite> suiteList = tasksConfigReader.getSuites();
		ArrayList<String> arr = new ArrayList<String>();
		int suiteNum=1;
		for (TestSuite aSuite : suiteList) {
			generateRunningJMX(aSuite,suiteNum);
			String tmpCommand = this.generateCommand(aSuite);
			logger.info("Generate Batch Command: " + tmpCommand);
			arr.add(tmpCommand);
			suiteNum++;
		}
		return arr;
	}

	public String generateBatchFile() {
		String batchName = tasksConfigReader.getSuitesGroupName() + ".bsh";
		this._removeFileIfExist(batchName);
		File batchFile = new File(batchName);
		FileOutputStream fopt = null;
		try {
			fopt = new FileOutputStream(batchFile);
			ArrayList<TestSuite> suiteList = tasksConfigReader.getSuites();
			int suiteNum=1;
			for (TestSuite aSuite : suiteList) {
				generateRunningJMX(aSuite,suiteNum);
				String commands = this.generateCommand(aSuite);
				fopt.write((commands+"\n").getBytes());
				fopt.write("sleep 60m\n".getBytes());
				fopt.flush();
				suiteNum++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				fopt.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return batchFile.getAbsolutePath();

	}

	private void _createOutputFolder(String folderPath) {
		File folder = new File(folderPath);

		try {
			if (!folder.exists()) {
				FileUtils.forceMkdir(folder);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void _removeFileIfExist(String filePath) {
		File file = new File(filePath);
		try {
			if (file.exists())
				FileUtils.forceDelete(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
