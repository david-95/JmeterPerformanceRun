# JmeterPerformanceRun
This project is help to batch executing performance tests from a config file , via jmeter
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
