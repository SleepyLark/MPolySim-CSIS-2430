package mpoly.model.games;

import mpoly.controller.MPolyController;
import mpoly.model.cards.*;
import mpoly.model.dealers.*;
import mpoly.model.players.MonopolyBot;

/**
 * The main simulation. Contains all functions need to play a game of Monopoly including keeping track of player movements and drawing cards
 * @author Skyler
 *
 */
public class MonopolyTest extends GameMaster
{
	/**
	 * ==[INDICES FOR BOARD SPACES]==
	00 Go
	01 Mediterranean Avenue
	02 Community Chest
	03 Baltic Avenue
	04 Income Tax
	05 Reading Railroad
	06 Oriental Avenue
	07 Chance
	08 Vermont Avenue
	09 Connecticut Avenue
	10 Jail / Just Visiting
	11 St. Charles Place
	12 Electric Company
	13 States Avenue
	14 Virginia Avenue
	15 Pennsylvania Railroad
	16 St. James Place
	17 Community Chest
	18 Tennessee Avenue
	19 New York Avenue
	20 Free Parking
	21 Kentucky Avenue
	22 Chance
	23 Indiana Avenue
	24 Illinois Avenue
	25 B. & O. Railroad
	26 Atlantic Avenue
	27 Ventnor Avenue
	28 Water Works
	39 Marvin Gardens
	30 Go To Jail
	31 Pacific Avenue
	32 North Carolina Avenue
	33 Community Chest
	34 Pennsylvania Avenue
	35 Short Line
	36 Chance
	37 Park Place
	38 Luxury Tax
	39 Boardwalk
	 */
	private MPolyController app; //reference to main controller
	private MonopolyBot bot; //player object
	private ChanceDeck chanceDeck;
	private ChestDeck chestDeck;
	private int doubleCounter; //how many doubles have been rolled
	private int[] gameBoard;
	private int playerPos; //player's current position
	private int jailCount; //how many times the player has been jailed [DEBUG]
	private int jailedFromCard; //how many times the player drew "go to jail" [DEBUG]
	private int jailedFromSpace; //how many times the player landed on "go to jail" [DEBUG]
	private int jailedFromSpeed; //how many times the player rolled too many doubles [DEBUG]
	private boolean outOfJail; //status on if the player is currently out of jail or not
	private boolean stratB; //switch to use strategy b
	private int jailBond; //how much money has been paid to leave jail [DEBUG]
	private int[][] snapshot; //copies the value of current test results while the test is still running
	private String[] spaces = 
		{
			"Go",
			"Mediterranean Avenue",
			"Community Chest",
			"Baltic Avenue",
			"Income Tax",
			"Reading Railroad",
			"Oriental Avenue",
			"Chance",
			"Vermont Avenue",
			"Connecticut Avenue",
			"Jail / Just Visiting",
			"St. Charles Place",
			"Electric Company",
			"States Avenue",
			"Virginia Avenue",
			"Pennsylvania Railroad",
			"St. James Place",
			"Community Chest",
			"Tennessee Avenue",
			"New York Avenue",
			"Free Parking",
			"Kentucky Avenue",
			"Chance",
			"Indiana Avenue",
			"Illinois Avenue",
			"B. & O. Railroad",
			"Atlantic Avenue",
			"Ventnor Avenue",
			"Water Works",
			"Marvin Gardens",
			"Go To Jail",
			"Pacific Avenue",
			"North Carolina Avenue",
			"Community Chest",
			"Pennsylvania Avenue",
			"Short Line",
			"Chance",
			"Park Place",
			"Luxury Tax",
			"Boardwalk"
		};

	public MonopolyTest(MPolyController app)
	{
		this.app = app;
		bot = new MonopolyBot();
		chanceDeck = new ChanceDeck(app);
		chestDeck = new ChestDeck(app);
		doubleCounter = 0;
		jailCount = 0;
		jailedFromCard = 0;
		jailedFromSpace = 0;
		jailedFromSpeed =0;
		gameBoard = new int[40]; //monopoly board has 40 spaces
		playerPos = 0;
		jailBond = 0;
		snapshot = new int[4][40];
		outOfJail = true;
		stratB = false;
		setupGame();
	}

	@Override
	public void startGame()
	{
		app.out("Starting simulation....");
		int counter = 0; //keep track of current test
		String out = "";
		String outCSV = "";
		//run 10 simulations for strategy A
		app.out("Strategy A:");
		while(counter < 10)
		{
			counter++;
			runSim(false);
			String[] testResults = printBoard(snapshot);
			out +="Test # "+counter+"\n" + testResults[0] +"\n";
			outCSV +="Test # "+counter+"\n" + testResults[1] +"\n";
			app.out("Finished test "+counter);
			
		}
		//export test results
		app.saveString(out,"stratA.txt");
		app.saveString(outCSV,"stratA.csv");
		
		//reset the test for strategy B
		counter = 0;
		out = "";
		outCSV = "";
		//run 10 simulation for strategy B
		app.out("Strategy B:");
		while(counter < 10)
		{
			counter++;
			runSim(true);
			String[] testResults = printBoard(snapshot);
			out +="Test # "+counter+"\n" + testResults[0] +"\n";
			outCSV +="Test # "+counter+"\n" + testResults[1] +"\n";
			app.out("Finished test "+counter);
		}
		//export test results
		app.saveString(out,"stratB.txt");
		app.saveString(outCSV,"stratB.csv");
		
		app.out("Simulation finished. Check the root of the project folder to see the results.");
		
		//testDebug();


	}

	/**
	 * Add player to game and shuffle decks
	 */
	@Override
	protected void setupGame()
	{
		this.addToGame(bot);
		chanceDeck.buildDeck();
		chestDeck.buildDeck();
		
		chanceDeck.shuffleCards();
		chestDeck.shuffleCards();
	}
	
	/**
	 * The function that runs the simulation 1,000,000 times
	 * @param useStratB whether or not to use strategy b
	 */
	private void runSim(boolean useStratB)
	{
		//restart the game and clears all counters
		this.restart();
		
		stratB = useStratB;
		
		//play 1,000 turns
		while(this.turnCount < 1000)
		{
			play();
		}
		//stop and take a snapshot of current statistics
		snapshot[0] = gameBoard.clone();

		//resume playing until 10,000 and repeat the process until 1,000,000
		while(this.turnCount < 10000)
		{
			play();
		}
		snapshot[1]=gameBoard.clone();

		while(this.turnCount < 100000)
		{
			play();
		}
		snapshot[2]=gameBoard.clone();

		while(this.turnCount < 1000000)
		{
			play();
		}
		snapshot[3]=gameBoard.clone();
	}

	/**
	 * clears all counter and resets other values so it's ready to play a game
	 */
	private void restart()
	{
		this.reset();
		chanceDeck.destroyDeck();
		chanceDeck.buildDeck();
		chanceDeck.shuffleCards();
		
		chestDeck.destroyDeck();
		chestDeck.buildDeck();
		chestDeck.shuffleCards();
		
		bot.destroyHand();
		
		doubleCounter = 0;
		jailCount = 0;
		jailedFromCard = 0;
		jailedFromSpace = 0;
		jailedFromSpeed =0;
		
		//clears arrays with 0s to prevent data leaks
		for(int i = 0; i < gameBoard.length; i++)
			gameBoard[i] = 0;
		
		for(int row = 0; row < snapshot.length; row++)
			for(int col = 0; col < snapshot[0].length; col++)
				snapshot[row][col] = 0;
		
		playerPos = 0;
		jailBond = 0;
		
		outOfJail = true;
		stratB = false;

	}
	
	/**
	 * Plays one turn of monopoly.
	 */
	private void play()
	{
		int diceRoll = rollDice();
		
		//if 3 consecutive doubles are rolled, go to jail
		if(doubleCounter >= 3)
		{
			goToJail();	
			jailedFromSpeed++;
		}
		
		//if the player is still in jail...
		if(!outOfJail)
		{
			diceRoll = EscapeJail(stratB);
		}
		
		playerPos = (diceRoll + playerPos) % 40;
		gameBoard[playerPos]++; //update what space the player landed
		switch(playerPos)
		{
		//community chest spaces
		case 2:
		case 17:
		case 33:
			drawFromChest(chestDeck.draw());
			break;
		//Chance deck spaces
		case 7:
		case 22:
		case 36:
			drawFromChance(chanceDeck.draw());
			break;
		//Go to jail space
		case 30:
			goToJail();
			jailedFromSpace++;
			break;
		}
		//End turn if the player didn't roll doubles and is not in jail
		if(doubleCounter == 0 && outOfJail)
		{
			this.next();
		}
	}

	/**
	 * picks two random numbers between 1 and 6 and returns the sum
	 * @return
	 */
	private int rollDice()
	{
		int die1 = Dealer.randomInt(1,6);
		int die2 = Dealer.randomInt(1,6);
		
		//if the two numbers are equal than increase counter
		if(die1 == die2)
			doubleCounter++;
		else
			doubleCounter = 0;//if they're not, then break the chain and reset to 0

		return die1+die2;
	}
	/**
	 * checks the drawn card from the community chest and discards it if needed
	 * @param nextCard the card that was drawn from the pile that needs to be checked
	 */
	private void drawFromChest(Card nextCard)
	{
		//check to see if the card is usable
		boolean addedToHand = drawFromDeck(nextCard);
		
		//Since most of the cards in this deck don't need to be implemented
		//we'll just discard them back to the appropriate pile if not needed
		if(!addedToHand)
		{
			chestDeck.discard(nextCard);
		}
		//if the draw pile is empty, then reshuffle the cards back
		if(chestDeck.isDrawDeckEmpty())
		{
			chestDeck.reshuffleDiscardPile();
		}
	}
	/**
	 * checks the drawn card from the chance deck and discards it if needed
	 * @param nextCard the card that was drawn from the pile that needs to be checked
	 */
	private void drawFromChance(Card nextCard)
	{
		//check to see if the card is usable
		boolean addedToHand = drawFromDeck(nextCard);
		
		//the chance deck has more cards that effect player's position so they need to be checked
		//based off of their assigned value. 1, 11, and 12 are common values and don't need to be checked
		int cardValue = nextCard.getNum();
		if(cardValue >= 2 && cardValue <= 9)
		{
			switch(cardValue)
			{
			case ChanceCard.GO_BACK:
				//go back three spaces. luckily since the closest chance space in on index 7
				//we don't need to worry about getting a negative number
				playerPos = playerPos - 3;
				break;

			case ChanceCard.TO_BOARDWALK:
				playerPos = 39;
				break;

			case ChanceCard.TO_ILLINOIS:
				playerPos = 24;
				break;

			case ChanceCard.TO_CHARLES:
				playerPos = 11;
				break;

			case ChanceCard.TO_RAILROAD_1:
			case ChanceCard.TO_RAILROAD_2:
				goToNearestRailroad(); //determine where the nearest railroad is
				break;

			case ChanceCard.TO_READING:
				playerPos = 5;
				break;

			case ChanceCard.TO_UTILITY:
				goToNearestUtility(); //determine where the nearest utility space is
				break;
			}

			gameBoard[playerPos]++; //update player position based on new space
		}

		//the card wasn't a get out of jail free card then discard it and reshuffle the deck if needed
		if(!addedToHand)
		{
			chanceDeck.discard(nextCard);
		}
		
		if(chanceDeck.isDrawDeckEmpty())
		{
			chanceDeck.reshuffleDiscardPile();
		}
	}
	/**
	 * Outcomes that are common for both types of cards
	 * @param nextCard the card that was drawn
	 * @return whether or not the card was added to the player's hand (i.e. it was a get out of jail free card)
	 */
	private boolean drawFromDeck(Card nextCard)
	{
		boolean addedToHand = nextCard.isJailBreak();
		
		//add card to player's hand if it was a get out of jail free card
		if(nextCard.isJailBreak())
			bot.addToHand(nextCard);
		else if(nextCard.isJail())
		{
			//go to jail if the card says so
			goToJail();
			jailedFromCard++;
		}
		else if(nextCard.isGo())
		{
			//move the player to go 
			playerPos = 0;
			gameBoard[playerPos]++;
		}

		return addedToHand;
	}

	/**
	 * Moves player to jail and determines if they can use the get out of jail free card
	 */
	private void goToJail()
	{
		doubleCounter = 0; //reset double counter
		playerPos = 10; //move player to jail
		gameBoard[playerPos]++;
		outOfJail = false; //player is now in jail
		jailCount++;
		
		//end your turn
		this.next();

		//assume that if player has any cards then it must be a get out of jail free card
		if(bot.getHandSize() > 0)
		{
			//discard card back to its appropriate deck
			Card discard = bot.discardCard(0);
			if(discard.getType() == Card.IS_COMMUNITY)
				chestDeck.discard(discard);
			else if(discard.getType() == Card.IS_CHANCE)
				chanceDeck.discard(discard);
			
			outOfJail = true; //player is now free
		}
	}
	
	/**
	 * Determines how the player escapes jail and how many turns have passed
	 * @param stratB whether to use strategy B or not
	 * @return The final dice roll after escaping jail
	 */
	private int EscapeJail(boolean stratB)
	{
		int diceRoll = 0;
		//if strategy B is on...
		if(stratB)
		{
			
			//player gets 3 tries to roll doubles and escape.
			int attempts = 0;
			
			while(attempts < 2)
			{
				diceRoll = rollDice();
				if(doubleCounter == 1)
				{
					/**
					 *  if you succeed in doing this you immediately move forward
					the number of spaces shown by your doubles throw; even though you
					had thrown doubles, you do not take another turn;
					 */
					outOfJail = true;
					break;
				}
				attempts++;
				this.next(); //skip a turn
			}
			//if after 2 tries your still not out of jail...
			if(!outOfJail)
			{
				//roll the dice
				diceRoll = rollDice();

				if(doubleCounter == 0)
				{
					jailBond -= 50;
				}
				else
					doubleCounter = 0;
			}

		}
		else
		{
			//if strategy A is used, than you immediately pay the fee and continue your turn
			jailBond -= 50;
			diceRoll = rollDice();
			
		}
		outOfJail = true;
		
		return diceRoll;
	}
	/**
	 * Calculates where the nearest railroad is based off of where they drew the card
	 */
	private void goToNearestRailroad()
	{
		//assumption is you can only go to the nearest railroad if you drew a chance card
		//chance spaces are only on 7, 22, and 36
		switch(playerPos)
		{
		case 7:
			playerPos = 15;
			break;
		case 22:
			playerPos = 25;
			break;
		case 36:
			playerPos = 5;
			break;
		}
	}

	/**
	 * Calculates where the nearest utility is based off of where they drew the card
	 */
	private void goToNearestUtility()
	{
		//assumption is you can only go to the nearest utility if you drew a chance card
		//chance spaces are only on 7, 22, and 36, and there's only 2 utilities
		switch(playerPos)
		{
		case 22:
			playerPos = 28;
			break;
		case 7:
		case 36:
			playerPos = 12;
			break;
		}
	}

	/**
	 * Turn data into a readable string for saving
	 * @param sample the dataset that needs to be printed
	 * @return an array of two values: 0 = console readable table, 1 = comma seperated values for spreadsheets
	 */
	private String[] printBoard(int[][] sample)
	{
		String[] out = new String[2];
		
		//fancy formatting strings that need to be repeated
		String col1 = "-----------------------";
		String col2 = "----------+----------";
		String col3 = "---------------------";
		String border = String.format("+%s+%s+%s+%s+%s+\n",col1,col2,col2,col2,col2);
		String subCol = String.format(" %8s | %8s ","Count","%");
		String header = border+String.format("| %-21s |%s|%s|%s|%s|\n","Space",subCol,subCol,subCol,subCol)+border;
		String print = border+String.format("| %-21s |  %-19s|  %-19s|  %-19s|  %-19s|\n","","n = 1,000","n = 10,000","n = 100,000","n = 1,000,000");
		
		
		String headerCSV = "Space,Count,%,Count,%,Count,%,Count,%\n";
		String printCSV = headerCSV+",n = 1000,,n = 10000,,n = 100000,,n = 1000000,,\n";
		
		//the assumption is there's only four stages of the test that needs to be saved,
		//so we need to keep track of the total for each stage to calculate the percentage
		int[] total = new int[4];
		
		print += header;
		
		//get total of all times the player landed on a space
		for(int test = 0; test < sample.length; test++)
		{
			for(int i = 0; i < sample[0].length; i++)
			{
				total[test] += sample[test][i];
			}
			
		}
		
		//calculate the percentage value of each space and print out the row with all values from all four stages
		for(int i = 0; i < sample[0].length; i++)
		{
			print += String.format("| %-21s | %8s | %8.4f | %8s | %8.4f | %8s | %8.4f | %8s | %8.4f |\n",
					spaces[i], sample[0][i],((float)sample[0][i]/total[0]) * 100, sample[1][i],((float)sample[1][i]/total[1]) * 100 ,sample[2][i],((float)sample[2][i]/total[2]) * 100 ,sample[3][i],((float)sample[3][i]/total[3]) * 100);
			printCSV += String.format("%s,%s,%.4f,%s,%.4f,%s,%.4f,%s,%.4f,\n",
					spaces[i], sample[0][i],((float)sample[0][i]/total[0]) * 100, sample[1][i],((float)sample[1][i]/total[1]) * 100 ,sample[2][i],((float)sample[2][i]/total[2]) * 100 ,sample[3][i],((float)sample[3][i]/total[3]) * 100);
		}
		
		
		print += border;
		
		out[0] = print;
		out[1] = printCSV;

		return out;

	}
	/**
	 * [DEBUG]
	 * A hacky way to see what's going on and view statistics. Place it anywhere as a breakpoint to see what changed
	 * or change what's inside to match what you're doing
	 */
	private void testDebug()
	{
		app.out("you were jailed "+jailCount+" times");
		app.out("drew the jail card "+jailedFromCard+" times");
		app.out("landed on 'Go To Jail' "+jailedFromSpace+" times");
		app.out("caught speeding "+jailedFromSpeed+" times");
		app.out("paid "+jailBond+" in jail money");
		
//		app.out("Chance Deck:");
//		chanceDeck.drawDebug();
//		chanceDeck.discardDebug();
//		
//		app.out("Chest Deck:");
//		chestDeck.drawDebug();
//		chestDeck.discardDebug();
	
	}

}
