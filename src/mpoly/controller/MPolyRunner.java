/**
 * This project uses Model-View-Controller (MVC) formatting
 * - The runner class is what actually starts the program
 * - The controller takes what elements are needed for the program to work
 * - All other objects are viewed as "models" that are used by the controller to get the project to work
 */
package mpoly.controller;

public class MPolyRunner
{
	public static void main(String[] args)
	{
		MPolyController app = new MPolyController();
		
		app.start();
	}
}
