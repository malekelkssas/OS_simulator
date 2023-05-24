package systemcalls;

import java.util.Scanner;

public class Input {
	
	private Scanner sc;

	public static String readInput() {
		sc = new Scanner(System.in);
		return sc.nextLine();
	}
}
