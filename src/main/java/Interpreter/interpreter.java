package Interpreter;

import java.io.IOException;

import memory.Memory;
import mutexes.Mutex;

import memory.Memory;
import storage.*;
import systemcalls.*;
import storage.Process;
import mutexes.Mutex;
import mutexes.Resource;

public class interpreter {
	private interpreter instance;
	private Memory memory;

	private interpreter() {
		instance = new interpreter();
		Memory memeory = new Memory();
		Mutex metux = Mutex.getInstance();
	}

	public interpreter getInstance() {
		if (instance == null) {
			instance = new interpreter();
		}
		return instance;
	}

	public static void read(Process process) throws IOException {
		int pc = process.getPC();
		process.inccrPC();
		UnParsedLine instruction = process.getUnParsedLines().get(pc);
		System.out.println("Currently executing process: " + process.toString());
		System.out.println("Currently executing instruction: " + instruction);
		if (instruction.getSplittedLine()[0].equals("print")) {
			printing(instruction.getSplittedLine()[1]);
		} else if (instruction.getSplittedLine()[0].equals("assign")) {
			prepareassign(instruction, process);
		} else if (instruction.getSplittedLine()[0].equals("writeFile")) {
			writeToFile(instruction.getSplittedLine(), process);
		} else if (instruction.getSplittedLine()[0].equals("readFile")) {
			readFromFile(instruction.getSplittedLine()[1], process);
		} else if (instruction.getSplittedLine()[0].equals("printFromTo")) {
			printrange(instruction.getSplittedLine(), process);
		} else if (instruction.getSplittedLine()[0].equals("semWait")) {
			String resource = (String) getVarible(instruction.getSplittedLine()[1], process);
			Mutex.getInstance().semWait(Resource.valueOf(resource), process);
		} else if (instruction.getSplittedLine()[0].equals("semSignal")) {
			String resource = (String) getVarible(instruction.getSplittedLine()[1], process);
			Mutex.getInstance().semSignal(Resource.valueOf(resource), process);
		}
	}

	private static void printing(String toPrint) {
		Print.print(toPrint);
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
		int valuea = (int) getVarible(lines[1], process);
		int valueb = (int) getVarible(lines[2], process);
		while (valuea <= valueb) {
			Print.print(valuea + "");
			valuea++;
		}
	}

	private static void prepareassign(UnParsedLine instruction, Process process) throws IOException {
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
		// WriteMemory.update(process); TODO update the process
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

}