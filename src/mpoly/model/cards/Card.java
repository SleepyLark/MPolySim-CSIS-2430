package mpoly.model.cards;
/***
 * the abstract class acts like a "shell". Any objects that extends from this class
 * will have the following functions (aka these are common elements between the two decks)
 * @author Skyler
 *
 */
public abstract class Card
{
	//identifies what kind of card is it
	public static final int IS_COMMUNITY = 0;
	public static final int IS_CHANCE = 1;
	
	//Constants for readability; these three cards are the same across both decks
    public static final int ADVANCE_TO_GO = 1;
    public static final int GET_OUT_OF_JAIL = 11;
    public static final int GO_TO_JAIL = 12;
    
    //each card is given a unique number from 1-16
    protected int number;
    
    //Returns what kind of card it is (in this case it's either a chance card or from the community chest
    public abstract int getType();
    
    public int getNum()
    {
    	return this.number;
    }
    
    //The following functions are common elements in both decks
    public boolean isJail()
    {
    	return number == GO_TO_JAIL;
    }
    
    public boolean isJailBreak()
    {
    	return number == GET_OUT_OF_JAIL;
    }
    
    public boolean isGo()
    {
    	return number == ADVANCE_TO_GO;
    }
    
    public String toString()
    {
    	String type = number+"";
    	return type;
    }
}
