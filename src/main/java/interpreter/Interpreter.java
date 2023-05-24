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
		System.out.println("Currently executing process: "+ process.toString());
		System.out.println("Currently executing instruction: "+ instruction);
		if (instruction.getSplittedLine()[0].equals("print")) {
			String toPrint = instruction.getSplittedLine()[1];
			Print.print(toPrint);
		} else if (instruction.getSplittedLine()[0].equals("assign")) {
			prepareassign(instruction, process);
		} else if (instruction.getSplittedLine()[0].equals("writeFile")) {
			String fileName = (String) getVarible(instruction.getSplittedLine()[1], process);
			String value = (String) getVarible(instruction.getSplittedLine()[2], process);
			WriteFile.writeFile(value, fileName);
		} else if (instruction.getSplittedLine()[0].equals("readFile")) {
			String fileName = (String) getVarible(instruction.getSplittedLine()[1], process);
			ReadFile.readFile(fileName);
		} else if (instruction.getSplittedLine()[0].equals("printFromTo")) {
			int valuea = (int) getVarible(instruction.getSplittedLine()[1], process);
			int valueb = (int) getVarible(instruction.getSplittedLine()[2], process);
			printrange(valuea, valueb);
		} else if (instruction.getSplittedLine()[0].equals("semWait")) {
			String resource = (String) getVarible(instruction.getSplittedLine()[1], process);
			Mutex.getInstance().semWait(Resource.valueOf(resource), process);
		} else if (instruction.getSplittedLine()[0].equals("semSignal")) {
			String resource = (String) getVarible(instruction.getSplittedLine()[1], process);
			Mutex.getInstance().semSignal(Resource.valueOf(resource), process);
		}
	}

	private static void printrange(int valuea, int valueb) {
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