package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Player;

/**
 * PlayerContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class PlayerContainer {

    public String id;
    public FigurContainer figure;
    public int score;

    public CardsStackContainer treasureCards;

    /**
     * Construct Container
     * @param player The player
     */
    public PlayerContainer(Player player){
        id = player.getId();
        figure = new FigurContainer(player.getFigure());
        score = player.getScore();
        treasureCards = new CardsStackContainer(player.getTreasureCards());
    }

    /**
     * Returns the Player
     * @return Player made from the Container
     */
    public Player getPlayerFromContainer() {
        Player player = new Player(id,figure.getFigureFromContainer(),treasureCards.getCardStackFromContainer());
        player.setScore(score);
        return player;
    }
}
