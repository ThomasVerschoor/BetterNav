package com.company.betternav.commands;

import com.company.betternav.Goal;
import com.company.betternav.PlayerGoals;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.String.valueOf;

//world.getname() om wereld te krijgen -> meerdere werelden
// derde y coordinaat enkel voor tp!
//bstats?
//maven toevoegen
// https://www.youtube.com/watch?v=iwzzd10Ms9U

//player object, player.isOp()
//met permissions werken, regeltje true of false
//-> player.betternav()
// permission hoe veel locaties per speler
// per speler apart?

public class Commands_Handler implements CommandExecutor {


    private final PlayerGoals playerGoals;

    String path = "/Users/thomasverschoor/Desktop/Minecraft/bukkit/paper_server/plugins/BetterNav/";

    public Commands_Handler(PlayerGoals playerGoals)
    {
        this.playerGoals = playerGoals;
    }

    public void writeFile(String name, String X, String Z) {

        try {
            FileWriter myWriter = new FileWriter(path + name + ".txt");
            myWriter.write(name + "\n" + X + "\n" + Z);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    // to read a file
    public ArrayList<String> readFile(String location) {

        // get arraylist for coordinates
        ArrayList<String> locations = new ArrayList<String>();

        try {

            File myObj = new File(path+location + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                locations.add(data);
                //System.out.println(data);
            }
            myReader.close();
            return locations;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

        }

        return locations;

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
                    String[] fileName = file.getName().split(".txt");

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
                    ArrayList<String> coordinates = readFile(location);

                    //send coordinates to the player
                    player.sendMessage(coordinates.get(0));
                    player.sendMessage(coordinates.get(1));
                    player.sendMessage(coordinates.get(2));


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
                    ArrayList<String> coordinates = readFile(location);

                    //get coordinates to the goal
                    String goal = coordinates.get(0);
                    double x = Double.parseDouble( coordinates.get(1) );
                    double z = Double.parseDouble( coordinates.get(2) );

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







