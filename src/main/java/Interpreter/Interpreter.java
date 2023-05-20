package Interpreter;
import memory.Memory;
import storage.*;
public class Interpreter {
	private Interpreter instance; 
	private Memory memory;
	private Interpreter() {
		instance = new Interpreter();
		Memory memeory = new Memory();
		Mutex metux = new Mutex();
	}

	public Interpreter getInstance() {
		if (instance == null) {
			instance = new Interpreter();
		}
		return instance;
	}
	
	public static void read(String filePath) {
		
	}
	

	

}
