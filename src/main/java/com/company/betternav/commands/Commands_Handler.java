package com.company.betternav.commands;

import com.company.betternav.events.NavBossBar;
import com.company.betternav.navigation.Goal;
import com.company.betternav.navigation.LocationWorld;
import com.company.betternav.navigation.PlayerGoal;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.ConfigYaml;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;
import java.util.*;

import static java.lang.String.valueOf;


/*
Command Handler for BetterNavigating Plugin
 */
public class Commands_Handler implements CommandExecutor {

    // class PlayerGoals to store the player's information and their goal (where to navigate)
    private final PlayerGoals playerGoals;

    // local path, where the files will need to be stored
    private final String path;

    // hashmap to hold the players with action bar enabled or not
    private HashMap<UUID, Boolean> actionbarplayers = new HashMap<>();

    // hashmap to keep track of players with their bossbar
    private HashMap<UUID, NavBossBar> bblist;

    // config file
    private final ConfigYaml config;


   /**
    * Makes a directory in the certain path if the directory doesn't exist
    */

    public void makeDirectory(String newPath){

        // Create missing folder at path
        File folder = new File(newPath);
        if (!folder.exists()) folder.mkdir();
    }

    /**
     *  Constructor for command handler
     *
     * @param playerGoals class
     * @param plugin, to get the path extracted
     *
     */
    public Commands_Handler(PlayerGoals playerGoals, JavaPlugin plugin,HashMap<UUID,Boolean> actionbarplayers,HashMap<UUID, NavBossBar> bblist)
    {
        this.playerGoals = playerGoals;
        this.actionbarplayers = actionbarplayers;
        this.config = new ConfigYaml(plugin);
        this.bblist = bblist;

        // File.separator to get correct separation, depending on OS
        this.path = plugin.getDataFolder().getAbsolutePath() + File.separator;


    }

    /**
     * To write the file that consists of the coordinates
     * @param name name of the location
     */
    public void writeFile(String name,Player player) {

        // initiale json parser Gson
        Gson json = new GsonBuilder().setPrettyPrinting().create();


        try {

            // if it does not exist: create missing folder Betternav
            makeDirectory(path);

            // read if private waypoints is enabled or not
            boolean privateWayPoints = config.getConfiguration().getBoolean("privateWayPoints");

            // get the worldname where the player is active
            String world = player.getWorld().getName();

            // attach the world path to the original path
            String worldPath = path+File.separator+world;

            // create missing folder world
            makeDirectory(worldPath);

            // put the world path equal to the player path
            String PlayerPath = worldPath;


            // if private waypoints is enabled, attach player information
            if(privateWayPoints){

                // get player uuid
                UUID uuid = player.getUniqueId();

                // get the uuid of the player
                String id = uuid.toString();

                // put the player path equal to the original path + uuid
                PlayerPath = worldPath+File.separator+id;

            }

            // use the shared directory
            else{

                // create/use the shared directory
                PlayerPath = PlayerPath+File.separator+"shared";
            }

            // create playerPath if it doesn't exist
            makeDirectory(PlayerPath);

            // get the maximum of waypoints in the configuration file
            int maximumWayPoints = config.getConfiguration().getInt("maximumWaypoints");

            // create new file string in the directory with filename: name
            String filename = PlayerPath+File.separator+name+".json";

            // create the file
            File directory = new File(PlayerPath);

            // check for the length of files in the directory
            int fileCount = directory.list().length;

            // check if the number of files
            if(fileCount<=maximumWayPoints){

                // get x and z location (string)
                int X_Coordinate = player.getLocation().getBlockX();
                int Y_Coordinate = player.getLocation().getBlockY();
                int Z_Coordinate = player.getLocation().getBlockZ();

                // create string value of locations (to send message later on)
                String X = valueOf(X_Coordinate);
                String Y = valueOf(Y_Coordinate);
                String Z = valueOf(Z_Coordinate);

                //make a filewriter
                FileWriter myWriter = new FileWriter(filename);

                // make map of coordinates and name to define it in json
                LocationWorld coordinate = new LocationWorld(world,name,Integer.parseInt(X),Integer.parseInt(Y),Integer.parseInt(Z));

                // write to Json file
                json.toJson(coordinate,myWriter);

                // close writer
                myWriter.close();

                // send player verification message
                player.sendMessage("§c§l(!) §c Location " + name + " saved on: X " + X + " Y "+Y+" Z " + Z);
            }

            else{

                // send player message if limit is reached
                player.sendMessage("Maximum amount of "+maximumWayPoints +" waypoints reached");
            }


        } catch (IOException e) {
            // send player message if error occurred
            player.sendMessage("An error occurred by writing a file for your coordinates");

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

    public LocationWorld readFile(String location,Player player) {

        // start a new json parser Gson
        Gson gson = new Gson();

        // get the world the player is active on
        String world = player.getWorld().getName();

        // get the uuid of the player who did the command
        String uuid = player.getUniqueId().toString();

        // make up the world path
        String worldPath = path+File.separator+world+File.separator;

        // get the config of privatewaypoins
        boolean privateWayPoints = config.getConfiguration().getBoolean("privateWayPoints");

        // if it is enabled: PlayerPath will be needed the uuid of the player
        if(privateWayPoints){

            // add the uuid of the player
            String playerPath = worldPath+uuid;

            // make up the world path out of the player path
            worldPath = playerPath;
        }

        else{

            //create shared directory
            worldPath = worldPath+File.separator+"shared";
        }

        // make a directory of the world path
        makeDirectory(worldPath);

        // create a string of the path of the file to be read
        String readPath = worldPath+File.separator+location+".json";

        // try to read the file (if exists)
        try (Reader reader = new FileReader(readPath)) {

            // Convert JSON File to Java Object
            LocationWorld location_coordinates = gson.fromJson(reader, LocationWorld.class);

            // return the class
            return location_coordinates;

        } catch (IOException e) {

            // send player error message if the waypoint couldn't be found
            player.sendMessage("Could not find waypoint "+location +", maybe you mean navplayer <player>?");
            return null;

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
    public boolean deleteFile(String location,Player player){

        // get the player id and world
        String id = player.getUniqueId().toString();
        String world = player.getWorld().getName();

        // check if the waypoints are private or not
        boolean privateWayPoints = config.getConfiguration().getBoolean("privateWayPoints");


        // add the world name to the file path
        String readPath = path+File.separator+world+File.separator;


        // if private enabled, add uuid of player
        if(privateWayPoints){
            readPath = readPath + id + File.separator;
        }

        else{

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
        if(file.delete()){

            return true;

        }else {

            return false;

        }

    }

    /**
     *
     * @param sender sender of the command
     * @param cmd commands
     * @param s message
     * @param args arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        // check if a player was the sender of the command
        if (!(sender instanceof Player)) {
            sender.sendMessage("only players can use that command");

            return true;
        }

        Player player = (Player) sender;

        // shows up information about the plugin
        if (cmd.getName().equalsIgnoreCase("bn")) {

            String command = ChatColor.RED+"/getlocation";
            String command_1 = ChatColor.WHITE+" or";
            String command_11 = ChatColor.RED+" /toggle";
            String explanation = ChatColor.GREEN+" shows or hides your current location";

            String command2 = ChatColor.RED+"/savelocation <location>";
            String command_2 = ChatColor.WHITE+" or";
            String command_21 = ChatColor.RED+" /save";
            String explanation2 = ChatColor.GREEN+" saves waypoint";

            String command3 = ChatColor.RED+"/showlocations";
            String command_3 = ChatColor.WHITE+" or";
            String command_31 = ChatColor.RED+" /showpossiblelocations";
            String explanation3 = ChatColor.GREEN+" shows list of all saved locations";

            String command4 = ChatColor.RED+"/showcoordinates <location>";
            String command_4 = ChatColor.WHITE+" or";
            String command_41 = ChatColor.RED+" /getcoordinates <location>";
            String explanation4 = ChatColor.GREEN+" shows coordinates of saved location";

            String command5 = ChatColor.RED+"/del <location>";
            String explanation5 = ChatColor.GREEN+" deletes a location";


            String command6 = ChatColor.RED+ "/nav <location>";
            String command_6 = ChatColor.WHITE+" or";
            String command_61 = ChatColor.RED+" /goto <location>";
            String explanation6 = ChatColor.GREEN+" start navigation to location";


            String command7 = ChatColor.RED+" /navplayer <player>";
            String explanation7 = ChatColor.GREEN+" start navigating to player";

            String command8 = ChatColor.RED+" /stopnav";
            String explanation8 = ChatColor.GREEN+" stop navigation";


            player.sendMessage(command+command_1+command_11 + explanation);
            player.sendMessage(command2+command_2+command_21+explanation2);
            player.sendMessage(command3+command_3+command_31+explanation3);
            player.sendMessage(command4+command_4+command_41+explanation4);
            player.sendMessage(command5+explanation5);
            player.sendMessage(command6+command_6+command_61+explanation6);
            player.sendMessage(command7+explanation7);
            player.sendMessage(command8+explanation8);

            return true;
        }

        // get current location of the player
        else if (cmd.getName().equalsIgnoreCase("getlocation")) {

            // get the UUID of the player
            UUID PlayersUUID = player.getUniqueId();

            // check if player is on list
            if(actionbarplayers.containsKey(PlayersUUID)){

            }
            else{
                // put player on the list
                actionbarplayers.put(PlayersUUID,false);
            }

            // get the boolean
            boolean en = actionbarplayers.get(PlayersUUID);

            // toggle the boolean
            en = !en;

            // set the toggled value
            actionbarplayers.remove(PlayersUUID);
            actionbarplayers.put(PlayersUUID,en);

        }


        // get list of locations
        else if (cmd.getName().equalsIgnoreCase("showlocations")) {


            player.sendMessage("saved locations: ");

            String id = player.getUniqueId().toString();
            String world = player.getWorld().getName();


            String readPath = path+File.separator+world+File.separator;
            boolean privateWayPoints = config.getConfiguration().getBoolean("privateWayPoints");
            if(privateWayPoints){
                readPath = readPath+id+File.separator;

            }
            else{
                //create shared directory
                readPath = readPath+File.separator+"shared";
            }



            File folder = new File(readPath);
            File[] listOfFiles = folder.listFiles();

            if(listOfFiles==null){
                player.sendMessage("There are no saved locations.");
                return true;
            }

            if (listOfFiles.length==0){
                player.sendMessage("There are no saved locations.");
            }

            else{
                for (File file : listOfFiles) {
                    if (file.isFile()) {

                        // get full filename of file
                        String[] fileName = file.getName().split(".json");

                        // location name will be first part
                        String location = fileName[0];
                        //System.out.println(file.getName());

                        //send message to the player
                        player.sendMessage("§c§l - §c " + location);

                    }
                }
            }


        }


        // save current location of the player
        else if (cmd.getName().equalsIgnoreCase("savelocation")) {

            // if location provided
            if (args.length == 1) {
                try {
                    String location = args[0];

                    writeFile(location,player);


                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            } else if (args.length > 1) {
                try {
                    String location = args[0];
                    String X = args[1];
                    String Z = args[2];

                    player.sendMessage("§c§l(!) §c Location " + location + " saved on: " + X + " " + Z);
                    writeFile(location,player);


                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }

            } else {
                player.sendMessage("§c§l(!) §cusage ");
                player.sendMessage("§c§l(!) §c/savelocation <name of your location> ");
                player.sendMessage("§c§l(!) §c/savelocation <name of your location> <X coordinate> <Z coordinate> ");
            }
        }

        else if(cmd.getName().equalsIgnoreCase("del")){
            // if location to delete provided
            if (args.length == 1) {
                try {
                    String location = args[0];
                    boolean deleted = deleteFile(location,player);
                    if(deleted){
                        player.sendMessage(location+" is deleted");
                    }
                    else{
                        player.sendMessage("Could not delete location "+location);
                    }

                }catch (IllegalArgumentException e){
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }

            }

        }

        // show coordinates of saved location
        else if (cmd.getName().equalsIgnoreCase("showcoordinates")) {

            // if location provided
            if (args.length == 1) {
                try {

                    // the location needed
                    String location = args[0];

                    //read coordinates out of file
                    LocationWorld coordinates = readFile(location,player);

                    if(coordinates==null){
                        player.sendMessage("/bn to get information about how to use bn commands");
                        return true;
                    }

                    //send coordinates to the player
                    player.sendMessage(coordinates.getName()+ " has coordinates X: "+String.valueOf(coordinates.getX())+ " and Z: "+String.valueOf(coordinates.getZ()));


                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            }

        }

        //navigate to location
        // show coordinates of saved location
        else if (cmd.getName().equalsIgnoreCase("nav")) {


            // if location provided
            if (args.length == 1) {
                try {

                    // get the UUID of the player
                    UUID PlayersUUID = player.getUniqueId();


                    // get the location needed
                    String location = args[0];

                    // read coordinates out of file
                    LocationWorld coordinates = readFile(location,player);

                    // error handling when location is wrong
                    if(coordinates==null){
                        player.sendMessage("/bn to get information about how to use bn commands");
                        return true;
                    }

                    //get coordinates to the goal
                    String goal = coordinates.getName();
                    double x = coordinates.getX();
                    double z = coordinates.getZ();

                    Goal playerGoal = new Goal( goal, new Location( Bukkit.getWorld("world"), x, 0, z ) );

                    player.sendMessage("Navigating to "+goal);
                    player.sendMessage("Navigating to "+x+" "+z);

                    this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            }

        }

        // show coordinates of saved location
        else if (cmd.getName().equalsIgnoreCase("navplayer")) {


            // if location provided
            if (args.length == 1) {
                try {

                    // get the UUID of the player
                    UUID PlayersUUID = player.getUniqueId();


                    // get the location needed
                    String playerName = args[0];

                    Player navto = Bukkit.getPlayer(playerName);

                    if(navto==null){
                        player.sendMessage("Could not find player "+playerName);
                        return true;
                    }

                    //get coordinates to the goal
                    String goal = playerName;



                    PlayerGoal playerGoal = new PlayerGoal( goal, navto);

                    player.sendMessage("Navigating to "+goal);


                    this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            }

        }

        // stop navigation
        else if (cmd.getName().equalsIgnoreCase("stopnav")) {


            try {

                // delete player at navigating people
                this.playerGoals.removePlayerGoal(player.getUniqueId());

                // delete the bossbar
                NavBossBar delbb = bblist.remove(player.getUniqueId());
                delbb.delete(player);

                // remove the bar of the list
                bblist.remove(player.getUniqueId());

                // set locationname in different color
                String endMessage = ChatColor.LIGHT_PURPLE + "ending navigation";

                // send player the message
                player.sendMessage(endMessage);
            }catch (Exception e){
                player.sendMessage("Cannot end navigation");
            }




        }

        else{
            return false;
        }


        return true;

    }


}







