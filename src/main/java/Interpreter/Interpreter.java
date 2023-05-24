package Interpreter;
import memory.Memory;
import mutexes.Mutex;
import storage.*;
public class Interpreter {
	private Interpreter instance; 
	private Memory memory;
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
	
	public static void read(String filePath) {
		
	}
	

	

}