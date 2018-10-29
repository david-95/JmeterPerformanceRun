package worker;

import java.io.File;
import java.util.HashMap;
	
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class QuickSettingReader {

	File quickSettingFile =null;
	public QuickSettingReader(String quickSettingFilepath) {
		try {
			quickSettingFile = new File(quickSettingFilepath);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public HashMap<String, String> read() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
