package com.company.betternav.commands;

import be.dezijwegel.betteryaml.BetterYaml;
import com.company.betternav.commands.betternavcommands.*;
import com.company.betternav.events.NavBossBar;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;


/*
Command Handler for BetterNavigating Plugin
 */
public class CommandsHandler implements CommandExecutor {

    private final Map<String, BetterNavCommand> commandMap;

    /**
     *  Constructor for command handler
     *
     * @param playerGoals class
     * @param plugin, to get the path extracted
     *
     */
    public CommandsHandler(YamlConfiguration config, PlayerGoals playerGoals, JavaPlugin plugin, HashMap<UUID,Boolean> actionbarplayers, HashMap<UUID, NavBossBar> bblist)
    {
        FileHandler fileHandler = new FileHandler(plugin, config);

        this.commandMap = new HashMap<String, BetterNavCommand>(){{
            put("bn",               new BnCommand());
            put("getlocation",      new GetLocationCommand(actionbarplayers));
            put("showlocations",    new ShowLocationsCommand(fileHandler, config));
            put("savelocation",     new SaveLocationCommand(fileHandler));
            put("del",              new DelCommand(fileHandler));
            put("showcoordinates",  new ShowCoordinatesCommand(fileHandler));
            put("nav",              new NavCommand(fileHandler, playerGoals, config));
            put("navplayer",        new NavPlayerCommand(playerGoals));
            put("stopnav",          new StopNavCommand(playerGoals, bblist));
        }};
    }

    /**
     *
     * @param sender sender of the command
     * @param cmd commands
     * @param s message
     * @param args arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        // check if a player was the sender of the command
        if (!(sender instanceof Player)) {
            sender.sendMessage("only players can use that command");

            return true;
        }

        // Return false if the command is not found
        String command = cmd.getName().toLowerCase();
        if ( ! commandMap.containsKey( command ))
            return false;

        // Use the command object for cmd execution
        Player player = (Player) sender;
        return commandMap.get( command ).execute( player, cmd, s, args );

    }


}







