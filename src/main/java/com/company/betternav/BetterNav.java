package com.company.betternav;

import be.dezijwegel.betteryaml.BetterLang;
import be.dezijwegel.betteryaml.BetterYaml;
import com.company.betternav.commands.CommandsHandler;
import com.company.betternav.events.Event_Handler;
import com.company.betternav.events.NavBossBar;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.BstatsImplementation;
import com.company.betternav.util.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
* BetterNav plugin
* @author Thomas Verschoor & Dieter Nuytemans
**/

public class BetterNav extends JavaPlugin {

    private static BetterNav instance;

    public static BetterNav getInstance()
    {
        return instance;
    }

    // BetterLang implementation
    public Map<String,String> getMessages()
    {

        // Auto-updates the config on the server and loads a YamlConfiguration and File. Optionally, a boolean can be passed, which enables or disables logging.
        BetterLang messaging = new BetterLang("langTemplate.yml", "english.yml", this);

        // Get all message names and their mapped messages. Useful when sending named messages to players (eg: see below)
        Map<String, String> messages = messaging.getMessages();

        return messages;
    }


    // run this code when plugin is started
    @Override
    public void onEnable()
    {

        BetterNav.instance = this;

        // BetterYaml-config implementation
        YamlConfiguration config = new YamlConfiguration();
        try
        {
            BetterYaml betterYaml = new BetterYaml("config.yml", this, true);
            config = betterYaml.getYamlConfiguration();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        final PlayerGoals playerGoals = new PlayerGoals();
        final HashMap<UUID, Boolean> actionbarplayers = new HashMap<>();
        final HashMap<UUID, NavBossBar> bblist = new HashMap<>();

        // start command handler
        CommandsHandler commands = new CommandsHandler( config, playerGoals, this, actionbarplayers, bblist,getMessages());
        getServer().getPluginManager().registerEvents(new Event_Handler( config, playerGoals,this ,actionbarplayers,bblist),this);

        // set executor for the commands
        getCommand("bn").setExecutor(commands);
        getCommand("getlocation").setExecutor(commands);
        getCommand("savelocation").setExecutor(commands);
        getCommand("showlocations").setExecutor(commands);
        getCommand("showcoordinates").setExecutor(commands);
        getCommand("nav").setExecutor(commands);
        getCommand("del").setExecutor(commands);
        getCommand("navplayer").setExecutor(commands);
        getCommand("stopnav").setExecutor(commands);

        // BetterLang-language implementation
        Map<String,String> messages = getMessages();

        // display a plugin enabled message
        getServer().getConsoleSender().sendMessage( messages.getOrDefault("BetterNav_enabled", ChatColor.GREEN+"BetterNav plugin enabled") );

        // implement bstats
        BstatsImplementation bstatsImplementation = new BstatsImplementation(this,config);
        bstatsImplementation.run();

        //Start UpdateChecker in a seperate thread to not completely block the server
        Thread updateChecker = new UpdateChecker(this);
        updateChecker.start();

    }


    // run this code when plugin should be disabled
    @Override
    public void onDisable()
    {

        // BetterLang-language implementation
        Map<String,String> messages = getMessages();

        // display a plugin disabled message
        getServer().getConsoleSender().sendMessage( messages.getOrDefault("BetterNav_disabled", ChatColor.RED+"BetterNav plugin disabled") );

    }

}