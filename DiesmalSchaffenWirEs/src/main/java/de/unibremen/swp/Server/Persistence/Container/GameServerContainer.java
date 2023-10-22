package de.unibremen.swp.Server.Persistence.Container;

import de.unibremen.swp.Game.Components.Field;
import de.unibremen.swp.Server.GameServer;
import org.javatuples.Triplet;

/**
 * GameServerContainer
 *
 * @author Sharma Katia, Safia Charif
 */
public class GameServerContainer {
    public FieldContainer field;
    public int port;
    public String currentTurnId = "";

    /**
     * Construct Container
     * @param gameServer Game server
     */
    public GameServerContainer(GameServer gameServer){
        field = new FieldContainer(gameServer.getField());
        currentTurnId = gameServer.getCurrentTurnId();
        port = gameServer.getPort();
    }

    /**
     * Returns the field of the GameServer
     * @return Field made from the FieldContainer
     */
    public Field getFieldContainer(){
        return field.getFieldFromContainer();
    }

    /**
     * Retrieve game server from container
     * @return The game server
     */
    public GameServer getGameServerFromContainer(){
        GameServer gameServer = new GameServer(port);
        gameServer.setField(getFieldContainer());
        gameServer.setCurrentTurnId(currentTurnId);
        gameServer.setLoadedFromPersistence(true);

        return gameServer;
    }
}
