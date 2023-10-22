package de.unibremen.swp.Game.Components.Cards;

import de.unibremen.swp.Game.Components.Cards.Type.TreasureCardType;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureType;

import java.io.Serializable;
import javax.swing.*;
import java.awt.*;

/**
 * TreasureCard
 *
 * Object used to keep track of treasures to be
 * collected by the player. Each player has 12
 * Cards contained in a CardStack
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class TreasureCard extends JLabel implements Serializable {
    private String treasureCardType;

    /**
     * Construct treasure card
     * @param treasureCardType Treasure type
     */
    public TreasureCard(String treasureCardType) {
        this.treasureCardType = treasureCardType;

        try {
            // muss noch geändert werden
            if(String.valueOf(treasureCardType).equals(TreasureCardType.NONE)) {
                this.setIcon(new ImageIcon(getClass().getResource("/TreasureCard_Back_smoler.png")));
            } else {
                this.setIcon(new ImageIcon(getClass().getResource(treasureCardType)));
            }
            this.setMinimumSize(new Dimension(182, 257));
            this.setOpaque(false);

        } catch (Exception e) {
            this.invalidate();
            e.printStackTrace();
            System.out.println("Fehler beim einlesen");
        }

    }

    /**
     * Retrieve treasure card type
     * @return treasure card type
     */
    public String getTreasureCardType() { return treasureCardType; }
}
