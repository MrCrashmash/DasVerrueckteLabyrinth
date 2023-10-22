package de.unibremen.swp.Client;

import de.unibremen.swp.Game.Components.Cards.PathCard;
import de.unibremen.swp.Game.Components.Cards.TreasureCard;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureCardType;
import de.unibremen.swp.Game.Components.Cards.Type.TreasureType;
import de.unibremen.swp.Game.Components.Field;
import de.unibremen.swp.Game.Components.Player;
import de.unibremen.swp.Game.GUI.MainMenu;
import de.unibremen.swp.Game.PlayerMove;
import de.unibremen.swp.Game.GUI.GUI;
import de.unibremen.swp.Network.Client;
import de.unibremen.swp.Network.Datapackage;
import de.unibremen.swp.Network.MessageType;
import org.javatuples.Triplet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameClient extends Client {
    private Field localField;

    public boolean hasTurn = false;

    private boolean hasInserted = false;

    private boolean hasWon = false;

    private boolean hasPaired = false;

    private GUI gui;

    private JFrame frame;


    public GameClient(final String clientId, final String hostname, final int port) {
        super(hostname, port, clientId);

        registerMethod(MessageType.DEBUG, (message, socket) -> {
            debugPrint(message);
        });

        registerMethod(MessageType.CONFIRM_PAIRING, (message, socket) -> {
            setHasPaired(true);
            this.setGui(new GUI(getLocalField(), this));
            frame.dispose();
            getGui().displayField();

            System.out.println(getLocalPlayer().getTreasureCards());
            getGui().updateCardStack(this);

            getGui().getGameField().updateUI();
        });

        /**
         * Receive a field update from the server
         */
        registerMethod(MessageType.FIELD_UPDATE, (message, socket) -> {
            if(message.get(1) instanceof Field) {
                setLocalField((Field) message.get(1));
                System.out.println("playerlist: " + getLocalField().getPlayerList());
                System.out.println("extra: " + getLocalField().getExtraCard());
                //gui = new GUI(getLocalField(), this);
            } //GUI has to be updated, how?!
            if(getGui() != null) {
                getGui().getGameField().updateUI();
            }

            //gui = new GUI(getLocalField(), this);
            debugPrint(message);
        });

        /**
         * Receive winner id from server
         * Controls which player won the game
         */
        registerMethod(MessageType.WINNER, (message, socket) -> {
            String winnerId = (String) message.get(1);

            if(winnerId.equals(getClientId())) {
                int result = JOptionPane.showConfirmDialog(getGui().getGameField(),"Back to the Menu?", "YOU WON", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    // close game
                    getGui().frame.dispose();
                    stop();
                    new MainMenu();
                } else{
                    getGui().frame.dispose();
                    System.exit(0);
                }
            } else {
                int result = JOptionPane.showConfirmDialog(getGui().getGameField(),"Back to the Menu?", "YOU LOST", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    // close game
                    getGui().frame.dispose();
                    stop();
                    new MainMenu();
                } else{
                    getGui().frame.dispose();
                    System.exit(0);
                }
            }
        });

        /**
         * Receive player move update
         */
        registerMethod(MessageType.PLAYER_UPDATE, (message, socket) -> {
            ArrayList<Object> test = (ArrayList<Object>)message.get(1);
            System.out.println(getClientId() + "got the update from " + test.get(1));
            //[0]-id   [1] PlayerMove
            //ArrayList<Object> test = (ArrayList<Object>)message.get(1);
            PlayerMove move = (PlayerMove) test.get(1);
            String id = (String)test.get(0);
            PathCard destination = getLocalField().getPathCard(move.getDestination()[0], move.getDestination()[1]);
            getLocalField().movePlayer(id, destination);
            getGui().getGameField().updateUI();

            String curType = getLocalPlayer().getTreasureCards().getCurrentCard().getTreasureCardType();
            if(curType.equals("/TC_Red.png") && destination.getTreasureType().equals("/p_L_startred.jpg")){
                sendMessage(MessageType.WINNER, "ICH HAB GEWONNEN");
                hasWon = true;
            } else if(curType.equals("/TC_Green.png") && destination.getTreasureType().equals("/p_L_startgreen.jpg")){
                sendMessage(MessageType.WINNER, "ICH HAB GEWONNEN");
                hasWon = true;
            } else {
                if(id.equals(getClientId())){
                    checkTreasureAndCollect();
                }
            }
        });

        /**
         * Receive turn id from server
         * Controls which player's turn it is
         */
        registerMethod(MessageType.TURN, (message, socket) -> {
            String turnId = (String) message.get(1);

            if(turnId.equals(getClientId())) {
                hasTurn = true;
                getGui().setPlayerTurn(hasTurn);
            } else {
                hasTurn = false;
                getGui().setPlayerTurn(hasTurn);
            }
        });

        /**
         * Receive insertion validation from server
         */
        registerMethod(MessageType.INSERT_VALIDATE, (message, socket) -> {
            if(message.get(1).equals("OK")) {
                hasInserted = true;
            } else {
                hasInserted = false;
            }
        });

        /**
         * Receive pause request from the server
         */
        registerMethod(MessageType.COLROW_UPDATE, (message, socket) -> {
            //res[Direction, Col, Row, ExtraCard-Rotation]
            String[] res = ((String)message.get(1)).split(" ");
            Triplet<String, Integer, Integer> pathMove = new Triplet<>(res[0],  Integer.valueOf(res[1]), Integer.valueOf(res[2]));
            localField.getExtraCard().setRotation(Integer.parseInt(res[3]));
                if(res[0].equals("UP")) {
                    localField.moveColumnUp(Integer.parseInt(res[1]));
                    getGui().updateColumn(Integer.parseInt(res[1]));
                }
                if(res[0].equals("DOWN")){
                    localField.moveColumnDown(Integer.parseInt(res[1]));
                    getGui().updateColumn(Integer.parseInt(res[1]));
                }
                if(res[0].equals("LEFT")){
                    localField.moveRowLeft(Integer.parseInt(res[2]));
                    getGui().updateRow(Integer.parseInt(res[2]));
                }
                if(res[0].equals("RIGHT")){
                    localField.moveRowRight(Integer.parseInt(res[2]));
                    getGui().updateRow(Integer.parseInt(res[2]));
                }

            getGui().getGameField().updateUI();
            debugPrint(message);
        });


        /**
         * Receive pause request from the server
         */
        registerMethod(MessageType.PAUSE, (message, socket) -> {
            //TODO Pausieren der buttons oder neues UIPanel anzeigen lassen
        });

        /**
         * Receive resume request from the server
         */
        registerMethod(MessageType.RESUME, (message, socket) -> {
            //TODO Aktivieren der buttons oder Game wieder anzeigen lassen
        });

        /**
         * Connect to server and start listening to incoming packages
         */
        start();
    }

    /**
     *
     * @return whether the client has won
     */
    public boolean getHasWon(){
        return hasWon;
    }

    /**
     *
     * @return bowhether it is the client's turn
     */
    public boolean getHasTurn(){
        return hasTurn;
    }

    public void setMenu(final JFrame frame) {
        this.frame = frame;
    }

    /**
     * Get local playing field
     * @return Local playing field
     */
    public Field getLocalField() {
        return localField;
    }

    /**
     * Set local playing field
     * @param field New field
     */
    private void setLocalField(final Field field) {
        localField = field;
    }

    public void setHasPaired(final boolean hasPaired) {
        this.hasPaired = hasPaired;
    }

    public boolean getHasPaired() {
        return this.hasPaired;
    }

    public void setGui(final GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return this.gui;
    }

    public void invokeGui() {
        gui.displayField();
    }

    public Player getPlayerBySenderId(final String id) {
        for(Player player : getLocalField().getPlayerList()) {
            if(player.getId().equals(id)) {
                return player;
            }
        }
        throw new IllegalArgumentException("No such player found!");
    }

    /**
     * Extracts player object belonging to the specific game client
     * from the local field player list
     * @return The respective player object
     */
    public Player getLocalPlayer() {
        for(Player p : getLocalField().getPlayerList()) {
            if(p.getId().equals(getClientId())) {
                return p;
            }
        }
        throw new IllegalArgumentException("No matching player found in player list!");
    }

    //////// PLAYER MOVEMENT ////////
    /**
     * Spieler bewegen.
     * Bekommt Spieler und Ziel-Parhcard übergeben
     * @param player Spieler
     * @param destination Ziel
     */
    public void movePlayer(Player player, PathCard destination) {
        Datapackage res = sendMessage(MessageType.MOVE_VALIDATE, new PlayerMove(localField.getIndices(player.getFigure().getCurrentPathCard()), localField.getIndices(destination)));
        System.out.println("Antwort" +res);
    }

    //////// PLAYER INSERTS ////////
    public void moveRowLeft(int y){
        //System.out.println("[GC] EC-R before: " + field.getExtraCard().getRotation());
        Datapackage res = sendMessage(MessageType.INSERT_VALIDATE, "LEFT -1 " + (y - 1) + " " + localField.getExtraCard().getRotation());
        System.out.println("Antwort" +res);
    }

    public void moveColumnUp(int x){
        //System.out.println("[GC] EC-R before: " + field.getExtraCard().getRotation());
        Datapackage res = sendMessage(MessageType.INSERT_VALIDATE, "UP " + (x - 1) + " -1" + " " + localField.getExtraCard().getRotation());
        System.out.println("Antwort" +res);
    }

    public void moveRowRight(int y){
        //res[Direction, Col, Row, ExtraCard-Rotation]
        Datapackage res = sendMessage(MessageType.INSERT_VALIDATE, "RIGHT -1 " + (y - 1) + " " + localField.getExtraCard().getRotation());
        System.out.println("Antwort" +res);
    }

    public void moveColumnDown(int x){
        //System.out.println("[GC] EC-R before: " + field.getExtraCard().getRotation());
        Datapackage res = sendMessage(MessageType.INSERT_VALIDATE, "DOWN " + (x - 1) + " -1" + " " + localField.getExtraCard().getRotation());
        System.out.println("Antwort" +res);

    }

    // Methode um zu überprüfen ob der Spieler gerade auf einem Schatz steht und ob dieser seiner obersten Karte ist, wenn ja dann einsammeln
    public void checkTreasureAndCollect() {
        Player player = getLocalPlayer();
        PathCard pathCard = player.getFigure().getCurrentPathCard();

        // TreasureCardType von erster Karte und TreasureType von PathCard herausfinden und getValue() vergleichen, wenn ja wird eingesammelt
        if(!(String.valueOf(pathCard.getTreasureType()).equals(TreasureType.NONE))) {
            boolean match = false;
            String tmpTc = String.valueOf(player.getTreasureCards().getCurrentCard().getTreasureCardType());
            String tmpPc = String.valueOf(pathCard.getTreasureType());

            String subTmpTc = tmpTc.substring(4, tmpTc.length() - 4);
            String subTmpPc = tmpPc.substring(5, tmpPc.length() - 4);

            if (subTmpTc.equals(subTmpPc)) {
                // TreasureCard der ObtainedCards des Spielers hinzufügen
                player.getTreasureCards().getObtainedCards().add(player.getTreasureCards().getCurrentCard());
                // CurrentCard aus den Totalcards entfernen
                player.getTreasureCards().getTotalCards().remove(player.getTreasureCards().getTotalCards().get(0));
                // Nächste Karte als CurrentCard setzen

                if(player.getTreasureCards().getTotalCards().size() != 0 && !hasWon){
                    player.getTreasureCards().setCurrentCard(player.getTreasureCards().getTotalCards().get(0));
                } else {
                    if(getLocalPlayer().getFigure().getColor().equals("GREEN")){
                        player.getTreasureCards().getTotalCards().add(new TreasureCard("/TC_Green.png"));
                    } else {
                        player.getTreasureCards().getTotalCards().add(new TreasureCard("/TC_Red.png"));
                    }
                    player.getTreasureCards().setCurrentCard(player.getTreasureCards().getTotalCards().get(0));

                }
                getGui().updateCardStack(this);
                sendMessage(MessageType.CARDSTACK_UPDATE, getLocalPlayer().getTreasureCards());
            }
        }
    }
}
