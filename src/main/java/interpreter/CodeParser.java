package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.OSSimulatoeException;
import scheduler.Scheduler;
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

	public static void main(String[] args) throws IOException, OSSimulatoeException {
		Process p1 = Interpreter.getProcessReady(readFile("Program_1.txt"), 0);
		Process p2 = Interpreter.getProcessReady(readFile("Program_2.txt"), 1);
		Process p3 = Interpreter.getProcessReady(readFile("Program_3.txt"), 4);
		Scheduler.getInstance().addToArrivingProcesses(p1);
		Scheduler.getInstance().addToArrivingProcesses(p2);
		Scheduler.getInstance().addToArrivingProcesses(p3);
		Scheduler.getInstance().setTimeSlice(2);
		Scheduler.getInstance().controlProcesses();
	}

}
