package mpoly.model.dealers;

import java.util.ArrayList;

import mpoly.controller.MPolyController;
import mpoly.model.cards.Card;
import mpoly.model.players.Player;

/**
 * Source of the main deck of cards
 * @author Skyler
 *
 */
public abstract class Dealer
{
	protected ArrayList<Card> drawDeck;
	protected ArrayList<Card> discardPile;
	private MPolyController app;
	private Card lastCardDrawn;
	private Card lastCardDiscarded;

	public Dealer(MPolyController app)
	{
		this.app = app;

		drawDeck = new ArrayList<Card>();
		discardPile = new ArrayList<Card>();
		lastCardDrawn = null;
		lastCardDiscarded = null;

	}
	
	/**
	 * builds the deck of cards needed for the game;
	 */
	public abstract void buildDeck();
	
	/**
	 * Overwrite the stack with a new one (which should be the same as deleting each element, but probably not)
	 */
	public void destroyDeck()
	{
		drawDeck = new ArrayList<Card>();
	}
	
	/**
	 * get a random integer (faster than doing Math.random() and casting it).
	 * @param min the lowest possible value
	 * @param max the highest possible value
	 * @return a random integer
	 */
	public static int randomInt(int min, int max)
	{
		return (int) (Math.random() * max) + min;
	}
	
	/**
	 * "shuffles" the cards a random amount of times
	 */
	public void shuffleCards()
	{
		int shuffleAmount = randomInt(1,5);
		
		for(int times = 0; times < shuffleAmount; times++)
		{
			shuffle();
		}
	}
	
	/**
	 * puts the Discard pile back into the draw deck and shuffles it
	 */
	public void reshuffleDiscardPile()
	{
		while(!discardPile.isEmpty())
		{
			drawDeck.add(discardPile.remove(0));
		}
		shuffleCards();
	}
	
	/**
	 * "Shuffles" the deck <br><i>maybe do some more complex shuffling?</i>
	 */
	private void shuffle()
	{
		ArrayList<Card> temp = new ArrayList<Card>();

		while (!drawDeck.isEmpty())
		{
			temp.add(drawDeck.remove(randomInt(0, drawDeck.size())));
		}
		
		drawDeck = temp;
	}
	
	/**
	 * Gives each player a certain amount of cards, or until the draw deck is gone. 
	 * Useful for the beginning of a game
	 * @param players list of players in the game
	 * @param handSize how many cards each player should have
	 */
	public void dealCards(ArrayList<Player> players, int handSize)
	{
		for(int times = 0; times < handSize && !(drawDeck.isEmpty()); times++)
		{
			for(Player currentPlayer : players)
			{
				if(!drawDeck.isEmpty())
				currentPlayer.addToHand(this.draw());
			}
		}
	}
	
	public void dealCards(Player specificPlayer, int handSize)
	{
		for(int times = 0; times < handSize && !(drawDeck.isEmpty()); times++)
		{
				if(!drawDeck.isEmpty())
				specificPlayer.addToHand(this.draw());
		}
	}

	/**
	 * Removes a card from the draw deck
	 * @return the first card from the draw deck
	 */
	public Card draw()
	{
		lastCardDrawn = drawDeck.remove(0);
		return lastCardDrawn;
	}
	
	/**
	 * removes the first card in the draw deck and gives it to the player
	 * @param playerThatDraws
	 */
	public void draw(Player playerThatDraws)
	{
		playerThatDraws.addToHand(draw());
	}
	
	/**
	 * Removes a card from the discard pile
	 * @return Draws the first cards from the discard pile
	 */
	public Card drawFromDiscardPile()
	{
		lastCardDiscarded = discardPile.remove(0);
		return lastCardDiscarded;
	}
	
	
	/**
	 *  adds a card to the discard pile
	 * @param discard the card that goes in the discard pile
	 * @return the card that got discarded
	 */
	public Card discard(Card discard)
	{
		discardPile.add(0,discard);
		return discard;
	}
	
	//====[DECK INFO]====
	//Methods that provide information but doesn't necessarily alter it
	
	
	/**
	 * looks at the first card in the discard pile (NOTE: Does not removes the card)
	 * @return the last card added to the discard pile
	 */
	public Card discardPeek()
	{
		return discardPile.get(0);
	}
	
	/**
	 * looks at the first card in the draw deck (NOTE: Does not removes the card)
	 * @return the first card in the draw deck
	 */
	public Card drawPeek()
	{
		return drawDeck.get(0);
	}
	
	public boolean isDrawDeckEmpty()
	{
		return drawDeck.isEmpty();
	}
	
	public boolean isDiscardPileEmpty()
	{
		return discardPile.isEmpty();
	}
	
	/**
	 * 
	 * @return size of drawDeck
	 */
	public int deckSize()
	{
		return drawDeck.size();
	}
	
	/**
	 * 
	 * @return size of discardPile
	 */
	public int discardSize()
	{
		return discardPile.size();
	}
	
	//====[DEBUG]====
	//Shows current status
	
	private String readableList(ArrayList<Card> list, int limit)
	{
		String desc = "";
		for(int index = 0; index < limit; index ++)
		{
			desc += (index + 1) +":\t" + list.get(index) + "\n";
		}
		
		return desc;
	}
	public void drawDebug(int numberOfCardsToShow)
	{
		if(numberOfCardsToShow > deckSize() || numberOfCardsToShow < 0)
		{
			numberOfCardsToShow = deckSize();
		}
		app.out("====[DRAW DECK DEBUG]====");
		app.out("Last card drawn: " + lastCardDrawn);
		app.out("Cards still in deck: \n" + readableList(drawDeck,numberOfCardsToShow));
	}
	
	public void discardDebug(int numberOfCardsToShow)
	{
		if(numberOfCardsToShow > discardSize() || numberOfCardsToShow < 0)
		{
			numberOfCardsToShow = discardSize();
		}
		app.out("====[DISCARD PILE DEBUG]====");
		app.out("Last card discarded: " + lastCardDiscarded);
		app.out("Cards still in pile: \n" + readableList(discardPile,numberOfCardsToShow));
	}

}
