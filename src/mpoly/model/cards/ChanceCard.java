package mpoly.model.cards;

public class ChanceCard extends Card
{
/**
 * TYPES OF CARDS:
 * 
    ++Advance to Boardwalk
    ++Advance to Go (Collect $200)
    ++Advance to Illinois Avenue. If you pass Go, collect $200
    ++Advance to St. Charles Place. If you pass Go, collect $200
    ++Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled
    ++Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled
    ++Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.
    --Bank pays you dividend of $50
    **Get Out of Jail Free
    ==Go Back 3 Spaces
    **Go to Jail. Go directly to Jail, do not pass Go, do not collect $200
    --Make general repairs on all your property. For each house pay $25. For each hotel pay $100
    --Speeding fine $15
    ++Take a trip to Reading Railroad. If you pass Go, collect $200
    --You have been elected Chairman of the Board. Pay each player $50
    --Your building loan matures. Collect $150
    
    TOTAL: 16
    
    ++ = moves player
    -- = money handling, not needed
    ** = jail cards

 */
	
	//Constants for readability; We only care about the cards that let you move 
    public static final int TO_BOARDWALK = 2;
    public static final int TO_ILLINOIS = 3;
    public static final int TO_CHARLES = 4;
    public static final int TO_RAILROAD_1 = 5;
    public static final int TO_RAILROAD_2 = 6;
    public static final int TO_UTILITY = 7;
    public static final int GO_BACK = 8;
    public static final int TO_READING = 9;
    
    
    public ChanceCard(int number)
    {
    	super.number = number;
    }


	@Override
	public int getType()
	{
		return Card.IS_CHANCE;
	}

}
