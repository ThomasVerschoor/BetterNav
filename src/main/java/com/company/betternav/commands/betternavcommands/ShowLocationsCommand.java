package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;

public class ShowLocationsCommand extends BetterNavCommand {

    private final FileHandler fileHandler;
    private final YamlConfiguration config;

    public ShowLocationsCommand(FileHandler fileHandler, YamlConfiguration config)
    {
        this.fileHandler = fileHandler;
        this.config = config;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages) {

        player.sendMessage("saved locations: ");

        String id = player.getUniqueId().toString();
        String world = player.getWorld().getName();


        String readPath = fileHandler.getPath() + File.separator+world+File.separator;
        boolean privateWayPoints = config.getBoolean("privateWayPoints");
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
        return true;
    }
}
