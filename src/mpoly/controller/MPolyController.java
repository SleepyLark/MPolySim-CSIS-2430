package mpoly.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import mpoly.model.games.*;
public class MPolyController
{

	private MonopolyTest game;
	

	public MPolyController()
	{
	
		game = new MonopolyTest(this);
	}

	public void start()
	{
		game.startGame();
	}
	
	/**
	 * A quick way to print to the console.  Useful for quick debugging
	 * @param message The thing you want to send to the console.
	 */
	public void out(Object message)
	{
		System.out.println(message);
	}
	
	public void saveString(String textToSave, String filename)
	{
		try
		{
			String path = System.getProperty("user.dir");
			File temp = new File(path + "//"+filename+ ".txt");
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
