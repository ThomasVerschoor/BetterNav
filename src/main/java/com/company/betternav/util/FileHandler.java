package com.company.betternav.util;

import com.company.betternav.navigation.Goal;
import com.company.betternav.navigation.LocationWorld;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import static java.lang.String.valueOf;

public class FileHandler
{

    // local path, where the files will need to be stored
    private final String path;
    private final YamlConfiguration config;
    private final Map<String,String> messages;

    public FileHandler(JavaPlugin plugin, YamlConfiguration config, Map<String,String> messages)
    {
        // File.separator to get correct separation, depending on OS
        this.path = plugin.getDataFolder().getAbsolutePath() + File.separator;
        this.config = config;
        this.messages = messages;
    }

    public String getPath()
    {
        return this.path;
    }

    /**
     * Makes a directory in the certain path if the directory doesn't exist
     */
    public void makeDirectory(String newPath)
    {
        // Create missing folder at path
        File folder = new File(newPath);
        if (!folder.exists()) folder.mkdir();
    }


    /**
     * To write the file that consists of the coordinates
     * @param player player who did the command
     * @param playerGoal the name of the goal and Location location
     */

    public void writeLocationFile(Player player, Goal playerGoal)
    {
        // initiate json parser Gson
        Gson json = new GsonBuilder().setPrettyPrinting().create();

        try
        {

            // if it does not exist: create missing folder Betternav
            makeDirectory(path);

            // read if private waypoints is enabled or not
            boolean privateWayPoints = config.getBoolean("privateWayPoints");

            // get the worldname where the player is active
            String world = player.getWorld().getName();

            // attach the world path to the original path
            String worldPath = path+File.separator+world;

            // create missing folder world
            makeDirectory(worldPath);

            // put the world path equal to the player path
            String PlayerPath = worldPath;


            // if private waypoints is enabled, attach player information
            if(privateWayPoints)
            {

                // get player uuid
                UUID uuid = player.getUniqueId();

                // get the uuid of the player
                String id = uuid.toString();

                // put the player path equal to the original path + uuid
                PlayerPath = worldPath+File.separator+id;

            }

            // use the shared directory
            else
            {

                // create/use the shared directory
                PlayerPath = PlayerPath+File.separator+"shared";
            }

            // create playerPath if it doesn't exist
            makeDirectory(PlayerPath);

            // get the maximum of waypoints in the configuration file
            int maximumWayPoints = config.getInt("maximumWaypoints");

            // create new file string in the directory with filename: name
            String filename = PlayerPath+File.separator+playerGoal.getName()+".json";

            // create the file
            File directory = new File(PlayerPath);

            // check for the length of files in the directory
            int fileCount = directory.list().length;

            // check if the number of files
            if(fileCount<=maximumWayPoints)
            {

                // get x and z location (string)
                int X_Coordinate = playerGoal.getLocation().getBlockX();
                int Y_Coordinate = playerGoal.getLocation().getBlockY();
                int Z_Coordinate = playerGoal.getLocation().getBlockZ();

                // create string value of locations (to send message later on)
                String X = valueOf(X_Coordinate);
                String Y = valueOf(Y_Coordinate);
                String Z = valueOf(Z_Coordinate);

                //make a filewriter
                FileWriter myWriter = new FileWriter(filename);

                // make map of coordinates and name to define it in json
                LocationWorld coordinate = new LocationWorld(world,playerGoal.getName(),Integer.parseInt(X),Integer.parseInt(Y),Integer.parseInt(Z));

                // write to Json file
                json.toJson(coordinate,myWriter);

                // close writer
                myWriter.close();

                // send player verification message
                String locsaved = messages.getOrDefault("location_saved", "§c§l(!) §c Location <location> saved on: X: " + X + " Y: "+Y+" Z: " + Z);

                // append data to location save command
                locsaved = locsaved +" X:"+X+" Y: "+Y+" Z: "+Z;
                player.sendMessage(locsaved.replace("<location>", playerGoal.getName()));
            }

            else
            {

                // send player message if limit is reached
                String message = messages.getOrDefault("maximum_amount", "Maximum amount of <number> waypoints reached");
                player.sendMessage(message.replace("<number>",String.valueOf(maximumWayPoints)));
            }


        }
        catch (IOException e)
        {
            // send player message if error occurred
            player.sendMessage(messages.getOrDefault("error_saving", "An error occurred by writing a file for your coordinates"));
        }
    }

    /**
     *
     * To delete a waypoint
     *
     * @param location the location to be deleted
     * @param player the player who did execute the deletion
     * @return boolean if the file is gone
     */
    public boolean deleteFile(String location,Player player)
    {

        // get the player id and world
        String id = player.getUniqueId().toString();
        String world = player.getWorld().getName();

        // check if the waypoints are private or not
        boolean privateWayPoints = config.getBoolean("privateWayPoints");


        // add the world name to the file path
        String readPath = path+File.separator+world+File.separator;


        // if private enabled, add uuid of player
        if(privateWayPoints)
        {
            readPath = readPath + id + File.separator;
        }

        else
        {
            //use shared directory
            readPath = readPath+"shared"+File.separator;
        }

        // create the directory
        makeDirectory(readPath);

        // create the full path to be read
        readPath = readPath + location+".json";

        // create new file object
        File file = new File(readPath);

        // if the file is deleted
        if(file.delete())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * Used to read a file
     *
     * @param location the location the player wants to have
     * @param player the player who did the action
     * @return object of class LocationWorld
     */

    public LocationWorld readFile(String location,Player player)
    {
        // start a new json parser Gson
        Gson gson = new Gson();

        // get the world the player is active on
        String world = player.getWorld().getName();

        // get the uuid of the player who did the command
        String uuid = player.getUniqueId().toString();

        // make up the world path
        String worldPath = path+File.separator+world+File.separator;

        // get the config of privatewaypoins
        boolean privateWayPoints = config.getBoolean("privateWayPoints");

        // if it is enabled: PlayerPath will be needed the uuid of the player
        if(privateWayPoints)
        {
            // add the uuid of the player
            String playerPath = worldPath+uuid;

            // make up the world path out of the player path
            worldPath = playerPath;
        }

        else
        {
            //create shared directory
            worldPath = worldPath+File.separator+"shared";
        }

        // make a directory of the world path
        makeDirectory(worldPath);

        // create a string of the path of the file to be read
        String readPath = worldPath+File.separator+location+".json";

        // try to read the file (if exists)
        try (Reader reader = new FileReader(readPath))
        {

            // Convert JSON File to Java Object
            // return the class
            return gson.fromJson(reader, LocationWorld.class);

        }
        catch (IOException e)
        {

            // send player error message if the waypoint couldn't be found
            String message = messages.getOrDefault("error_navplayer", "Could not find waypoint <location>, maybe you mean navplayer <player>?");
            player.sendMessage(message.replace("<location>",location));
            return null;

        }

    }
}
