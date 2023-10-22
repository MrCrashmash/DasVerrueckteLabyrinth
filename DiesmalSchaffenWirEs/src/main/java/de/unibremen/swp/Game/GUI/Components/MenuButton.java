package de.unibremen.swp.Game.GUI.Components;

import javax.swing.*;
import java.awt.*;

/**
 * Menu Button
 *
 * Menu Button
 *
 * @author Zimmermann Henning
 */
public class MenuButton extends JButton {
    /**
     * Construct Button
     * @param text Text
     */
    public MenuButton(String text) {
        super(text);
        setBackground(Constants.UI_BUTTON_BLUE);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Tahoma", Font.BOLD, 18));
        setForeground(Color.ORANGE);
        setVisible(true);
    }
}
