package com.company.betternav.commands;

import com.company.betternav.Goal;
import com.company.betternav.LocationWorld;
import com.company.betternav.PlayerGoals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
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

    /**
     *  Constructor for command handler
     *
     * @param playerGoals class
     * @param plugin, to get the path extracted
     */
    public Commands_Handler(PlayerGoals playerGoals, JavaPlugin plugin)
    {
        this.playerGoals = playerGoals;

        // File.separator to get correct separation, depending on OS
        this.path = plugin.getDataFolder().getAbsolutePath() + File.separator;

    }

    /**
     * To write the file that consists of the coordinates
     * @param name name of the location
     * @param X x coordinate to write
     * @param Z z coordinate to write
     */
    public void writeFile(String name, String X, String Z) {

        Gson json = new GsonBuilder().setPrettyPrinting().create();

        try {
            //write new file
            FileWriter myWriter = new FileWriter(path + name + ".json");

            // make map of coordinates and name to define it in json
            LocationWorld coordinate = new LocationWorld("world",name,Integer.parseInt(X),0,Integer.parseInt(Z));

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
    public LocationWorld readFile(String location) {


        // get arraylist for coordinates
        //ArrayList<String> locations = new ArrayList<String>();

        Gson gson = new Gson();

        try (Reader reader = new FileReader(path+location + ".json")) {

            // Convert JSON File to Java Object
            LocationWorld location_coordinates = gson.fromJson(reader, LocationWorld.class);
            return location_coordinates;

        } catch (IOException e) {
            e.printStackTrace();
        }

     return null;

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

            // get x and z location (string)
            int X_Coordinate = player.getLocation().getBlockX();
            int Z_Coordinate = player.getLocation().getBlockZ();

            String X = valueOf(X_Coordinate);
            String Z = valueOf(Z_Coordinate);

            player.sendMessage("Your current location is X " + X + " Z " + Z);
        }

        // get list of locations
        else if (cmd.getName().equalsIgnoreCase("showlocation")) {


            player.sendMessage("saved locations: ");

            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            assert listOfFiles != null;
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


        // save current location of the player
        else if (cmd.getName().equalsIgnoreCase("savelocation")) {

            // if location provided
            if (args.length == 1) {
                try {
                    String location = args[0];

                    // get x and z location (string)
                    int X_Coordinate = player.getLocation().getBlockX();
                    int Z_Coordinate = player.getLocation().getBlockZ();

                    String X = valueOf(X_Coordinate);
                    String Z = valueOf(Z_Coordinate);

                    player.sendMessage("§c§l(!) §c Location " + location + " saved on: " + X + " " + Z);
                    writeFile(location, X, Z);


                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }
            } else if (args.length > 1) {
                try {
                    String location = args[0];
                    String X = args[1];
                    String Z = args[2];

                    player.sendMessage("§c§l(!) §c Location " + location + " saved on: " + X + " " + Z);
                    writeFile(location, X, Z);


                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c§l(!) §cThat is not a valid entity!");
                }

            } else {
                player.sendMessage("§c§l(!) §cusage ");
                player.sendMessage("§c§l(!) §c/savelocation <name of your location> ");
                player.sendMessage("§c§l(!) §c/savelocation <name of your location> <X coordinate> <Z coordinate> ");
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
                    LocationWorld coordinates = readFile(location);

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

                    //read coordinates out of file
                    LocationWorld coordinates = readFile(location);

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

        else{
            return false;
        }


        return true;

    }


}







