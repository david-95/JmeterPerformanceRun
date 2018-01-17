package worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TasksBatchRunner {
	private Logger logger = LogManager.getLogger("mylog");
	ArrayList<String> commandsList = null;
	int SLEEP_TIME_BETWEEN_EACH_RUNNING = 10000; // 10 seconds
	private String batchPath;

	public TasksBatchRunner(ArrayList<String> commandsList) {
		this.commandsList = commandsList;
	}

	public TasksBatchRunner(String batchPath) {
		this.batchPath = batchPath;
	}

	public void runCommands() {

		for (String command : commandsList) {
			Process p;
			InputStreamReader inputStream = null;
			BufferedReader reader = null;
			logger.info("//////////////begin run command////////////////////");
			logger.info("Running Command: " + command);
			try {
				p = Runtime.getRuntime().exec(command);
				p.waitFor();

				inputStream = new InputStreamReader(p.getInputStream());
				reader = new BufferedReader(inputStream);
				reader.close();
				String line = "";
				while ((line = reader.readLine()) != null) {
					logger.info(line + "\n");
					System.out.println(line);
					System.out.flush();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				p = null;
				try {
					inputStream.close();
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// sleep 5 seconds for next cycle run
			try {
				logger.info("Try to sleep after one testrun finished");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void genCommandsBatchAndRun() {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("chmod 777 " + this.batchPath);
			p.waitFor();
			if (askUserIfCallingBatchDirectly()) {
				p = Runtime.getRuntime().exec("bash " + this.batchPath);
				p.waitFor();
			}
			return;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			p.destroy();
		}

	}

	private boolean askUserIfCallingBatchDirectly() {
		Scanner inputReader = new Scanner(System.in);
		System.out.println("Do you want to run batch now?  Y(y) will run jmeter performance test at once,\n N(n) will get batch file generated, need run batch mannually");
		String strInput = "N";
//		strInput=inputReader.nextLine().trim();
		if (strInput.toLowerCase().equals("y")) {
			return true;
		}
		return false;
	}

}
