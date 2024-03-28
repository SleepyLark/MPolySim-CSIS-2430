package mpoly.model.games;



import mpoly.controller.MPolyController;
import mpoly.model.cards.*;
import mpoly.model.dealers.*;
import mpoly.model.players.MonopolyBot;

public class MonopolyTest extends GameMaster
{
	/**
	 * 
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
	private MPolyController app;
	private MonopolyBot bot;
	private ChanceDeck chanceDeck;
	private ChestDeck chestDeck;
	private int doubleCounter;
	private int[] gameBoard;
	private int playerPos;
	private int jailCount;
	private int jailedFromCard;
	private int jailedFromSpace;
	private int jailedFromSpeed;
	private boolean outOfJail;
	private boolean stratB;
	private int jailBond;
	private int[][] snapshot;
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
	}

	@Override
	public void startGame()
	{
		while(this.turnCount < 1000)
		{
			play();
		}
		snapshot[0] = gameBoard.clone();

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

		app.out(this.printBoard(snapshot));
		
		
		//testDebug();


	}

	@Override
	protected void setupGame()
	{
		this.addToGame(bot);
		chanceDeck.shuffleCards();
		chestDeck.shuffleCards();


	}

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

	}

	private void play()
	{
		int diceRoll = rollDice();

		if(doubleCounter >= 3)
		{
			goToJail();	
			jailedFromSpeed++;
		}
		
		if(!outOfJail)
		{
			if(stratB)
			{
				int attempts = 0;
				//skip two turns. On third must pay fee and move on if no doubles
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
					this.next();
				}

				if(!outOfJail)
				{
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
				jailBond -= 50;
			}
			
			outOfJail = true;
		}

		playerPos = (diceRoll + playerPos) % 40;
		gameBoard[playerPos]++;
		switch(playerPos)
		{
		case 2:
		case 17:
		case 33:
			drawFromChest(chestDeck.draw());
			break;
		case 7:
		case 22:
		case 36:
			drawFromChance(chanceDeck.draw());
			break;
		case 30:
			goToJail();
			jailedFromSpace++;
			break;
		}

		if(doubleCounter == 0)
		{
			this.next();
		}
	}


	private int rollDice()
	{
		int die1 = Dealer.randomInt(1,6);
		int die2 = Dealer.randomInt(1,6);
		if(die1 == die2)
		{
			doubleCounter++;
		}
		else
			doubleCounter = 0;

		return die1+die2;
	}

	private void drawFromChest(Card nextCard)
	{
		boolean addedToHand = drawFromDeck(nextCard);
		if(!addedToHand)
		{
			chestDeck.discard(nextCard);
		}
		if(chestDeck.isDrawDeckEmpty())
		{
			chestDeck.reshuffleDiscardPile();
		}
	}

	private void drawFromChance(Card nextCard)
	{
		boolean addedToHand = drawFromDeck(nextCard);
		int cardValue = nextCard.getNum();
		if(cardValue >= 2 && cardValue <= 9)
		{
			switch(cardValue)
			{
			case ChanceCard.GO_BACK:
				if(playerPos - 3 < 0)
				{
					playerPos = (playerPos - 3) + playerPos; 
				}
				else
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
				goToNearestRailroad();
				break;

			case ChanceCard.TO_READING:
				playerPos = 5;
				break;

			case ChanceCard.TO_UTILITY:
				goToNearestUtility();
				break;
			}

			gameBoard[playerPos]++;
		}


		if(!addedToHand)
		{
			chanceDeck.discard(nextCard);
		}
		
		if(chanceDeck.isDrawDeckEmpty())
		{
			chanceDeck.reshuffleDiscardPile();
		}
	}
	private boolean drawFromDeck(Card nextCard)
	{
		boolean addedToHand = nextCard.isJailBreak();
		if(nextCard.isJailBreak())
			bot.addToHand(nextCard);
		else if(nextCard.isJail())
		{
			goToJail();
			jailedFromCard++;
		}
		else if(nextCard.isGo())
		{
			playerPos = 0;
			gameBoard[playerPos]++;
		}

		return addedToHand;
	}

	private void goToJail()
	{
		doubleCounter = 0;
		playerPos = 10; //move player to jail
		outOfJail = false;
		jailCount++;

		//assume that if player has any cards then it must be a get out of jail free card
		if(bot.getHandSize() > 0)
		{
			Card discard = bot.discardCard(0);
			if(discard.getType() == Card.IS_COMMUNITY)
				chestDeck.discard(discard);
			else if(discard.getType() == Card.IS_CHANCE)
				chanceDeck.discard(discard);
			
			outOfJail = true;
		}
	}

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

	private String printBoard(int[][] sample)
	{
		String col1 = "-----------------------";
		String col2 = "----------+----------";
		String col3 = "---------------------";
		String border = String.format("+%s+%s+%s+%s+%s+\n",col1,col2,col2,col2,col2);
		String subCol = String.format(" %8s | %8s ","Count","%");
		String header = border+String.format("| %-21s |%s|%s|%s|%s|\n","Space",subCol,subCol,subCol,subCol)+border;
		String print = border+String.format("| %-21s |  %-19s|  %-19s|  %-19s|  %-19s|\n","","n = 1,000","n = 10,000","n = 100,000","n = 1,000,000");
		int[] total = new int[4];
		//float[] percentage = new float[sample[0].length];
		
		print += header;
		
		for(int test = 0; test < sample.length; test++)
		{
			for(int i = 0; i < sample[0].length; i++)
			{
				total[test] += sample[test][i];
			}
			
		}
		
		for(int i = 0; i < sample[0].length; i++)
		{
			print += String.format("| %-21s | %8s | %8.2f | %8s | %8.2f | %8s | %8.2f | %8s | %8.2f |\n",spaces[i], sample[0][i],((float)sample[0][i]/total[0]) * 100, sample[1][i],((float)sample[1][i]/total[1]) * 100 ,sample[2][i],((float)sample[2][i]/total[2]) * 100 ,sample[3][i],((float)sample[3][i]/total[3]) * 100);
		}
		
		
		print += border;

		return print;

	}
	
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
