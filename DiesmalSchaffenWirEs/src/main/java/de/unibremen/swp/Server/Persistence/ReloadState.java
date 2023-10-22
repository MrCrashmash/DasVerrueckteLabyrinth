package de.unibremen.swp.Server.Persistence;

import com.google.gson.Gson;
import de.unibremen.swp.Server.GameServer;
import de.unibremen.swp.Server.Persistence.Container.GameServerContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


/**
 * ReloadState
 * enables loading persistent game state
 *
 * @author Sharma Katia, Safia Charif
 */
public class ReloadState {

    /**
     * Returns a GameServer Object from a given GameserverContainer as json
     * @param file the .json containing the GameserverContainer
     * @return a GameServer Object
     * @throws FileNotFoundException should be handled by the caller
     */
    public GameServer containerToGameServer(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fileReader = new FileReader(file);
        GameServerContainer gameServerContainer = gson.fromJson(fileReader,GameServerContainer.class);

        return gameServerContainer.getGameServerFromContainer();
    }
}
