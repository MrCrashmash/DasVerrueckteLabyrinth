package de.unibremen.swp.Game.GUI;

import de.unibremen.swp.Client.GameClient;
import de.unibremen.swp.Game.GUI.Components.Constants;
import de.unibremen.swp.Game.GUI.Components.MenuButton;
import de.unibremen.swp.Server.GameServer;
import de.unibremen.swp.Server.Persistence.ReloadState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Instant;

/**
 * Main menu
 *
 * Main menu screen shown upon startup
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class  MainMenu extends JFrame {

    GUI gui;

    // Frame
    public JFrame frame;

    // UI
    JLayeredPane startUI;

    // Dialog
    String ipPort;
    String ipAdresse;


    // FileChooser
    JFileChooser loadGame;
    int loadResult;

    // Buttons
    MenuButton hostButton;
    MenuButton joinButton;
    MenuButton loadButton;
    MenuButton exitButton;

    public static GameServer gameServer;
    GameClient clientHost;
    GameClient clientGuest;

    /**
     * Constructor
     */
    public MainMenu() {
        // Frame
        frame = new JFrame("Das verrückte Labyrinth: Diesmal schaffst du es! SPECIAL EDITION");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);

        // UI
        startUI = new JLayeredPane();
        startUI.setBounds(0,0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // FileChooser
        loadGame = new JFileChooser();
        loadGame.setCurrentDirectory(new File(System.getProperty("user.home")));

        // Background
        JLabel startUIBackground = new JLabel();
        startUIBackground.setBounds(0,0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        startUIBackground.setIcon(new ImageIcon(getClass().getResource(Constants.UI_BACKGROUND)));

        // Logo
        JLabel startUILogo = new JLabel();
        startUILogo.setBounds(490, 90, 400, 200);
        startUILogo.setIcon(new ImageIcon(getClass().getResource(Constants.UI_LOGO)));


        // Buttons
        hostButton = new MenuButton("Host Game");
        hostButton.setBounds(555,360,260,65);
        hostButton.addActionListener(e -> {
            if(gameServer == null) {
                ipPort = JOptionPane.showInputDialog(frame,"IP-Port:");
                if(ipPort == null || ipPort.length() != 4) {
                    System.out.println("Port is schlecht!");
                } else {
                    int port = Integer.parseInt(ipPort);
                    gameServer = new GameServer(port);
                    String unixTime = Long.toString(Instant.now().getEpochSecond());
                    clientHost = new GameClient(unixTime, "127.0.0.1", port);
                    clientHost.setMenu(frame);
                }
            } else {
                String unixTime = Long.toString(Instant.now().getEpochSecond());
                clientHost = new GameClient(unixTime, "127.0.0.1", gameServer.getPort());
                clientHost.setMenu(frame);
            }
        });

        joinButton = new MenuButton("Join game");
        joinButton.setBounds(555,445,260,65);
        joinButton.addActionListener(e -> {
            ipPort = JOptionPane.showInputDialog(frame,"IP-Port:");
            ipAdresse = JOptionPane.showInputDialog(frame,"IP-Adresse:");
            if(ipPort == null || ipPort.length() != 4) {
                System.out.println("Port is schlecht!");

            } else if(ipAdresse == null) {
                System.out.println("IP-Adresse is schlecht!");
            } else {
                int port = Integer.parseInt(ipPort);
                String unixTime = Long.toString(Instant.now().getEpochSecond());
                clientGuest = new GameClient(unixTime, ipAdresse, port); //replace with your own local ipv4 for testing
                clientGuest.setMenu(frame);

                try {
                    Thread.sleep(5000);
                } catch(InterruptedException ignored){}

                //initcallback(clientGuest);
            }
        });
        // join listener gleich wie host?

        loadButton = new MenuButton("Load Game");
        loadButton.setBounds(555,530,260,65);
        loadButton.addActionListener(e ->{
            loadResult = loadGame.showOpenDialog(frame);
            if (loadResult == JFileChooser.APPROVE_OPTION){
                File selectedFile = loadGame.getSelectedFile();
                ReloadState reloadState = new ReloadState();
                try{
                    GameServer gameServerLoaded = reloadState.containerToGameServer(selectedFile);
                    System.out.println("AUSGABE LOAD: " + gameServerLoaded);
                    if(gameServer == null) {
                        gameServer = gameServerLoaded;
                    }
                } catch(IOException ignored){}
            }
        });

        exitButton = new MenuButton("Exit Game");
        exitButton.setBounds(555,615,260,65);
        exitButton.addActionListener(e -> {
                // close game
                frame.dispose();
                System.exit(0);
        });

        // Add UI Elements
        startUI.add(startUILogo);
        startUI.add(exitButton);
        startUI.add(hostButton);
        startUI.add(joinButton);
        startUI.add(loadButton);
        startUI.add(startUIBackground);

        // Show frame
        frame.add(startUI);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main( String[] args )
    {
        new MainMenu();
    }
}
