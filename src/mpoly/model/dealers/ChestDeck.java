package mpoly.model.dealers;

import mpoly.controller.MPolyController;
import mpoly.model.cards.CommunityChest;

public class ChestDeck extends Dealer
{
	public ChestDeck(MPolyController app)
	{
		super(app);
		buildDeck();
	}
	
	public void buildDeck()
	{
		for (int number = 1; number <= 16; number++)
		{
			drawDeck.add(new CommunityChest(number));
		}
	}
}
