package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.Goal;
import com.company.betternav.util.FileHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;
import static java.lang.Double.parseDouble;

public class SaveLocationCommand extends BetterNavCommand
{

    private final FileHandler fileHandler;
    public SaveLocationCommand(FileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages)
    {
        // if location provided
        if (args.length == 1)
        {
            try
            {
                String location = args[0];

                //check for illegal characters
                location = location.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                Goal saveloc = new Goal(location,player.getLocation());

                fileHandler.writeLocationFile(player,saveloc);

            }
            catch (IllegalArgumentException e)
            {
                player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
            }
        }

        // if name and coordinates are given
        else if (args.length == 4)
        {
            try
            {
                // get the location name
                String location = args[0];

                //check for illegal characters
                location = location.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                String X = args[1];
                String Y = args[2];
                String Z = args[3];

                Location saveloc = new Location(player.getWorld(),parseDouble(X),parseDouble(Y),parseDouble(Z));

                Goal pg = new Goal(location,saveloc);

                fileHandler.writeLocationFile(player,pg);

            }
            catch (IllegalArgumentException e)
            {
                player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
            }

        }
        else
        {
            String primaryColor = messages.getOrDefault("primary_color", "§d");

            String message = primaryColor + messages.getOrDefault("usage", "§c§l(!) §cusage ");
            player.sendMessage(message);

            String message2 = primaryColor + messages.getOrDefault("savelocation_current", "§c§l(!) §c/savelocation <name of your location> ");
            player.sendMessage(message2);

            String message3 = primaryColor + messages.getOrDefault("savelocation_coordinates", "§c§l(!) §c/savelocation <name of your location> <X coordinate> <Z coordinate> ");
            player.sendMessage(message3);
        }
        return true;
    }
}
