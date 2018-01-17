package testbase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Data
@Log4j
@NoArgsConstructor
@AllArgsConstructor
public class CommandRunResult {
	
	public String Command;
	public String ExecuteResult;
	public String RunningStartTime;
	public String RunningEndTime;
	public String ErrorLog;

}
