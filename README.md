# JmeterPerformanceRun

## What is JmeterPerformanceRun
This project is a tool,to make performance test process more quickly. It assist user running performance test via jmeter, help to batch executing performance tests from a config file 

## Usecase story
John would like do perfermance test agains to a web b2b server. He dont' know how many online users the server can bear with.
so he expect to execute tests with 10 online users, 15 online users, 20 online users do login & browse & logout at same time every 5 mins, keep doing the manipulation in  10 mins , to see how the servers performance.  
In traditional way, He has to do one test,wait it end, then change test scripts' input parameters, then do next run, repeat the steps till all the tests are done. It a long and tedious work.  
But with this tool, what he done is just put all 3 suites of test setting in a xml configuration file. then type a command and run. All tests are executed one by one in sequence. He can run before out of office ,and see tests resulte tommorow. It is much convinient.  

## How to use it 
1. you record a jmx as template, 
2. edit configfile to add config values, such as threads , ramp_time, loop controller,csv dataset ..., almost every tunning configs can be set in this configure file.   
3. the program will scan the xml config, generate a jmx  instance for each suite, then using regular expression to scan and set values of config to jmx instances, jmx instances are stored to  tag 'jmxFolder' setting folder  
then the program generate a bash script , which contents is jmeter console commands 
4. you type to run bash scripts to execute all tests.  
5. the bash scripts will run all test cases one by one. export test static results to tag 'outputFolder' setting  


## How to run it 
First, you should prepare performance testcase        
you may use jmeter to record a workflow scenario , then just save as jmx. This jmx is save as template  

```
java -jar jmeterPerfRunner.jar config.xml 
```
then it run and generate an xxx.bsh file , the name is suites name. for example, if you use below config, it will generate a bash file , named as ProjectName-pc-h5-Run.bsh
It will prompt to you, if you choose directly run or choose manually run bash file. you better input "No" , then manually run bash file later.(don't ask why)


## Config files 
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
