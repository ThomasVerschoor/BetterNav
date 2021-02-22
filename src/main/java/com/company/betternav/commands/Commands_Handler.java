package com.company.betternav.commands;

import com.company.betternav.Goal;
import com.company.betternav.LocationWorld;
import com.company.betternav.PlayerGoals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
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
     */
    public Commands_Handler(PlayerGoals playerGoals, JavaPlugin plugin,HashMap<UUID,Boolean> actionbarplayers)
    {
        this.playerGoals = playerGoals;

        // File.separator to get correct separation, depending on OS
        this.path = plugin.getDataFolder().getAbsolutePath() + File.separator;

        this.actionbarplayers = actionbarplayers;

    }

    /**
     * To write the file that consists of the coordinates
     * @param name name of the location
     * @param X x coordinate to write
     * @param Z z coordinate to write
     */
    public void writeFile(String name, String X, String Z,Player player) {

        Gson json = new GsonBuilder().setPrettyPrinting().create();

        try {



            //System.out.println(path + name + ".json");

            // Create missing folder Betternav
            makeDirectory(path);

            String world = player.getWorld().getName();
            String worldPath = path+File.separator+world;

            // create missing folder world
            makeDirectory(worldPath);

            //get player uuid
            UUID uuid = player.getUniqueId();
            String id = uuid.toString();

            String PlayerPath = worldPath+File.separator+id;

            //System.out.println(path);
            //System.out.println(newPath);

            makeDirectory(PlayerPath);

            String filename = PlayerPath+File.separator+name+".json";
            //write new file
            FileWriter myWriter = new FileWriter(filename);
            //System.out.println(filename);


            // make map of coordinates and name to define it in json
            LocationWorld coordinate = new LocationWorld(world,name,Integer.parseInt(X),0,Integer.parseInt(Z));

            //write to Json file
            json.toJson(coordinate,myWriter);

            //close writer
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred by writing a file for your coordinates");
            e.printStackTrace();
        }

    }

    // to read a file
    public LocationWorld readFile(String location,Player player) {


        // get arraylist for coordinates
        //ArrayList<String> locations = new ArrayList<String>();

        Gson gson = new Gson();

        String world = player.getWorld().getName();
        String uuid = player.getUniqueId().toString();


        try (Reader reader = new FileReader(path+File.separator+world+File.separator+uuid+File.separator+location + ".json")) {

            // Convert JSON File to Java Object
            LocationWorld location_coordinates = gson.fromJson(reader, LocationWorld.class);
            return location_coordinates;

        } catch (IOException e) {
            e.printStackTrace();
        }

     return null;

    }

    public boolean deleteFile(String location,Player player){

        String id = player.getUniqueId().toString();
        String world = player.getWorld().getName();

        // create new file object
        File file = new File(path+File.separator+world+File.separator+id+File.separator+location+".json");

        if(file.delete()){

            return true;
        }else {
            return false;
        }


    }





    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("only players can use that command");

            return true;
        }

        Player player = (Player) sender;

        // shows up information about the plugin
        if (cmd.getName().equalsIgnoreCase("bn")) {

            player.sendMessage("Helpfile");
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

            /*

            // get x and z location (string)
            int X_Coordinate = player.getLocation().getBlockX();
            int Y_Coordinate = player.getLocation().getBlockY();
            int Z_Coordinate = player.getLocation().getBlockZ();

            String X = valueOf(X_Coordinate);
            String Y = valueOf(Y_Coordinate);
            String Z = valueOf(Z_Coordinate);

            //player.sendMessage("Your current location is X " + X + " Z " + Z);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("X "+X +" Y "+ Y + " Z " + Z));

            */
        }


        // get list of locations
        else if (cmd.getName().equalsIgnoreCase("showlocation")) {


            player.sendMessage("saved locations: ");

            String id = player.getUniqueId().toString();
            String world = player.getWorld().getName();

            File folder = new File(path+File.separator+world+File.separator+id+File.separator);
            File[] listOfFiles = folder.listFiles();

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

                    // get x and z location (string)
                    int X_Coordinate = player.getLocation().getBlockX();
                    int Y_Coordinate = player.getLocation().getBlockY();
                    int Z_Coordinate = player.getLocation().getBlockZ();

                    String X = valueOf(X_Coordinate);
                    String Y = valueOf(Y_Coordinate);
                    String Z = valueOf(Z_Coordinate);

                    player.sendMessage("§c§l(!) §c Location " + location + " saved on: " + X + " " + Z);
                    writeFile(location, X, Z,player);


                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            } else if (args.length > 1) {
                try {
                    String location = args[0];
                    String X = args[1];
                    String Z = args[2];

                    player.sendMessage("§c§l(!) §c Location " + location + " saved on: " + X + " " + Z);
                    writeFile(location, X, Z,player);


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

                    //send coordinates to the player
                    player.sendMessage(coordinates.getName());
                    player.sendMessage(String.valueOf(coordinates.getX()));
                    player.sendMessage(String.valueOf(coordinates.getZ()));



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

                    // get world of the player
                    String world = player.getWorld().getName();


                    // get the location needed
                    String playerName = args[0];

                    Player navto = Bukkit.getPlayer(playerName);

                    //get coordinates to the goal
                    String goal = playerName;

                    double x = navto.getLocation().getX();
                    double z = navto.getLocation().getZ();



                    Goal playerGoal = new Goal( goal, new Location( Bukkit.getWorld(world), x, 0, z ) );

                    player.sendMessage("Navigating to "+goal);
                    player.sendMessage("Navigating to "+x+" "+z);

                    this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            }

        }

        else{
            return false;
        }


        return true;

    }


}







