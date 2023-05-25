package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import storage.Process;

public class CodeParser {
	private static BufferedReader br;

	public static ArrayList<String> readFile(String filePath) throws IOException {
		br = new BufferedReader(new FileReader(filePath));
		ArrayList<String> fileLines = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			fileLines.add(line);
		}
		br.close();
		return fileLines;
	}

	public static void main(String[] args) throws IOException {
		Process p1 = Interpreter.getProcessReady(readFile("Program_1.txt"));
		
	}

}