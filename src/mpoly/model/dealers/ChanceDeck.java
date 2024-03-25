package mpoly.model.dealers;

import mpoly.controller.MPolyController;
import mpoly.model.cards.ChanceCard;

public class ChanceDeck extends Dealer
{
	public ChanceDeck(MPolyController app)
	{
		super(app);
		buildDeck();
	}
	
	public void buildDeck()
	{
		for (int number = 1; number <= 16; number++)
		{
			drawDeck.add(new ChanceCard(number));
		}
	}
}
