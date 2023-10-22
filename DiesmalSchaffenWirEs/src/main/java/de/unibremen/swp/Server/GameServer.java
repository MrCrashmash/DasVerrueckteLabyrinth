package de.unibremen.swp.Server;

import de.unibremen.swp.Game.Components.Cards.CardStack;
import de.unibremen.swp.Game.Components.Cards.PathCard;
import de.unibremen.swp.Game.Components.Figure;
import de.unibremen.swp.Game.PlayerMove;
import de.unibremen.swp.Game.Components.Field;
import de.unibremen.swp.Game.Components.Player;
import de.unibremen.swp.Network.Datapackage;
import de.unibremen.swp.Network.MessageType;
import de.unibremen.swp.Network.Server;
import org.javatuples.Triplet;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * GameServer
 * Defines necessary control sequences and the core
 * game flow for 2-Client communication
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */

public class GameServer extends Server {
    private Field field;

    private boolean isLoadedFromPersistence = false;

    private String currentTurnId = "";

    private boolean allreadyInsertet = false;

    private ArrayList<Player> players = new ArrayList<>(2);

    private ServerLogic serverLogic;

    private ServerController serverController = new ServerController();


    /**
     * Server constructor
     * @param port Port which the server should listen on
     */
    public GameServer(final int port) {
        super(port, true, true, false, false);
    }

    /**
     * Callback to handle client registration
     * initiates the game when two clients connect.
     * @param message The message a client registered with.
     * @param socket The socket a client registered with.
     *
     */
    @Override
    public void onClientRegistered(final Datapackage message, final Socket socket) {
        System.out.println(getClientCount());
        if(players.isEmpty() && getClientCount() == 2) {
            if(isLoadedFromPersistence) {
                persistenceRoutine();
            } else {
                regularRoutine();
            }
        }
    }

    private void persistenceRoutine(){
        System.out.println("loaded from persistence");
        System.out.println("persistence field: " + getField());

        //get old ids
        String[] oldIds = new String[2];
        for(int i = 0; i < getField().getPlayerList().size(); i++) {
            oldIds[i] = getField().getPlayerList().get(i).getId();
        }

        String[] newIds = new String[2];
        for(int i = 0; i < 2; i++) {
            newIds[i] = getClients().get(i).getId();
        }

        Arrays.sort(oldIds);
        Arrays.sort(newIds);

        String newTurnId = "";

        //replace old ids
        for(int i = 0; i < 2; i++) {
            if(oldIds[i].equals(getCurrentTurnId())) {
                newTurnId = newIds[i];
            }
            getField().getPlayerList().get(i).setId(newIds[i]);
        }

        broadcastMessage(new Datapackage(MessageType.FIELD_UPDATE, getField()));

        this.serverLogic = new ServerLogic(getField());
        try {
            Thread.sleep(2000);
            broadcastMessage(new Datapackage(MessageType.CONFIRM_PAIRING, "PAIRED"));
            Thread.sleep(4000);
            setCurrentTurnId(newTurnId);

            ArrayList<Object> data = new ArrayList<>();
            data.add(getField().getPlayerList().get(0).getId());
            int[] hm = getField().getIndices(getField().getPlayerList().get(0).getFigure().getCurrentPathCard());
            data.add(new PlayerMove(hm, hm));
            broadcastMessage(new Datapackage(MessageType.PLAYER_UPDATE, data));

            ArrayList<Object> dataa = new ArrayList<>();
            dataa.add(getField().getPlayerList().get(1).getId());
            int[] hmm = getField().getIndices(getField().getPlayerList().get(1).getFigure().getCurrentPathCard());
            dataa.add(new PlayerMove(hmm, hmm));
            broadcastMessage(new Datapackage(MessageType.PLAYER_UPDATE, dataa));
        } catch(InterruptedException ignored){}
    }

    private void regularRoutine() {
        //Construct players from client data
        ArrayList<RemoteClient> clients = getClients();
        int firstId = Integer.parseInt(clients.get(0).getId());
        int secondId = Integer.parseInt(clients.get(1).getId());

        ArrayList<CardStack> cardStacks = serverController.createCardStacks();

        if(firstId < secondId) {
            players.add(new Player(String.valueOf(firstId), new Figure("/player1.png", "RED"), cardStacks.get(0)));
            players.add(new Player(String.valueOf(secondId), new Figure("/player2.png", "GREEN"), cardStacks.get(1)));
        } else {
            players.add(new Player(String.valueOf(secondId), new Figure("/player1.png", "RED"), cardStacks.get(0)));
            players.add(new Player(String.valueOf(firstId), new Figure("/player2.png", "GREEN"), cardStacks.get(1)));
        }

        setField(serverController.createField());
        getField().setPlayerList(players);
        getField().getPathCard(0, 0).getSprite().add(players.get(0).getFigure().getSprite(), new GridBagConstraints());
        getField().getPathCard(6, 6).getSprite().add(players.get(1).getFigure().getSprite(), new GridBagConstraints());

        players.get(0).getFigure().setCurrentPathCard(getField().getPathCard(0, 0));
        players.get(1).getFigure().setCurrentPathCard(getField().getPathCard(6, 6));

        field.setPlayerList(players);

        //Broadcast playing field to clients
        broadcastMessage(new Datapackage(MessageType.FIELD_UPDATE, getField()));
        //Randomly pick player to make first turn and broadcast

        //construct server logic and server controller
        this.serverLogic = new ServerLogic(getField());
        try {
            Thread.sleep(2000);
            broadcastMessage(new Datapackage(MessageType.CONFIRM_PAIRING, "PAIRED"));
            Thread.sleep(4000);
            setCurrentTurnId(players.get(new Random().nextInt(2)).getId());
            //broadcastMessage(new Datapackage(MessageType.TURN, currentTurnId));

        } catch(InterruptedException ignored){}
    }

    /**
     * Handle messages received from a client
     */
    @Override
    public void preStart() {

        /**
         * Receive move validate request from client, validates the move,
         * if ok server broadcast field and executes the move
         */
        registerMethod(MessageType.MOVE_VALIDATE, (message, socket) -> {
            if(isItYourTurn(message.getSenderID())) {
                System.out.println("CurTURN: " + currentTurnId);
                System.out.println("Requester: " + message.getSenderID());
                if(allreadyInsertet) {
                    PlayerMove move = (PlayerMove) message.get(1);
                    if (serverLogic.validateMove(move.getOrigin(), move.getDestination())) {
                        Player curPlayer = serverLogic.getPlayerBySenderId(message.getSenderID());

                        int x = move.getDestination()[0];
                        int y = move.getDestination()[1];
                        curPlayer.getFigure().setCurrentPathCard(field.getPathCard(x, y));

                        ArrayList<Object> test = new ArrayList<>();
                        test.add(message.getSenderID());
                        test.add(move);
                        broadcastMessage(new Datapackage(MessageType.PLAYER_UPDATE, test));


                        allreadyInsertet = false;
                        System.out.println("allreadyInsertet "+ allreadyInsertet);
                        for(Player p : getField().getPlayerList()){
                            System.out.println("isItYourTurn: " + isItYourTurn(p.getId()));
                            if(!(isItYourTurn(p.getId()))){
                                System.out.println("cur " + currentTurnId);
                                setCurrentTurnId(p.getId());
                                System.out.println("cur " + currentTurnId);
                                break;
                            }
                        }

                        sendReply(socket, "OK");
                    } else {
                        sendReply(socket, "NOT OK");
                    }
                } else {
                    sendReply(socket, "YOU NEED TO INSERT");
                }
            } else {
                sendReply(socket, "NOT YOUR TURN");
            }
        });

        /**
         * Client requests pathmove for a row to be validated, if it's a valid move sever makes the same move.
         */
        registerMethod(MessageType.INSERT_VALIDATE, (message, socket) -> {
            if(isItYourTurn(message.getSenderID())) {
                if(!allreadyInsertet) {
                    //res[Direction, Col, Row, ExtraCard-Rotation]
                    String[] res = ((String) message.get(1)).split(" ");

                    Triplet<String, Integer, Integer> pathMove = new Triplet<>(res[0], Integer.valueOf(res[1]), Integer.valueOf(res[2]));
                    field.getExtraCard().setRotation(Integer.parseInt(res[3]));
                    if (serverLogic.validatePathMove(pathMove)) {
                        if (res[0].equals("UP")) {
                            field.moveColumnUp(Integer.parseInt(res[1]));
                        }
                        if (res[0].equals("DOWN")) {
                            field.moveColumnDown(Integer.parseInt(res[1]));
                        }
                        if (res[0].equals("LEFT")) {
                            field.moveRowLeft(Integer.parseInt(res[2]));
                        }
                        if (res[0].equals("RIGHT")) {
                            field.moveRowRight(Integer.parseInt(res[2]));
                        }

                        broadcastMessage(new Datapackage(MessageType.COLROW_UPDATE, message.get(1)));

                        allreadyInsertet = true;

                        sendReply(socket, "OK");
                    } else {
                        sendReply(socket, "NOT OK");
                    }
                } else {
                    sendReply(socket, "YOU ALLREADY INSERTED");
                }
            } else {
                sendReply(socket, "NOT YOUR TURN");
            }
        });

        /**
         * Receive new CardStack from local player
         * Required for persistence which obtains all data from the server
         */
        registerMethod(MessageType.CARDSTACK_UPDATE, (message, socket) -> {
            Player p = getField().getPlayerBySenderId(message.getSenderID());

            p.setTreasureCards((CardStack)message.get(1));

            sendReply(socket, "OK");
        });



        /**
         * Receive pause request from client, broadcast pause to all clients
         * Sender ID will be used client-side to render resume button for the one who paused
         */
        registerMethod(MessageType.PAUSE, (message, socket) -> {
           broadcastMessage(new Datapackage(MessageType.PAUSE, message.getSenderID()));
           sendReply(socket, "OK");
        });

        /**
         * Receive resume request from client, broadcast resume to all clients
         */
        registerMethod(MessageType.RESUME, (message, socket) -> {
            broadcastMessage(new Datapackage(MessageType.RESUME, message.getSenderID()));
            sendReply(socket, "OK");
        });

        /**
         * Receive from client that he won the game and broadcast the winner id to all clients
         */
        registerMethod(MessageType.WINNER, (message, socket) -> {
            broadcastMessage(new Datapackage(MessageType.WINNER, message.getSenderID()));
            sendReply(socket, "OK");

            try{
                stop();
            } catch (IOException ignored){}
        });
    }

    /**
     * Set the server's local playing field
     * @param field Playing field
     */
    public void setField(final Field field) {
        this.field = field;
    }

    /**
     * Retrieve the server's local playing field
     * @return Playing field
     */
    public Field getField() {
        return field;
    }

    public boolean isItYourTurn(String id){
        if(id.equals(currentTurnId)){
            return true;
        }
        return false;
    }


    /**
     * Sets current GameClient turn id
     * @param currentTurnId Id of the GameClient whose turn it currently is
     */
    public void setCurrentTurnId(String currentTurnId) {
        this.currentTurnId = currentTurnId;
        broadcastMessage(new Datapackage(MessageType.TURN, currentTurnId));
    }

    /**
     * Retrieve current turn id
     * @return The id of the GameClient whose turn it currently is
     */
    public String getCurrentTurnId() {
        return currentTurnId;
    }

    /**
     * Retrieve the port the game server is listening on
     * @return The current server port
     */
    public int getPort() {
        return port;
    }

    /**
     * Set persistence load flag
     * @param isLoadedFromPersistence Persistence flag
     */
    public void setLoadedFromPersistence(final boolean isLoadedFromPersistence) {
        this.isLoadedFromPersistence = isLoadedFromPersistence;
    }

    /**
     * Get persistence load flag
     */
    public boolean getLoadedFromPersistence() {
        return isLoadedFromPersistence;
    }

}
