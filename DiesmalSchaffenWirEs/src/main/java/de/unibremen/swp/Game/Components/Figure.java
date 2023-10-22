package de.unibremen.swp.Game.Components;

import de.unibremen.swp.Game.Components.Cards.PathCard;
import javax.swing.*;
import java.awt.*;

/**
 * Field
 *
 * Object that holds all game tiles,
 * so called PathCards
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class Figure extends JLabel {

    private final String spritePath; // War glaube anders gedacht, ist jetzt der dateipfad für den spieler
    private final String color;
    private PathCard currentPathCard;
    private JLabel sprite;

    /**
     * Construct Figure
     * @param spritePath Sprite path
     * @param color Sprite color
     */
    public Figure (String spritePath, String color){
        this.spritePath = spritePath;
        this.color = color;

        try {
            sprite = new JLabel(new ImageIcon(getClass().getResource(spritePath)), SwingConstants.CENTER);
            sprite.setMinimumSize(new Dimension(25, 40));
            sprite.setOpaque(false);
            add(sprite);
        } catch(Exception e) {
            this.invalidate();
            e.printStackTrace();
            System.out.println("Fehler beim einlesen");
        }
    }

    /**
     * Retrieve Sprite
     * @return Sprite
     */
    public JLabel getSprite() { return sprite; }

    /**
     * Retrieve sprite path
     * @return Sprite path
     */
    public String getSpritePath() {
        return spritePath;
    }

    /**
     * Retrieve color
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * Retrieve path card
     * @return PathCard
     */
    public PathCard getCurrentPathCard() {
        return currentPathCard;
    }

    /**
     * Set current PathCard
     * @param currentPathCard PathCard
     */
    public void setCurrentPathCard(PathCard currentPathCard) {
        this.currentPathCard = currentPathCard;
    }

    /**
     * Equality check
     * @param obj Figure
     * @return Whether or not thery're equal
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Figure){
            return this.spritePath.equals(((Figure) obj).getSpritePath())
            && this.color.equals(((Figure) obj).getColor())
            && this.currentPathCard.equals(((Figure) obj).currentPathCard);
        } else {
            return false;
        }
    }
}
