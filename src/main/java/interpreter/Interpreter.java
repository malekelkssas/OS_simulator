package interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import exceptions.OSSimulatoeException;
import memory.Memory;
import mutexes.Mutex;
import storage.*;
import systemcalls.*;
import storage.Process;
import mutexes.Resource;

public class Interpreter {
	private Interpreter instance;
	private Memory memory;
	private static int processid;

	private Interpreter() {
		instance = new Interpreter();
		Memory memeory = new Memory();
		Mutex metux = Mutex.getInstance();
	}

	public Interpreter getInstance() {
		if (instance == null) {
			instance = new Interpreter();
		}
		return instance;
	}

	public static Process executeInstruction(Process process) throws IOException, OSSimulatoeException {
		process = ReadMemory.readProcess(process.getID());
		System.out.println("Process pc in executeInstruction: " + process.getPC());
		System.out.println("executing process "+ process.getID());
		process.setState(State.EXECUTE);
		int pc = process.getPC();
		if (pc < process.getUnParsedLines().size()) {
			UnParsedLine instruction = process.getUnParsedLines().get(pc);
			parse(process, instruction);
		} else {
			process.setState(State.FINISH);
		}

		process.inccrPC();
		WriteMemory.updateProcess(process);
		return process;
	}

	public static void parse(Process process, UnParsedLine instruction) throws IOException, OSSimulatoeException {
		thePrints(instruction, process);
		if (instruction.getSplittedLine()[0].equals("print")) {
			printing(instruction.getSplittedLine()[1], process);
		} else if (instruction.getSplittedLine()[0].equals("assign")) {
			prepareassign(instruction, process);
		} else if (instruction.getSplittedLine()[0].equals("writeFile")) {
			writeToFile(instruction.getSplittedLine(), process);
		} else if (instruction.getSplittedLine()[0].equals("readFile")) {
			readFromFile(instruction.getSplittedLine()[1], process);
		} else if (instruction.getSplittedLine()[0].equals("printFromTo")) {
			//todo: here
			printrange(instruction.getSplittedLine(), process);
		} else if (instruction.getSplittedLine()[0].equals("semWait")) {
			semWait(instruction.getSplittedLine()[1], process);
		} else if (instruction.getSplittedLine()[0].equals("semSignal")) {
			semSignal(instruction.getSplittedLine()[1], process);
		}
	}

	private static void thePrints(UnParsedLine instruction, Process process) {
		System.out.println("Currently executing process: " + process.toString());
		System.out.println("Currently executing instruction: " + instruction);
	}

	private static void semWait(String line, Process process) {
		Mutex.getInstance().semWait(Resource.valueOf(line), process);
	}

	private static void semSignal(String line, Process process) {
		Mutex.getInstance().semSignal(Resource.valueOf(line), process);
	}

	private static void printing(String toPrint, Process process) {
		String var = (String) getVarible(toPrint, process);
		Print.print(var);
	}

	private static void writeToFile(String[] line, Process process) throws IOException {
		String fileName = (String) getVarible(line[1], process);
		String value = (String) getVarible(line[2], process);
		WriteFile.writeFile(value, fileName);
	}

	private static void readFromFile(String line, Process process) throws IOException {
		String fileName = (String) getVarible(line, process);
		ReadFile.readFile(fileName);
	}

	private static void printrange(String[] lines, Process process) {
		//TODO: make sure from the integer
		int valuea = Integer.parseInt((String) getVarible(lines[1], process));
		int valueb = Integer.parseInt((String) getVarible(lines[2], process));
		while (valuea <= valueb) {
			Print.print(valuea + "");
			valuea++;
		}
	}

	private static void prepareassign(UnParsedLine instruction, Process process)
			throws IOException, OSSimulatoeException {
		Object valueb = null;
		String operation = instruction.getSplittedLine()[2];
		if (operation.equals("input")) {
			valueb = Input.readInput();
		} else if (operation.equals("readFile")) {
			String fileName = (String) getVarible(instruction.getSplittedLine()[3], process);
			valueb = ReadFile.readFile(fileName);
		} else {
			valueb = getVarible(operation, process);
			if (valueb == null) {
				valueb = operation;
			}
		}
		String valuea = instruction.getSplittedLine()[1];
		assign(valuea, valueb, process);
		WriteMemory.updateProcess(process);
	}

	private static Object getVarible(String name, Process process) {
		for (Variable var : process.getVariables()) {
			if (var.getName().equals(name)) {
				return var.getValue();
			}
		}
		return null;
	}

	private static void assign(String valuea, Object valueb, Process process) {
		for (Variable var : process.getVariables()) {
			if (var.getName().equals(valuea)) {
				var.setValue(valueb);
			}
		}
	}

	public Memory getMemory() {
		return memory;
	}

	public static Process getProcessReady(ArrayList<String> lines, int arrivalTime) throws OSSimulatoeException {
		Vector<UnParsedLine> unParsedLines = new Vector<UnParsedLine>();
		Vector<Variable> variblesToAdd = new Vector<Variable>();
		HashSet<String> varibles = new HashSet<String>();
		for (String line : lines) {
			unParsedLines.add(new UnParsedLine(line));
			varibles.addAll(checkVarible(line));
		}
		for (String var : varibles) {
			variblesToAdd.add(new Variable(var, null));
		}
		Process process = new Process(++processid, unParsedLines, variblesToAdd, arrivalTime);
		WriteMemory.writeProcess(process);
		return process;
	}

	private static HashSet<String> checkVarible(String line) {
		HashSet<String> varibles = new HashSet<String>();
		String[] splitedLines = line.split(" ");
		for (int i = 0; i < splitedLines.length; i++) {
			if (!splitedLines[i].equals("print") || !splitedLines[i].equals("assign")
					|| !splitedLines[i].equals("writeFile") || !splitedLines[i].equals("readFile")
					|| !splitedLines[i].equals("semWait") || !splitedLines[i].equals("semSignal")
					|| splitedLines[i].equals("input") || !splitedLines[i].equals("userOutput")
					|| !splitedLines[i].equals("userInput") || !splitedLines[i].equals("file")) {
				if (splitedLines[i].matches(".")) {
					varibles.add(splitedLines[i]);
				}
			}
		}
		return varibles;
	}

}