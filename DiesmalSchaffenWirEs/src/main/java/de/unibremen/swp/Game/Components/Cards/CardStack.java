package de.unibremen.swp.Game.Components.Cards;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * CardStack
 *
 * Object to hold a given player's TreasureCards
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class CardStack implements Serializable {
    private TreasureCard currentCard;
    private ArrayList<TreasureCard> totalCards = new ArrayList<>();
    private ArrayList<TreasureCard> obtainedCards = new ArrayList<>();

    /**
     * Construct Cardstack
     * @param cards Contained treasure cards
     */
    public CardStack(final ArrayList<TreasureCard> cards) {
        this.totalCards = cards;
    }

    /**
     * Retrieve treasure card that is currently being seeked
     * @return Current treasure card
     */
    public TreasureCard getCurrentCard() {
        return currentCard;
    }

    /**
     * Set current treasure card
     * @param currentCard Current treasure card
     */
    public void setCurrentCard(TreasureCard currentCard) {
        this.currentCard = currentCard;
    }

    /**
     * Retrieve all TreasureCards within the card stack
     * @return A list of all treasure cards
     */
    public ArrayList<TreasureCard> getTotalCards() {
        return totalCards;
    }

    /**
     * Set the list of cards contained within the stack
     * @param totalCards A list of all cards
     */
    public void setTotalCards(ArrayList<TreasureCard> totalCards) {
        this.totalCards = totalCards;
    }

    /**
     * Retrieve list of cards found/won by the player
     * @return A list of all found cards
     */
    public ArrayList<TreasureCard> getObtainedCards() {
        return obtainedCards;
    }

    /**
     * Set the lost of found cards
     * @param obtainedCards Found cards
     */
    public void setObtainedCards(ArrayList<TreasureCard> obtainedCards) {
        this.obtainedCards = obtainedCards;
    }

    /**
     * Equality override to compare cardstacks
     * @param obj Cardstack
     * @return Whether or not they are equal
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof CardStack){
            return this.currentCard.equals(((CardStack) obj).currentCard)
                    && this.totalCards.equals(((CardStack) obj).totalCards)
                    && this.obtainedCards.equals(((CardStack) obj).obtainedCards);
        } else {
            return false;
        }
    }
}