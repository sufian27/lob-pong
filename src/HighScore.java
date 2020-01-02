import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HighScore {
	static String fileName = "../other-files/highscore.txt";
	static File myFile;
	public static int read()  { //method to read the highest score from the highscore file
		myFile = new File (fileName);
		int highX;
		int highFinal =0;
		try {
			Scanner fileRead = new Scanner (myFile);
			while (fileRead.hasNext()) {
				String line = fileRead.nextLine();
				System.out.println("Highscore: " + line); //for testing
				highX = Integer.parseInt(line);
				if (highX > highFinal) {
					highFinal = highX;
				}
			}
			fileRead.close();
		} catch (FileNotFoundException e) {
			
		}
		return highFinal;
	}

	public static void write(int s) { //method to append argument score into the highscore file
		myFile = new File (fileName);
		try {	
			FileWriter f2 = new FileWriter(myFile, true);
			PrintWriter out = new PrintWriter(f2);
			out.println(s);
			out.close();	
		} catch (IOException e) {
			System.out.println("File not found!");
		}
	}
}


