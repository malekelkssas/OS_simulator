package systemcalls;

import java.io.*;
import java.util.*;

public class WriteFile {
	private static PrintWriter pw;

	public static void writeFile(String fileLines, String filePath) throws IOException {
		pw = new PrintWriter(new FileWriter(filePath));

		
		pw.println(fileLines);
		
		pw.flush();
		pw.close();
	}

}
