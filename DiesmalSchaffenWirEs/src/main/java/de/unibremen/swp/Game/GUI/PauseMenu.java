package de.unibremen.swp.Game.GUI;

import de.unibremen.swp.Game.GUI.Components.Constants;
import de.unibremen.swp.Server.GameServer;
import de.unibremen.swp.Server.Persistence.ReloadState;
import de.unibremen.swp.Server.Persistence.SaveState;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Pause Menu
 *
 * Pause Menu
 *
 * @author Zimmermann Henning
 */
public class PauseMenu extends JPanel {
    // Entfernen durch Constants. ersetzen
    Color BACKGROUND_BLACK = new Color(0,0,0,230);

    // Text
    JLabel logo;

    // Buttons
    MenuButton saveButton;
    MenuButton exitButton;
    MenuButton resumeButton;

    /**
     * Construct Menu
     */
    public PauseMenu() {
        setBounds(0,0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setBackground(BACKGROUND_BLACK);
        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        // Text
        logo = new JLabel();
        logo.setBounds(480,90,400,200);
        logo.setIcon(new ImageIcon(getClass().getResource(Constants.UI_LOGO)));

        resumeButton = new MenuButton("Resume");
        resumeButton.setBounds(555,360,260,65);
        resumeButton.addActionListener(e -> {
            setVisible(false);
        });

        // Buttons
        saveButton = new MenuButton("Save Game");
        saveButton.setBounds(555,445,260,65);
        saveButton.addActionListener(e -> {
            if(MainMenu.gameServer != null) {
                JFileChooser saveGame = new JFileChooser();
                //TODO: Eventuell nicht "this"
                int loadResult = saveGame.showOpenDialog(this);
                if (loadResult == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = saveGame.getSelectedFile();
                    SaveState saveState = new SaveState();
                    try {
                        saveState.saveGameServer(MainMenu.gameServer, selectedFile);
                        System.out.println("habe cool gesaved");
                    } catch (Exception ignored) {}
                }
            } else {
                int result = JOptionPane.showConfirmDialog(this, "Only the host can save the game!", null, JOptionPane.CANCEL_OPTION);
            }
        });


        exitButton = new MenuButton("Exit Game");
        exitButton.setBounds(555,530,260,65);
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,"Are you sure? \nMake sure to safe before", null, JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                // close game
                GUI.frame.dispose();
                System.exit(0);
            }
        });

        add(logo);
        add(resumeButton);
        add(saveButton);
        add(exitButton);
        setVisible(false);
    }

    /**
     * Draws the Background for the JPanel
     * @param g UI / Background
     */
    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);
    }

}
