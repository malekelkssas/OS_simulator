package systemcalls;

import java.io.*;
import java.util.*;

public class ReadFile {
	private static BufferedReader br;

	public static String readFile(String filePath) throws IOException {
		br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		br.close();
		return line;
	}
}
