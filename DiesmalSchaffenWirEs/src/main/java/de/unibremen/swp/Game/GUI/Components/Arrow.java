package de.unibremen.swp.Game.GUI.Components;

import javax.swing.*;

/**
 * Arrow
 *
 * Arrow button component
 *
 * @author Sharma Katia, Safia Charif
 */
public class Arrow extends JLabel {
    private final int x;
    private final int y;

    /**
     * Construct button
     * @param x X coord
     * @param y y coord
     * @param text Text
     * @param constant Index
     */
    public Arrow(int x, int y, String text, int constant){
        super(text,constant);
        this.x = x;
        this.y = y;
    }

    /**
     * Get x coord
     * @return X coordinate
     */
    public int getXValue(){
        return x;
    }

    /**
     * Get y coord
     * @return y coordinate
     */
    public int getYValue(){
        return y;
    }
}
