package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;

public class ShowLocationsCommand extends BetterNavCommand
{

    private final FileHandler fileHandler;
    private final YamlConfiguration config;

    public ShowLocationsCommand(FileHandler fileHandler, YamlConfiguration config)
    {
        this.fileHandler = fileHandler;
        this.config = config;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages)
    {
        String primaryColor = messages.getOrDefault("primary_color", "§d");
        String secondaryColor = messages.getOrDefault("secondary_color", "§2");

        String message = primaryColor+messages.getOrDefault("saved_locations", "saved locations: ");
        player.sendMessage(message);

        String id = player.getUniqueId().toString();
        String world = player.getWorld().getName();

        String readPath = fileHandler.getPath() + File.separator+world+File.separator;
        boolean privateWayPoints = config.getBoolean("privateWayPoints");
        if(privateWayPoints)
        {
            readPath = readPath+id+File.separator;
        }
        else
        {
            //create shared directory
            readPath = readPath+File.separator+"shared";
        }

        File folder = new File(readPath);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles==null||listOfFiles.length==0)
        {
            String message_no_locations = primaryColor + messages.getOrDefault("no_saved_locations", "There are no saved locations");
            player.sendMessage(message_no_locations);
            return true;
        }

        else
        {
            for (File file : listOfFiles)
            {
                if (file.isFile())
                {
                    // get full filename of file
                    String[] fileName = file.getName().split(".json");

                    // location name will be first part
                    String location = fileName[0];
                    //System.out.println(file.getName());

                    //send message to the player
                    String locationInList = secondaryColor + messages.getOrDefault("locationindex", " - ")+ location;
                    player.sendMessage(locationInList);
                }
            }
        }
        return true;
    }
}
