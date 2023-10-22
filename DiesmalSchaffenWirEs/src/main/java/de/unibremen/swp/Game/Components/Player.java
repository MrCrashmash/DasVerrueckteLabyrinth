package de.unibremen.swp.Game.Components;


import de.unibremen.swp.Game.Components.Cards.CardStack;

import javax.swing.*;
import java.io.Serializable;

/**
 * Player
 *
 * Object that holds player data
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class Player implements Serializable {

    private String id;
    private final Figure figure;
    private int score;
    private CardStack treasureCards;

    /**
     * Construct Player
     * @param id Player id
     * @param figure Player figure
     * @param treasureCards Player's treasure cards
     */
    public Player(String id, Figure figure, CardStack treasureCards) {
        this.id = id;
        this.figure = figure;
        this.score = 0;
        this.treasureCards = treasureCards;
    }

    /**
     * Get player Id
     * @return Player id
     */
    public String getId() {
        return id;
    }

    /**
     * Set player Id
     * @param id Player id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get player figure
     * @return Figure
     */
    public Figure getFigure() {
        return figure;
    }

    /**
     * Retrieve score
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set score
     * @param score Score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieve CardStack
     * @return CardStack
     */
    public CardStack getTreasureCards() {
        return treasureCards;
    }

    /**
     * Set Cardstack
     * @param treasureCards Cardstack
     */
    public void setTreasureCards(final CardStack treasureCards) {
        this.treasureCards = treasureCards;
    }

    /**
     * Equality check
     * @param obj Player
     * @return Whether or not they're equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return this.id.equals(((Player) obj).id)
                    && this.figure.equals(((Player) obj).figure)
                    && this.score == ((Player) obj).score
                    && this.treasureCards.equals(((Player) obj).treasureCards);
        } else {
            return false;
        }

    }
}