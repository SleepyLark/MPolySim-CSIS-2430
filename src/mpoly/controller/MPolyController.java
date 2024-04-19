package mpoly.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import mpoly.model.games.*;

/**
 * The class that is in charge of staging and executing all other object in the program
 * @author Skyler
 *
 */
public class MPolyController
{
	
	private MonopolyTest game;
	

	public MPolyController()
	{
		//Create a new game object
		game = new MonopolyTest(this);
	}

	/**
	 * The function that is called in the "main" method. All important methods need to be called here
	 */
	public void start()
	{
		//Calls the game object to execute the simulation
		game.startGame();
	}
	
	/**
	 * A quick way to print to the console.  Useful for debugging and quick messages
	 * @param message The thing you want to send to the console.
	 */
	public void out(Object message)
	{
		System.out.println(message);
	}
	
	/**
	 * Take the string provided and write it to a new file
	 * @param textToSave What you want to save to a file
	 * @param filename name of file (MUST include extension name)
	 */
	public void saveString(String textToSave, String filename)
	{
		try
		{
			//Create a new file and write to it. if something files it will go to the "catch" block which has nothing inside it :P
			String path = System.getProperty("user.dir");
			File temp = new File(path + "//"+filename);
			Scanner reader = new Scanner(textToSave);
			PrintWriter output = new PrintWriter(temp);

			while (reader.hasNext())
			{
				output.println(reader.nextLine());
			}

			output.close();
			reader.close();

		}
		catch (IOException error)
		{
			
		}

	}
	
}
