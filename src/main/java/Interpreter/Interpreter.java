package Interpreter;
import storage.*;
public class Interpreter {
	private Interpreter instance; 
	private memory memory;
	private Interpreter() {
		instance = new Interpreter();
		memory meme = new memory();
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
