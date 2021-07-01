package it.polimi.ingsw.utilities;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Singleton used for storing the network configuration
 */
public class NetworkConfiguration {
    private static transient NetworkConfiguration instance = null;

    private int PORT;

    /**
     * Constructor for the class
     * Sets the port at a default value
     */
    private NetworkConfiguration(){
        PORT = 50885;
    }

    /**
     * Loads the network configuration from "NetworkConfiguration.json". If it fails, it will load the default configuration
     * @return the loaded configuration
     */
    private static NetworkConfiguration loadFromJSON() {
        Gson gson = new Gson();
        try {
            String JSON = Files.readString(Paths.get("src/main/resources/NetworkConfiguration.json"));
            return  gson.fromJson(JSON, NetworkConfiguration.class);
        } catch (Exception e) {
            System.err.println("Unable to load network configuration from file. Loading default configuration");
            return new NetworkConfiguration();
        }
    }

    /**
     * Getter for the instance of NetworkConfiguration
     * @return The instance of NetworkConfiguration
     */
    public static NetworkConfiguration getInstance(){
        if(instance==null) instance = loadFromJSON();
        return instance;
    }

    /**
     * Getter for the server port
     * @return The server port
     */
    public int getPORT() {
        return PORT;
    }
}
