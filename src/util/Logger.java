package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Logger {
	
	// butchered - thought we could do something clever
	public void addToLog(String fileId, String content) {
		
		String filePath = fileId;
		Path path = Paths.get(filePath);
//		if(!Files.exists(path)) {
			new File(filePath);
//		}
		try {
			FileWriter fileWriter = new FileWriter(fileId, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);
			printWriter.println(content);
			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to Update Log File", e);
		}
		
		
	}
	
	// unused - thought we could do something clever
	public List<String> readFromLog(String fileId) {
		ArrayList<String> content = new ArrayList();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileId));
			String line = reader.readLine();
			while (line != null) {
				content.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to Read Log File", e);
		}
		return content;
	}
	
}
