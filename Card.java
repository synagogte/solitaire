import javax.lang.model.util.ElementScanner14;

/**
 * The Card class can instantiate a Card object, can return a Card object's rank and suit, can 
 * determine whether a Card object is red or not, determine whether a Card object is 
 * face up or not, can turn a card face up, can turn a card face down, 
 * and can return a Card object's file name. 
 * 
 * @author Syna Gogte
 * @version 11/1/21
 */

public class Card
{
    private int rank; 
    private String suit;
    private boolean isFaceUp;

    /**
     * Constructor for objects of class Card
     * 
     * @param r the rank of the card (a number between 1 and 13)
     * @param s the suit of the card, must be "s", "c", "h" or "d"
     */
    public Card(int r, String s)
    {
        rank = r;
        suit = s;
        isFaceUp = false; 
}


    /*
     * gets the rank of the card
     * @return the rank of the card
     */
    public int getRank()
    {
      return rank;
    }

    /*
     * gets the suit of the card
     * @return the suit of the card
     */
    public String getSuit()
    {
        return suit;
    }

    /*
     * finds out whether the color of the card is red
     * @return true if the card is red, false otherwise
     */
    public boolean isRed()
    {
        return (suit.equals("d") || suit.equals("h")); 
    }

    /*
     * finds out whether the card is face up or down
     * @return true if the card is face up, false otherwise
     */
    public boolean isFaceUp()
    {
        return isFaceUp; 
    }

    /*
     * turns the card so it is face up 
     */
    public void turnUp()
    {
        isFaceUp = true; 
    }

    /*
     * turns the card so it is face down
     */
    public void turnDown()
    {   
        isFaceUp = false; 
    }

    public String getFileName()
    {
        if(!isFaceUp)
           return "cards/back.gif";
        else
        {
            if(rank > 1 && rank <10)
                return "cards/" + rank + suit + ".gif";
            String rName = "";
            if (rank == 1)
                rName = "a";
            else if (rank == 10)
                rName = "t";
            else if (rank == 11)
                rName = "j";
            else if (rank == 12)
                rName = "q";
            else
                rName = "k"; 
            return "cards/" + rName + suit + ".gif"; 
        }   
    } 
}
