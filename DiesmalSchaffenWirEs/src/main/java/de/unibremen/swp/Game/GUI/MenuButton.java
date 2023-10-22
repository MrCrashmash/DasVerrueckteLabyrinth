package de.unibremen.swp.Game.GUI;

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
    Color color1 = new Color(3, 59, 90);
    Color color2 = new Color(3, 59, 90).brighter().brighter();
    Color color3 = new Color(54, 32, 3);

    /**
     * Construct Button
     * @param text Text
     */
    public MenuButton(String text) {
        super(text);
        setBackground(color1);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Tahoma", Font.BOLD, 18));
        setForeground(Color.ORANGE);
        setVisible(true);

        // change color
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(color2);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(color1);
            }
        });
    }
}