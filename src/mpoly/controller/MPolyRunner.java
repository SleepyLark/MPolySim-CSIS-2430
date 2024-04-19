/**
 * This project uses Model-View-Controller (MVC) formatting
 * - The runner class is what actually starts the program
 * - The controller takes what elements are needed for the program to work
 * - All other objects are viewed as "models" that are used by the controller to get the project to work
 */
package mpoly.controller;

/**
 * The class that actually runs the program. It makes and calls a controller object that handles the main program
 * @author Skyler
 *
 */
public class MPolyRunner
{
	public static void main(String[] args)
	{
		//make a new controller object and call it to start the program
		MPolyController app = new MPolyController();
		
		app.start();
	}
}
