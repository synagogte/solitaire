import java.util.*;

/**
 * The Solitaire class simulates the card game Solitaire. 
 * The Solitaire class can instantiate a Solitaire object and initialize its instance fields; 
 * can return the card on top of the stock, waste, or a foundation at a given index; 
 * can return a reference to a pile at a given index; can reset the stock or deal three cards when
 * the stock is clicked; can select or unselect the waste, foundations or piles; and can move cards 
 * around between the waste, foundations, and different piles.
 * 
 * @author Syna Gogte
 * @version  11/1/21
 */

public class Solitaire
{
	/**
	 *instantiating an object of class Solitaire.
	 * @param args 
	 */
	public static void main(String[] args)
	{
		new Solitaire();
	}

	private Stack<Card> stock;
	private Stack<Card> waste;
	private Stack<Card>[] foundations;
	private Stack<Card>[] piles;
	private SolitaireDisplay display;

	/** 
	 * Constructor for objects of the class Solitaire.
	 */
	public Solitaire()
	{
		foundations = new Stack[4];
        piles = new Stack[7];
		stock = new Stack<Card>();
		waste = new Stack<Card>();
		
		for (int i = 0; i < 4; i++)
        {
            foundations[i] = new Stack<Card>();
        }
        for (int i = 0; i < 7; i++)
        {
            piles[i] = new Stack<Card>();
        }
		createStock(); 
		deal();
		display = new SolitaireDisplay(this);
	}

	/**
	 * returns the card on top of the stock or null if the stock is empty
	 * @return the card on top of the stock 
	 */
	public Card getStockCard()
	{
		if (stock.isEmpty())
            return null;
		return stock.peek();
	}

	/** 
	 * returns the card on top of the waste,
	 * or null if the waste is empty
	 * 
	 * @return the card on top of the waste
	 */
	public Card getWasteCard()
	{
		if (waste.isEmpty())
            return null;
		return waste.peek();
	}

	//precondition:  0 <= index < 4
	//postcondition: returns the card on top of the given
	//               foundation, or null if the foundation
	//               is empty
	/**
	 * returns the card on top of the foundation at the index,
	 * or null if the waste is empty
	 * 
	 * @param index the index of the foundation 
	 * @return the card on top of the foundation at index index 
	 */
	public Card getFoundationCard(int index)
	{
		if(foundations[index].isEmpty()) 
			return null; 
		return foundations[index].peek(); 
	}

	//precondition:  0 <= index < 7
	//postcondition: returns a reference to the given pile
	/**
     * returns the reference to the pile at the given index.
     * 
     * @param index index of the pile
     * @return a reference to the pile at the given index.
     */
	public Stack<Card> getPile(int index)
	{
		Stack<Card> pile = piles[index];
        return pile;
	}

	/**
	 * called when the stock is clicked. Resets the stock if it's empty, otherwise three cards or
     * any remaining cards in the stock are dealt from the stock to the waste.
	 * 
	 */
	public void stockClicked()
	{
		if (!display.isWasteSelected() && !display.isPileSelected())
		{
			if (stock.isEmpty())
				resetStock();
			else
				dealThreeCards();
		}	
	}

	/**
	 * called when the waste is clicked
	 * selects the waste and if it can be moved, it moves it 
	 */
	public void wasteClicked()
	{
		//IMPLEMENT ME
		if(!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected() && !display.isFoundationSelected())
			display.selectWaste();
		else if (display.isWasteSelected())
		{
            if(canAddToFoundation(waste.peek(), validFoundationIndex(waste.peek())))
            {
                foundations[validFoundationIndex(waste.peek())].push(waste.pop());
            }
            display.unselect();
        }


		//System.out.println("waste clicked");
	}

	//precondition:  0 <= index < 7
	//

	/**
	 * called when given pile is clicked. Either moved a card or many cards
	 * to another pile, or a card to the foundation. Or turns a face down card 
	 * face up. (only does these things if it is legal)
	 * 
	 * @param index the index of the pile 
	 */
	public void pileClicked(int index)
	{
		//IMPLEMENT ME
		if (display.isWasteSelected())
		{
			if (canAddToPile(waste.peek(), index))
				getPile(index).push(waste.pop());
			display.unselect();		
		}
		
		else if(display.isPileSelected() && display.selectedPile() != index)
        {
            Stack<Card> cards = removeFaceUpCards(display.selectedPile());
            if(!cards.isEmpty() && canAddToPile(cards.peek(), index))
            {
                addToPile(cards, index);
            }
            else
            {
                addToPile(cards, display.selectedPile());
            }
            display.unselect();
        }
		
		else if(display.isPileSelected() && display.selectedPile() == index)
        {
            if(!getPile(index).isEmpty() && canAddToFoundation(getPile(index).peek(),
                validFoundationIndex(getPile(index).peek())))
            {
                foundations[validFoundationIndex(getPile(index).peek())].push(piles[index].pop());
            }
            display.unselect();
        }
		else if(display.isFoundationSelected())
        {
            if(canAddToPile(getFoundationCard(display.selectedFoundation()), index))
            {
                getPile(index).push(foundations[display.selectedFoundation()].pop());
            }
            display.unselect();
        }
		else if(!getPile(index).isEmpty() && getPile(index).peek().isFaceUp())
        {
			display.selectPile(index);
			
		}
		else if(!getPile(index).isEmpty() && !getPile(index).peek().isFaceUp())
		{
			getPile(index).peek().turnUp();
			//display.unselect(); 
		}
		//System.out.println("pile #" + index + " clicked");
	}

	//precondition:  0 <= index < 4
	
	/**
	 * called when given foundation is clicked
	 * moves a legal card from a pile or waste to a foundation
	 * 
	 * @param index the index of the foundation 
	 */
	public void foundationClicked(int index)
	{
		if(display.isWasteSelected())
        {
            if(canAddToFoundation(getWasteCard(), index))
            {
                foundations[index].push(waste.pop());
            }
            display.unselect();
        }
		else if(display.isPileSelected())
        {
            if(canAddToFoundation(getPile(display.selectedPile()).peek(), index))
            {
                foundations[index].push(getPile(display.selectedPile()).pop());                
            }
            display.unselect();
        }

		else if(display.isFoundationSelected())
        {
            display.unselect();
        }
        else if(!foundations[index].isEmpty())
        {
            display.selectFoundation(index);
        }
		//System.out.println("foundation #" + index + " clicked");
	}

	

	/**
     * Creates an ArrayList containing each of the 52 cards of a standard deck. "Shuffles" cards by
     * repeatedly removing a random card from this ArrayList and adding it to the stock until the
     * ArrayList is empty.
     */
	public void createStock()
	{
		ArrayList<Card> deck = new ArrayList<Card>();
        for(int i = 1; i <= 13; i++)
        {
            deck.add(new Card(i, "s"));
            deck.add(new Card(i, "c"));
            deck.add(new Card(i, "h"));
            deck.add(new Card(i, "d"));
        }
        for(int i = 0; i < 52; i++)
        {
            int index = (int)(Math.random()*deck.size());
            stock.push(deck.remove(index));
        }
		
	}
	/**
     * deals cards from the stock to the 7 piles, with each pile containing one more card than its
     * index. 
	 * turns the top card of each pile face up.
     * 
     * @postcondition Cards were removed from the stock and added to the piles. 
     */
	public void deal()
	{
		for (int i = 0; i < 7; i++)
        {
            for(int j = 0; j <= i; j++) 
            {
                getPile(i).push(stock.pop());
            }
            getPile(i).peek().turnUp();
        }
	}
	
	/**
	 * deals three cards from the stock to the waste, or all remaining cards
	 * if there are less than 3. 
	 * cards are turned face up 
	 * 
	 */
	public void dealThreeCards()
	{
		if (stock.size() < 3)
		{
			while(!stock.isEmpty())
            {
                waste.push(stock.pop());
                waste.peek().turnUp();
            }
		}

		else
		{
			for (int i = 0; i<3; i++)
			{
				waste.push(stock.pop());
           	 	waste.peek().turnUp();
			}
		}
	}

	/**
	 * moves cards from the waste to the stock until there are no cards left 
	 * in the waste. 
	 * cards are turned face down 
	 */
	public void resetStock()
	{
		while(!waste.isEmpty())
		{
			stock.push(waste.pop());
           	stock.peek().turnDown();
		}
	}

	/* precondition:  0 <= index < 7
	 * postcondition: Returns true if the given card can be
	 * legally moved to the top of the given pile
	 */
	/**
	 * tests if it is legal to add a card to a pile 
	 * @param card the card you are trying to move
	 * @param index the index of the pile
	 * @return true if you can move, false otherwise
	 */
	private boolean canAddToPile(Card card, int index)
	{
		if (getPile(index).isEmpty())
        {
            if(card.isFaceUp() && card.getRank() == 13)
            {
                return true;
            }
            return false;
        }

		Card temp = getPile(index).peek(); 
		if(card.isFaceUp() && temp.isFaceUp() && temp.getRank() == card.getRank()+1 && temp.isRed() == !card.isRed())
			return true;
		return false; 
	}

	/* precondition:  0 <= index < 7
	 *postcondition: Removes all face-up cards on the top of
	 *the given pile; returns a stack
	 * containing these cards
	 */
	/**
	 * removes face up cards from one pile at index index 
	 * @param index the index of the pile 
	 * @return a stack of the cards that have been removed 
	 */
	private Stack<Card> removeFaceUpCards(int index)
	{
		Stack<Card> removed = new Stack<Card>();
		while(!getPile(index).isEmpty() && getPile(index).peek().isFaceUp())
			removed.push(getPile(index).pop());
		return removed;
	}

	/*precondition:  0 <= index < 7
	 *postcondition: Removes elements from cards, and adds
	 *them to the given pile.
	 */
	/**
	 * adds a stack of cards to a pile at index index 
	 * 
	 * @param cards a stack of cards that you are adding
	 * @param index the indec of the pile 
	 */
	private void addToPile(Stack<Card> cards, int index)
	{
		while(!cards.isEmpty())
			getPile(index).push(cards.pop());

	}
	
	/* precondition:  0 <= index < 4
	 * postcondition: Returns true if the given card can be
	 * legally moved to the top of the given
	 *  foundation
	 */	
	/**
	 * tests whether it is legal to add a card to the foundation
	 * @param card the card you are trying to move
	 * @param index the index of the foundation
	 * @return true if you can add, false otherwise 
	 */
	private boolean canAddToFoundation(Card card, int index)
	{
		if(foundations[index].isEmpty())
        {
            if(card.isFaceUp() && card.getRank() == 1)
            {
                return true;
            }
		}
		else if(card.isFaceUp() && getFoundationCard(index).getSuit().equals(card.getSuit()) &&
            getFoundationCard(index).getRank() == card.getRank() - 1)
        {
            return true;
        }
    	return false;
	}

	/**
	 * finds the first index of an empty foundation
	 * @return the first index of an empty foundation or -1 if none are empty 
	 */
	private int emptyFoundations()
    {
        for (int i = 0; i < 4; i++)
        {
            if(foundations[i].isEmpty())
            {
                return i;
            }
        }
        return -1;
    }

	/**
	 * finds the first foundation that a card can be added to 
	 * @param card the card you are trying to move
	 * @return the index of the foundation 
	 */
	private int validFoundationIndex(Card card)
    {
        for (int i = 0; i < 4; i++)
        {
            if(!foundations[i].isEmpty() && getFoundationCard(i).getSuit().equals(card.getSuit()))
            {
                return i;
            }
        }
        return emptyFoundations();
    }

	/**
	 * figures out if all the cards are in the foundation
	 * @return true if all cards in the foundation, false otherwise
	 */
	private boolean allInFoundation()
	{
		for (int i = 0; i<4; i++)
		{
			if (getPile(i).isEmpty())
				return false;
		}
		return (waste.isEmpty() && stock.isEmpty() );
			
	}

	/**
	 * celebrates when the user wins the game
	 * prints "you win" if you complete the game 
	 */
	public void celebrate()
	{
		if (allInFoundation())
		{
			System.out.println("YOU WIN");
			
		}
	}


}