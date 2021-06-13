package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.LocationWorld;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;

public class ShowCoordinatesCommand extends BetterNavCommand {

    private final FileHandler fileHandler;

    public ShowCoordinatesCommand(FileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages) {

        // if location provided
        if (args.length == 1) {
            try {

                // the location needed
                String location = args[0];

                //read coordinates out of file
                LocationWorld coordinates = fileHandler.readFile(location,player);

                if(coordinates==null){
                    player.sendMessage("/bn to get information about how to use bn commands");
                    return true;
                }

                //send coordinates to the player
                player.sendMessage(coordinates.getName()+ " has coordinates X: "+String.valueOf(coordinates.getX())+ " Y:"+String.valueOf(coordinates.getY())+" and Z: "+String.valueOf(coordinates.getZ()));


            } catch (IllegalArgumentException e) {
                player.sendMessage("§c§l(!) §cThat is not a valid entity!");
            }
        }
        return true;
    }
}
