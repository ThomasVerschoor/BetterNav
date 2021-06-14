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
                    player.sendMessage( messages.getOrDefault(location+" "+"is_deleted", location+" is deleted"));

                }
                else
                {
                    player.sendMessage( messages.getOrDefault("could_not_delete"+" "+location, "Could not delete location "+location));
                }

            }
            catch (IllegalArgumentException e)
            {
                player.sendMessage( messages.getOrDefault("could_not_delete", "Could not delete location"));
            }
        }
        return true;
    }
}
