package de.unibremen.swp.Server.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.unibremen.swp.Server.GameServer;
import de.unibremen.swp.Server.Persistence.Container.GameServerContainer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * SaveState
 * enables persistent game state saving
 *
 * @author Sharma Katia, Safia Charif
 */
public class SaveState {
    /**
     * Saves a GameServer as an instance of GameServerContainer as Json.
     * Reading should be done with ReloadState.containerToGameServer().
     * The Field, Port and the current turn are saved and can be recreated.
     * @param gameServer to be saved
     * @param file desired location for the .json
     * @throws IOException Should be handled by the caller
     */
    public void saveGameServer(GameServer gameServer, File file) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        FileWriter fileWriter = new FileWriter(file);
        gson.toJson(new GameServerContainer(gameServer), fileWriter);
        fileWriter.close();
    }








}