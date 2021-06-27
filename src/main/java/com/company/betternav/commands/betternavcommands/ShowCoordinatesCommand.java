package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.LocationWorld;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

public class ShowCoordinatesCommand extends BetterNavCommand
{

    private final FileHandler fileHandler;
    private final YamlConfiguration config;

    public ShowCoordinatesCommand(FileHandler fileHandler, YamlConfiguration config)
    {
        this.fileHandler = fileHandler;
        this.config = config;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages)
    {

        // if location provided
        if (args.length == 1)
        {
            try
            {
                // the location needed
                String location = args[0];

                //read coordinates out of file
                LocationWorld coordinates = fileHandler.readFile(location,player);

                if(coordinates==null)
                {
                    player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
                    return true;
                }

                // set up message
                String primaryColor = messages.getOrDefault("primary_color", "§d");
                String secondaryColor = messages.getOrDefault("secondary_color", "§2");

                String hascoordinates = messages.getOrDefault("has_coordinates", "has coordinates");

                // get XZ or XYZ coordinates
                String coordinateMessage;

                if(config.getBoolean("height_check"))
                {
                    coordinateMessage = " X:"+String.valueOf(coordinates.getX())+ " Y:"+String.valueOf(coordinates.getY())+" Z:"+String.valueOf(coordinates.getZ());

                }
                else
                {
                    coordinateMessage = " X:"+String.valueOf(coordinates.getX())+ " Z:"+String.valueOf(coordinates.getZ());
                }
                String message = primaryColor+location+" "+hascoordinates+" "+secondaryColor+coordinateMessage;


                //send coordinates to the player
                player.sendMessage(message);

            }
            catch (IllegalArgumentException e)
            {
                player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
            }
        }
        return true;
    }
}
