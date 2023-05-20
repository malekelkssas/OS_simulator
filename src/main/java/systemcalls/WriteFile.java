package systemcalls;

import java.io.*;
import java.util.*;

public class WriteFile {
	private PrintWriter pw;

	public void writeFile(ArrayList<String> fileLines, String filePath) throws IOException {
		pw = new PrintWriter(new FileWriter(filePath));

		for (String line : fileLines) {
			pw.println(line);
		}
		pw.flush();
		pw.close();
	}

}
