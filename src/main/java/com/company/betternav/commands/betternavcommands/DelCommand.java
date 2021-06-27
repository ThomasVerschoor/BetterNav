package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;

public class DelCommand extends BetterNavCommand
{

    private final FileHandler fileHandler;

    public DelCommand(FileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages)
    {
        // if location to delete provided
        if (args.length == 1)
        {
            try
            {
                String location = args[0];
                boolean deleted = fileHandler.deleteFile(location,player);
                if(deleted)
                {
                    String primaryColor = messages.getOrDefault("primary_color", "§d");
                    String secondaryColor = messages.getOrDefault("secondary_color", "§6");
                    String message = messages.getOrDefault("is_deleted", "is deleted");

                    message = primaryColor + location + secondaryColor +" "+ message;
                    player.sendMessage(message);
                }
                else
                {
                    String primaryColor = messages.getOrDefault("primary_color", "§d");
                    String message = primaryColor + messages.getOrDefault("could_not_delete", "Could not delete location ");

                    player.sendMessage(message);
                }

            }
            catch (IllegalArgumentException e)
            {
                player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
            }
        }
        return true;
    }
}
