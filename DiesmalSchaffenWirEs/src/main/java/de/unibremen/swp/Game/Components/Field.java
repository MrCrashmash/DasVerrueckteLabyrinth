package de.unibremen.swp.Game.Components;

import de.unibremen.swp.Game.Components.Cards.PathCard;
import java.awt.*;
import java.io.Serializable;

import org.javatuples.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Field
 *
 * Object that holds all game tiles,
 * so called PathCards
 *
 * @author Mut Daniel, Zimmermann Henning,
 *         Drescher Lennart, Sharma Katia, Safia Charif
 */
public class Field implements Serializable {
    private List<List<PathCard>> pathCardPositions;
    private PathCard extraCard;
    private PathCard currentSelection;

    private static Triplet<String, Integer, Integer> forbiddenPathMove = new Triplet("NONE", -1, -1);
    private List<Player> playerList = new ArrayList<>(2);

    /**
     * Construct field
     * @param pathCardPositions PathCards
     * @param extraCard Extra card
     */
    public Field(final List<List<PathCard>> pathCardPositions, final PathCard extraCard) {
        this.pathCardPositions = pathCardPositions;
        this.extraCard = extraCard;
    }

    /**
     * Set currently selected card
     * @param currentSelection PathCard
     */
    public void setSelection(final PathCard currentSelection) {
        this.currentSelection = currentSelection;
    }

    /**
     * Retrieve currently selected card
     * @return PathCard
     */
    public PathCard getSelection() {
        return currentSelection;
    }

    /**
     * Retrieve list of all PathCards
     * @return PathCards
     */
    public List<List<PathCard>> getPathCardPositions() {
        return pathCardPositions;
    }

    /**
     * Retrieve extra card
     * @return Extra card
     */
    public PathCard getExtraCard() {
        return extraCard;
    }

    /**
     * Retrieve a PathCard from the field
     * @param x Its x coordinate
     * @param y Its y coordinate
     * @return The respective PathCard
     */
    public PathCard getPathCard(int x, int y){
        return pathCardPositions.get(x).get(y);
    }

    /**
     * Set a PathCard in the field
     * @param pathCard A PathCard
     * @param x Its x coordinate
     * @param y Its y coordinate
     */
    private void setPathCard(PathCard pathCard, int x, int y){
        pathCardPositions.get(x).set(y, pathCard);
    }

    /**
     * Set forbidden PathCard insertion move
     * @param forbiddenPathMove Forbidden insertion move
     */
    public static void setForbiddenPathMove(Triplet<String,Integer,Integer> forbiddenPathMove){
        Field.forbiddenPathMove = forbiddenPathMove;
    }

    /**
     * Retrieve player list
     * @return A list of all players
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Set player list
     * @param playerList A list of all players
     */
    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * Retrieve forbidden insertion move
     * @return Forbidden insertion move
     */
    public static Triplet<String, Integer, Integer> getForbiddenPathMove(){
        return forbiddenPathMove;
    }

    /**
     * Move player object to a specific PathCard
     * @param senderId The GameClient id
     * @param destination The destination PathCard
     */
    public void movePlayer(final String senderId, final PathCard destination) {
        Player player = getPlayerBySenderId(senderId);

        player.getFigure().getCurrentPathCard().remove(player.getFigure().getSprite());
        // destination position for player
        destination.getSprite().add(player.getFigure().getSprite(), new GridBagConstraints());
        // currentPathCard ändern
        player.getFigure().setCurrentPathCard(destination);
    }


    /**
     * Move PathCard row to the right
     *
     * @param y Y-Value
     */
    public void moveRowRight(int y) {
        PathCard old = getPathCard(pathCardPositions.size() - 1, y);
        for (int i = pathCardPositions.size() - 2; i >= 0; i--) {
            setPathCard(getPathCard(i, y), i + 1, y);
            //hier koordinaten ändern
        }
        setPathCard(extraCard, 0, y);
        // Falls der/die Spieler vom Brett geschoben würde.
        for (Player player : playerList) {
            if (player.getFigure().getCurrentPathCard() == old) {
                // remove player from current pathcard
                player.getFigure().getCurrentPathCard().remove(player.getFigure().getSprite());
                // destination position for player
                getPathCard(0, y).getSprite().add(player.getFigure().getSprite(), new GridBagConstraints());
                // currentPathCard ändern
                player.getFigure().setCurrentPathCard(getPathCard(0, y));
            }
        }
        extraCard = old;
        forbiddenPathMove = new Triplet<>("LEFT", -1, y);
        extraCard.revalidate();
        extraCard.repaint();
    }


    /**
     * Move PathCard row to the left
     *
     * @param y Y-Value
     */
    public void moveRowLeft(int y){
        PathCard old = getPathCard(0, y);
        for (int i = 1; i <= pathCardPositions.size()-1; i++) {
            setPathCard(getPathCard(i, y), i - 1, y);
        }
        setPathCard(extraCard, pathCardPositions.size()-1, y);
        // Falls der/die Spieler vom Brett geschoben würde.
        for (Player player : playerList){
            if(player.getFigure().getCurrentPathCard() == old) {
                // remove player from current pathcard
                player.getFigure().getCurrentPathCard().remove(player.getFigure().getSprite());
                // destination position for player
                getPathCard(pathCardPositions.size()-1, y).getSprite().add(player.getFigure().getSprite(), new GridBagConstraints());
                // currentPathCard ändern
                player.getFigure().setCurrentPathCard(getPathCard(pathCardPositions.size()-1, y));
            }
        }
        extraCard = old;
        forbiddenPathMove = new Triplet<>("RIGHT", -1, y);
    }

    /**
     * Move PathCard column downwards
     *
     * @param x X-Value
     */
    public void moveColumnDown( int x){
        PathCard old = getPathCard(x,pathCardPositions.size()-1);
        pathCardPositions.get(x).add(0,extraCard);
        // Falls der/die Spieler vom Brett geschoben würde.
        for (Player player : playerList){
            if(player.getFigure().getCurrentPathCard() == old) {
                // remove player from current pathcard
                player.getFigure().getCurrentPathCard().remove(player.getFigure().getSprite());
                // destination position for player
                getPathCard(x, 0).getSprite().add(player.getFigure().getSprite(), new GridBagConstraints());
                // currentPathCard ändern
                //player.getFigure().setCurrentPathCard(getPathCard(x, 0));
                player.getFigure().setCurrentPathCard(extraCard);
            }
        }
        extraCard = pathCardPositions.get(x).remove(pathCardPositions.get(x).size()-1);
        forbiddenPathMove = new Triplet<>("UP", x, -1);
    }

    /**
     * Move PathCard column upwards
     *
     * @param x X-Value
     */
    public void moveColumnUp( int x){
        PathCard old = getPathCard(x,0);
        pathCardPositions.get(x).add(extraCard);
        // Falls der/die Spieler vom Brett geschoben würde.
        for (Player player : playerList){
            if(player.getFigure().getCurrentPathCard() == old) {
                // remove player from current pathcard
                player.getFigure().getCurrentPathCard().remove(player.getFigure().getSprite());
                // destination position for player
                getPathCard(x, pathCardPositions.get(x).size()-1).getSprite().add(player.getFigure().getSprite(), new GridBagConstraints());
                // currentPathCard ändern
                //player.getFigure().setCurrentPathCard(getPathCard(x, 0));
                player.getFigure().setCurrentPathCard(extraCard);
            }
        }
        extraCard = pathCardPositions.get(x).remove(0);
        forbiddenPathMove = new Triplet<>("DOWN", x, -1);
    }

    /**
     * Finds the exact coordinates of a PathCard within the field list
     * @param card The respective card
     * @return Array [x, y] denoting a PathCard's coordinates within the grid
     */
    public int[] getIndices(final PathCard card) {
        int[] result = new int[2];
        int x = -1;
        for(List<PathCard> cols : this.getPathCardPositions()) {
            int y = -1;
            x++;
            for(PathCard row : cols) {
                y++;
                if(row.getId() == card.getId()) {
                    result[0] = x;
                    result[1] = y;
                    return result;
                }
            }
        }
        throw new IllegalArgumentException("Card wasn't found");
    }

    /**
     * Retrieve player object by GameClient id
     * @param id GameClient id
     * @return Player object
     */
    public Player getPlayerBySenderId(final String id) {
        for(Player player : getPlayerList()) {
            if(player.getId().equals(id)) {
                return player;
            }
        }
        throw new IllegalArgumentException("No such player found!");
    }

    /**
     * Stringify field
     * @return
     */
    @Override
    public String toString(){
        StringBuilder ausgabeString = new StringBuilder();
        for (List<PathCard> l:pathCardPositions) {
            ausgabeString.append("\n");
            for (PathCard p:l) {
                ausgabeString.append(" ").append(p);
            }
        }
        return  ausgabeString.toString();
    }

    /**
     * Equality override
     * @param obj Field
     * @return Whether or not they're equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Field){
            return this.toString().equals(obj.toString())
                    && this.extraCard.equals(((Field) obj).extraCard)
                    && this.playerList.equals(((Field) obj).playerList);
        } else {
            return false;
        }
    }
}

