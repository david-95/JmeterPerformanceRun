# JmeterPerformanceRun
## What is JmeterPerformanceRun
This project is a tool,to make performance test process more quickly. It assist user running performance test via jmeter, help to batch executing performance tests from a config file \n
you record a jmx as template, 
edit configfile to add config values, such as threads , ramp_time, loop controller,csv dataset ..., almost every tunning configs can be set in this configure file.
the program will scan the xml config, generate a jmx  instance for each suite, then using regular expression to scan and set values of config to jmx instances, jmx instances are stored to  tag 'jmxFolder' setting folder
then the program generate a bash script , which contents is jmeter console commands 
then you run bash scripts manually
the bash scripts will run all test cases one by one. export test static results to tag 'outputFolder' setting



## how to run it 
First, you should prepare performance testcase
you may use jmeter to record a workflow scenario , then just save as jmx. This jmx is save as template
Second, prepare a folder for bat

```
java -jar jmeterPerfRunner.jar config.xml 
```

## config files 
you must specify a xml config file , config contents as below:
```
<?xml version="1.0" encoding="UTF-8"?>
<suites name="ProjectName-pc-h5-Run">
	<!-- 1, -->
	<suite name="project_case_name_thread_duration_thinktime">
		<!--templatePath -->
		<templatePath>/%jmx_template_filepath%/B2B_h5_login_browse_logout.jmx</templatePath>
		
		<jmxFolder>/%filepath to store jmx instance% </jmxFolder>
		
		<!-- output of stastics files -->
		<outputFolder>/%filepath to store statics files% /outputs </outputFolder>

		<JmxPropertiesSetting>
			<!-- Customized setting of thread group -->
			<ThreadGroup.num_threads>1</ThreadGroup.num_threads>
			<ThreadGroup.ramp_time>1</ThreadGroup.ramp_time>

			<!-- for main loop controller -->
			<LoopController.continue_forever>false
			</LoopController.continue_forever>
			<LoopController.loops>1</LoopController.loops>

			<!-- for scheduler -->
			<ThreadGroup.scheduler>false</ThreadGroup.scheduler>
			<ThreadGroup.duration>100</ThreadGroup.duration>
			
			<CSVDataSet.filepath>/%filepath of csv dataset %/2000username.csv</CSVDataSet.filepath>
		</JmxPropertiesSetting>

	</suite>
  ...
  <suite>
  ...
  </suite>
</suites>
```
