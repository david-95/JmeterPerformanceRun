package process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import worker.QuickSettingReader;
import worker.RunningCommandsGenerator;
import worker.TasksBatchRunner;
import worker.TasksConfigReader;

/**
 * 
 * @author David An
 * 
 * This tool is used to generate running jmx and set running properties to jmx and generate a batch file and run according
 * to customer's requirement
 * 
 * This class is main port for jmx setting and 
 *
 */
public class BatchRunner {
	private static Logger logger = LogManager.getLogger("mylog");

	
	public static void main(String[] args) {
		if(args.length<1 || args.length>2) {
			logger.info("Usage: java -jar jmeterPerfRunner.jar config.xml");
			return;
			
		}
		if(args[0]==null || args[0].trim().equals("")) {
			logger.info("Usage: java -jar jmeterPerfRunner.jar config.xml");
			return;	
		}
		File confFile=new File(args[0].trim());
		if(!confFile.exists() || confFile.length()<1) {
			logger.info("Config File not exist or crashed!");
			return;
		}
		String tasksConfigPath = args[0];
		RunningCommandsGenerator commandsGenerator = new RunningCommandsGenerator(tasksConfigPath);
		ArrayList<String> commandsList=null;
		String batchFilePath = commandsGenerator.generateBatchFile();
		logger.info("Gennerate batch file: "+batchFilePath);
		TasksBatchRunner taskBatchRunner = new TasksBatchRunner(batchFilePath);
		taskBatchRunner.genCommandsBatchAndRun();
	}

	/**
	 * TODO, should build up relationship between QuickSetting Items and JMX properties
	 * @param quickSettingFilepath
	 */
	public void genJmxFromQuickSetting(String quickSettingFilepath) {
		QuickSettingReader reader = new QuickSettingReader(quickSettingFilepath);
		HashMap<String,String> settings=reader.read();
		
			
		
	}
}
