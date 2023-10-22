package de.unibremen.swp.Game.GUI;


import com.intellij.uiDesigner.core.GridConstraints;
import de.unibremen.swp.Client.GameClient;
import de.unibremen.swp.Game.Components.Cards.*;
import de.unibremen.swp.Game.Components.Cards.Type.PathType;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureType;
import de.unibremen.swp.Game.Components.Field;
import de.unibremen.swp.Game.Components.Player;
import de.unibremen.swp.Game.GUI.Components.PathCardPanel;
import de.unibremen.swp.Game.GUI.Components.Arrow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * GUI
 *
 * Playing field UI
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class GUI {

    final int WINDOW_WIDTH = 1366;
    final int WINDOW_HEIGHT = 768;
    final String UI_BACKGROUND = "/Background_Small.jpg";
    final String FIELD_BACKGROUND = "/Board_Small.png";

    public static Field field;
    GameClient gameClient;
    public static JPanel gameField;
    public static JLabel playerTurn;
    private JLabel whoYouPlaying;
    public static JFrame frame;
    public static JLayeredPane UI;
    // CardStack etc.
    static JLayeredPane playerTotalCards;
    static JLayeredPane playerObtainedCards;
    static JLabel cardsLeft;

    PauseMenu pauseMenu;
    boolean pauseMenuIsVisible;

    GridConstraints c;


    private Arrow currentArrowClicked;
    private int offSetForMove;
    private Arrow[] arrows;

    /**
     * Construct GUI
     * @param field Field
     * @param gameClient Client
     */
    public GUI(Field field, GameClient gameClient){
        this.field = field;
        this.gameClient = gameClient;
        c = new GridConstraints();
        c.setRow(0);
        c.setColumn(0);
        c.setRowSpan(1);
        c.setColSpan(1);
        //c.myMaximumSize.setSize(new Dimension(96, 96));
        //c.myMinimumSize.setSize(new Dimension(96, 96));
        c.myPreferredSize.setSize(new Dimension(75, 75));
    }


    /**
     * Initialize and render field
     */
    public void displayField() {
        frame = new JFrame("Koole GUI für SWP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);

        //"window"
        UI = new JLayeredPane();
        UI.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        JLabel UIBackground = new JLabel();
        UIBackground.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        UIBackground.setIcon(new ImageIcon(getClass().getResource(UI_BACKGROUND)));

        //"Board"
        JLabel boardBackground = new JLabel();
        boardBackground.setBounds(345, 47, 655, 655);
        boardBackground.setIcon(new ImageIcon(getClass().getResource(FIELD_BACKGROUND)));
        boardBackground.setMinimumSize(new Dimension(655, 655));

        // Field
        gameField = new JPanel();
        gameField.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        gameField.setLayout(new GridLayout(9, 9));
        gameField.setBounds(315, 1, 715, 715);
        gameField.setOpaque(false);

        // Treasure Cards für Spieler 1
        playerTotalCards = new JLayeredPane();
        playerTotalCards.setBounds(40,250,900,900);
        playerObtainedCards = new JLayeredPane();
        playerObtainedCards.setBounds(1050,280,232,307);
        ///if(gameClient.getLocalField().getPlayerList())

        displayCardStack(gameClient);


        // UI Elements
        MenuButton pauseButton = new MenuButton("Pause");
        pauseButton.setBounds(1175,25,150,50);
        pauseButton.addActionListener(e -> {
            pauseMenu.setVisible(true);
        });

        // Move Button
        MenuButton moveButton = new MenuButton("Move");
        moveButton.setBounds(1175,650,150,50);
        moveButton.addActionListener(e -> {
            if(field.getSelection() != null) {
                System.out.println("ayy");
                gameClient.movePlayer(gameClient.getLocalPlayer(), field.getSelection());
            }
            /*
            if(PathCard.currentClicked != null) {
                if(PathCard.currentClicked != field.getExtraCard()) {
                    gameClient.movePlayer(gameClient.getLocalPlayer(), PathCard.currentClicked);
                    //PathCard.currentClicked.removeSelectionBorder();
                    PathCard.currentClicked = null;
                } else {
                    System.out.println("Da darfst du nicht hin..");
                }
            }*/
            gameField.updateUI();
        });
        // Push Button
        MenuButton pushButton = new MenuButton("Push");
        pushButton.setBounds(1175,580,150,50);
        pushButton.addActionListener(e -> {if(currentArrowClicked != null){

            if(currentArrowClicked.getVerticalAlignment() == SwingConstants.TOP){
                notifyGameControllerColumnUp(currentArrowClicked.getXValue());
                Arrow newArrow =(Arrow) Arrays.stream(arrows).filter(arrow -> arrow.getVerticalAlignment() == SwingConstants.BOTTOM && arrow.getXValue() == currentArrowClicked.getXValue()).toArray()[0];
                showExtracardOnArrow(newArrow);
                currentArrowClicked = newArrow;

            } else if (currentArrowClicked.getVerticalAlignment() == SwingConstants.BOTTOM){
                notifyGameControllerColumnDown(currentArrowClicked.getXValue());
                Arrow newArrow =(Arrow) Arrays.stream(arrows).filter(arrow -> arrow.getVerticalAlignment() == SwingConstants.TOP && arrow.getXValue() == currentArrowClicked.getXValue()).toArray()[0];
                showExtracardOnArrow(newArrow);
                currentArrowClicked = newArrow;

            } else if (currentArrowClicked.getHorizontalAlignment() == SwingConstants.LEFT){
                notifyGameControllerRowLeft(currentArrowClicked.getYValue());
                Arrow newArrow =(Arrow) Arrays.stream(arrows).filter(arrow -> arrow.getHorizontalAlignment() == SwingConstants.RIGHT && arrow.getYValue() == currentArrowClicked.getYValue()).toArray()[0];
                showExtracardOnArrow(newArrow);
                currentArrowClicked = newArrow;

            } else if (currentArrowClicked.getHorizontalAlignment() == SwingConstants.RIGHT){
                notifyGameControllerRowRight(currentArrowClicked.getYValue());
                Arrow newArrow =(Arrow) Arrays.stream(arrows).filter(arrow -> arrow.getHorizontalAlignment() == SwingConstants.LEFT && arrow.getYValue() == currentArrowClicked.getYValue()).toArray()[0];
                showExtracardOnArrow(newArrow);
                currentArrowClicked = newArrow;

            }
            currentArrowClicked.updateUI();

            //resete currentArrowClicked (also sorg dafür das die extracard nichtmehr angezeigt wird)
            //setz currentArrowClicked == null

        }});

        // Rotate Button
        MenuButton rotateButton = new MenuButton("Rotate");
        rotateButton.setBounds(1175,510,150,50);
        rotateButton.addActionListener(e -> {
            field.getExtraCard().setRotation(field.getExtraCard().getRotation() + 1);
            try {
                rotateComponent(field.getExtraCard());
            } catch (IOException ignored) {}
            gameField.updateUI();

        });
        // Player Turn
        playerTurn = new JLabel();
        playerTurn.setBounds(0, 45, 136, 122);

        //Who you playing
        whoYouPlaying = new JLabel();
        whoYouPlaying.setBounds(0, 167, 136, 50);
        if(field.getPlayerList().get(0).getId().equals(gameClient.getClientId())){
            whoYouPlaying.setIcon(new ImageIcon(getClass().getResource("/red.png")));
        } else {
            whoYouPlaying.setIcon(new ImageIcon(getClass().getResource("/green.png")));
        }

        // Cards Left
        int cardsLeftCounter = gameClient.getLocalPlayer().getTreasureCards().getTotalCards().size();
        cardsLeft = new JLabel("Cards Left: " + cardsLeftCounter);
        cardsLeft.setBounds(35, 630, 155, 115);
        cardsLeft.setFont(new Font("Serif", Font.BOLD, 22));
        cardsLeft.setForeground(new Color(0xE1E1E1));

        // GameField Buttons and PathCards
        addButtonsAndPathCards();

        // PauseMenu
        pauseMenu = new PauseMenu();
        pauseMenuIsVisible = false;
        // Action for PauseMenu
        Action pauseGame = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!pauseMenuIsVisible) {
                    pauseMenu.setVisible(true);
                    pauseMenuIsVisible = true;
                    // alles andere deaktivieren
                } else {
                    pauseMenu.setVisible(false);
                    pauseMenuIsVisible = false;
                }
            }
        };
        UI.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "pauseGame");
        UI.getActionMap().put("pauseGame", pauseGame);

        UI.add(cardsLeft);
        UI.add(playerTurn);
        UI.add(whoYouPlaying);
        UI.add(pauseButton);
        UI.add(moveButton);
        UI.add(pushButton);
        UI.add(rotateButton);
        UI.add(gameField,  Integer.valueOf(51));
        UI.add(boardBackground, Integer.valueOf(50));
        UI.add(UIBackground, JLayeredPane.DEFAULT_LAYER);
        UI.add(pauseMenu, JLayeredPane.POPUP_LAYER);

        frame.add(UI);
        frame.setResizable(false);
        frame.setVisible(true);
        arrows = Arrays.stream(gameField.getComponents())
                .filter((component -> component instanceof Arrow))
                .toArray(Arrow[]::new);
    }

    public void setPlayerTurn(boolean hasTurn){
        if (hasTurn){
            playerTurn.setIcon(new ImageIcon(getClass().getResource("/your-turn.png")));
        } else {
            playerTurn.setIcon(null);
        }
    }

    /**
     * Populate field with buttons and pathcards
     */
    public void addButtonsAndPathCards(){
        for(int y = 0; y <= 8 ; y++) {
            for(int x = 0; x <= 8; x++) {

                if(y == 0) {
                    //Button für die erste Reihe
                    addButtonTopRow(x, y);
                } else if(y == 8){
                    //Button für die letzte Reihe
                    addButtonBottomRow(x, y);
                } else if (x == 0){
                    //Button für die erste Spalte
                    addButtonLeftColumn(x, y);
                } else if (x == 8){
                    //Button für die letzte Spalte
                    addButtonRightColumn(x, y);
                } else {
                    PathCardPanel pathCardPanel = new PathCardPanel(field,x-1,y-1);
                    //PathCardListener listener = new PathCardListener();

                    pathCardPanel.add(pathCardPanel.getPathCard(), c); //getPathCard gibt die pathcard wieder, lässt sich bestimmt auch mit getImg lösen (prio: 7,8/10) // genau diese Zeilein Constructor von PathCardPanel rein
                    //Random random = new Random();
                    //PathCard pathCard = new PathCard(random.nextInt(4), PathType.values()[random.nextInt(3)],false);
                    //mouselistner //mouselistner auf figure?
                    //pathCardPanel.addMouseListener(listener);


                    gameField.add(pathCardPanel); //if you use pathCard instead of pathCardPanel the white space between rows disappears (prolly cause of the additional jpanel wrapper
                }
            }
        }
        JPanel pathCardPanel = new PathCardPanel(field,9,9);
        pathCardPanel.add(field.getExtraCard(),c);
        UI.add(pathCardPanel);

    }

    /**
     * Populate top button row
     * @param x Coordinate
     * @param y Coordinate
     */
    public void addButtonTopRow(int x, int y){
        Arrow label = new Arrow(x,y,"", SwingConstants.CENTER);
        if(x % 2 == 0 && x != 0 && x != 8){
            label.setText("↓");
            setUpArrow(label);
            label.setVerticalAlignment(SwingConstants.BOTTOM);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showExtracardOnArrow(label);
                    currentArrowClicked = label;
                }
            });
        }
        gameField.add(label);
    }

    /**
     * Populate bottom button row
     * @param x Coordinate
     * @param y Coordinate
     */
    public void addButtonBottomRow(int x, int y){
        Arrow label = new Arrow(x,y,"", SwingConstants.CENTER);
        if(x % 2 == 0 && x != 0 && x != 8) {
            label.setText("↑");
            setUpArrow(label);
            label.setVerticalAlignment(SwingConstants.TOP);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showExtracardOnArrow(label);
                    currentArrowClicked = label;
                }
            });
        }
        gameField.add(label);
    }

    /**
     * Populate left button column
     * @param x Coordinate
     * @param y Coordinate
     */
    public void addButtonLeftColumn(int x, int y){
        Arrow label = new Arrow(x,y,"", SwingConstants.CENTER);
        if(y % 2 == 0 && y != 0 && y != 8) {
            label.setText("→");
            setUpArrow(label);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showExtracardOnArrow(label);
                    currentArrowClicked = label;
                }
            });
        }
        gameField.add(label);
    }

    /**
     * Populate right button column
     * @param x Coordinate
     * @param y Coordinate
     */
    public void addButtonRightColumn(int x, int y){
        Arrow label = new Arrow(x,y,"", SwingConstants.CENTER);
        if(y % 2 == 0 && y != 0 && y != 8) {
            label.setText("←");
            setUpArrow(label);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showExtracardOnArrow(label);
                    currentArrowClicked = label;

                }
            });
        }
        gameField.add(label);
    }

    /**
     * Benachrichtigt den GameController, dass eine Karte in einer Spalte eingefügt werden soll, von oben
     * @param x die Spalte in der die Karte eingefügt werden soll
     */
    public void notifyGameControllerColumnDown(int x){
        System.out.println("2.GUI NOTIFY: " + x);
        gameClient.moveColumnDown(x);
    }
    /**
     * Benachrichtigt den GameController, dass eine Karte in einer Reihe eingefügt werden soll, von rechts
     * @param y die Reihe in der die Karte eingefügt werden soll
     */
    public void notifyGameControllerRowLeft(int y){
        System.out.println("2.GUI NOTIFY: " + y);
        gameClient.moveRowLeft(y);
    }

    /**
     * Benachrichtigt den GameController, dass eine Karte in einer Spalte eingefügt werden soll, von unten
     * @param x die Spalte in der die Karte eingefügt werden soll
     */
    public void notifyGameControllerColumnUp(int x){
        System.out.println("2.GUI NOTIFY: " + x);
        gameClient.moveColumnUp(x);
    }
    /**
     * Benachrichtigt den GameController, dass eine Karte in einer Reihe eingefügt werden soll, von links
     * @param y die Reihe in der die Karte eingefügt werden soll
     */
    public void notifyGameControllerRowRight(int y){
        System.out.println("2.GUI NOTIFY: " + y);
        gameClient.moveRowRight(y);
    }


    /**
     * Updates and repaints field row
     * @param y Y-Coordinate
     */
    public void updateRow(int y){

        Component[] components = gameField.getComponents();

        for (int i = 0; i < components.length; i++){
            if(components[i] instanceof PathCardPanel && ((PathCardPanel) components[i]).j == y){
                PathCardPanel pathCardPanel = (PathCardPanel)components[i];
                pathCardPanel.removeAll();
                try {
                    rotateComponent(pathCardPanel.getPathCard());
                    pathCardPanel.add(pathCardPanel.getPathCard(), c);
                } catch(IOException ignored){}
            }
        }

        UI.repaint();
        UI.revalidate();
    }

    /**
     * Updates and repaints field column
     * @param x X-Coordinate
     */
    public void updateColumn(int x){
        Component[] components = gameField.getComponents();

        for (int i = 0; i < components.length; i++){
            if(components[i] instanceof PathCardPanel && ((PathCardPanel) components[i]).i == x){
                PathCardPanel pathCardPanel = (PathCardPanel)components[i];
                //System.out.println("pc: " + pathCardPanel.getPathCard());
                pathCardPanel.removeAll();
                try {
                    rotateComponent(pathCardPanel.getPathCard());
                    pathCardPanel.add(pathCardPanel.getPathCard(), c);
                } catch(IOException ignored){}
            }
        }

        UI.repaint();
        UI.revalidate();
    }

    /**
     * Display the players cardstack
     * Takes the players cards and displays it like a stack on the left side
     * @param player Player
     */
    public static void displayCardStack(GameClient player) {
        CardStack cardStack = player.getLocalPlayer().getTreasureCards();

        for(int i = 0; i <= cardStack.getTotalCards().size()-1; i++) {
            TreasureCard treasureCard = cardStack.getTotalCards().get(i);
            treasureCard.setBounds(100 - (i * 8), (i * 11), 182, 257);
            playerTotalCards.add(treasureCard);
        }

        UI.add(playerTotalCards);
        player.getGui().playerTotalCards.updateUI();
    }

    /**
     * Updates the players cardstack
     * Takes the players cards and updates it based on the total cards left.
     * @param player Player
     */
    public static void updateCardStack(GameClient player) {
        Player localPlayer = player.getLocalPlayer();
        CardStack cardStack = localPlayer.getTreasureCards();
        playerTotalCards.removeAll(); // remove old cards
        playerTotalCards.revalidate();
        playerTotalCards.repaint();
        System.out.println("CARDSTACK SIZE: " +cardStack.getTotalCards().size());
        for(int i = 0; i <= cardStack.getTotalCards().size()-1; i++) {
            TreasureCard newTreasureCard = cardStack.getTotalCards().get(i);
            newTreasureCard.setBounds(100 - (i * 8), (i * 11), 182, 257);
            playerTotalCards.add(newTreasureCard);
        }

        // update card counter
        cardsLeft.removeAll();
        cardsLeft.setText("Cards Left: " + cardStack.getTotalCards().size());
        player.getGui().playerTotalCards.updateUI();
    }


    /**
     * Zeigt die neue ExtraCard auf dem geklickten Pfeil an und entfernt ggf. die ExtraCard an der alten Stelle.
     * @param label arrow der übergeben wird
     */
    private void showExtracardOnArrow(Arrow label){
        if (currentArrowClicked != null){
            currentArrowClicked.remove(field.getExtraCard());
            currentArrowClicked.updateUI();
        }
        label.add(field.getExtraCard(),c);
        label.updateUI();
    }

    /**
     * Initialize arrow
     * @param label Label
     */
    private void setUpArrow(JLabel label){
        label.setFont(new Font("Serif", Font.BOLD, 20));
        label.setForeground(new Color(0xe7b14f));
        label.setOpaque(false);
        label.setPreferredSize(new Dimension(75, 75));
    }

    /**
     * Rotate field component
     * @param card PathCard
     * @throws IOException Sprite not found
     */
    public void rotateComponent(final PathCard card) throws IOException {
        ImageIcon icon = (ImageIcon)card.getSprite().getIcon();
        BufferedImage img;
        if(String.valueOf(card.getTreasureType()).equals("")){
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource(String.valueOf(card.getPathType()))));
        } else {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource(String.valueOf(card.getTreasureType()))));
        }


        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(getRotationTheta(card)), img.getWidth() / 2, img.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        img = op.filter(img, null);

        card.getSprite().setIcon(new ImageIcon(img));
    }

    /**
     * Retrieve rotation angle
     * @param card PathCard
     * @return Rotiation angle
     */
    private double getRotationTheta(PathCard card) {
        return (card.getRotation() * 90.0f);
    }

    /**
     * Retrieve Field
     * @return Field
     */
    public JPanel getGameField() {
        return gameField;
    }
}
