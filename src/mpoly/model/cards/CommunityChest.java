package mpoly.model.cards;

public class CommunityChest implements Card
{ 
/**
 * 	TYPES OF CARDS:
 *
    **Advance to Go (Collect $200)**
    Bank error in your favor. Collect $200
    Doctor’s fee. Pay $50
    From sale of stock you get $50
    **Get Out of Jail Free**
    **Go to Jail. Go directly to jail, do not pass Go, do not collect $200**
    Holiday fund matures. Receive $100
    Income tax refund. Collect $20
    It is your birthday. Collect $10 from every player
    Life insurance matures. Collect $100
    Pay hospital fees of $100
    Pay school fees of $50
    Receive $25 consultancy fee
    You are assessed for street repair. $40 per house. $115 per hotel
    You have won second prize in a beauty contest. Collect $10
    You inherit $100
    
    TOTAL: 16
**/
    
	//Only 3 cards are useful for the test we'll be doing. the rest doesn't need to be tracked
    public static final int ADVANCE_TO_GO = 1;
    public static final int GET_OUT_OF_JAIL = 11;
    public static final int GO_TO_JAIL = 12;
    
    private int number;
    
    public CommunityChest(int number)
    {
    	this.number = number;
    }
    
    public int getNum()
    {
    	return this.number;
    }
    
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



}
