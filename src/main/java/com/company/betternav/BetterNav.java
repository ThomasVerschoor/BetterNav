package com.company.betternav;
import be.dezijwegel.betteryaml.BetterYaml;
import com.company.betternav.commands.CommandsHandler;
import com.company.betternav.events.Event_Handler;
import com.company.betternav.events.NavBossBar;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/*
* BetterNav plugin
* @author Thomas Verschoor & Dieter Nuytemans
 */
public class BetterNav extends JavaPlugin {

    private static BetterNav instance;

    public static BetterNav getInstance()
    {
        return instance;
    }

    // run this code when plugin is started
    @Override
    public void onEnable(){

        BetterNav.instance = this;

        YamlConfiguration config = new YamlConfiguration();
        try {
            BetterYaml betterYaml = new BetterYaml("config.yml", this, true);
            config = betterYaml.getYamlConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final PlayerGoals playerGoals = new PlayerGoals();
        final HashMap<UUID, Boolean> actionbarplayers = new HashMap<>();
        final HashMap<UUID, NavBossBar> bblist = new HashMap<>();

        // start command handler
        CommandsHandler commands = new CommandsHandler( config, playerGoals, this, actionbarplayers, bblist );
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

        // display a plugin enabled message
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BetterNav plugin enabled");


        // bstats addon
        int pluginId = 10444; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        /*
         *custom bstats added
         */

        // get distance to goal set and add SimplePie chart
        int distance = config.getInt("Distance");
        metrics.addCustomChart(new SimplePie("distance_to_goal", () -> String.valueOf(distance)));

        // get maximum of locations and add SimplePie chart
        int maxlocations = config.getInt("maximumWaypoints");
        metrics.addCustomChart(new SimplePie("maximum_locations", () -> String.valueOf(maxlocations)));

        // get navbarmode and add SimplePie chart
        int navbarmode = config.getInt("BossBar");
        metrics.addCustomChart(new SimplePie("navbar_mode", () -> String.valueOf(navbarmode)));

        // get number of private waypoints and add SimplePie chart
        boolean privatewaypoints = config.getBoolean("privateWayPoints");
        metrics.addCustomChart(new SimplePie("private_waypoints", () -> String.valueOf(privatewaypoints)));

        // get the setting if the welcome message is enabled and add SimplePie chart
        boolean welcome_message = config.getBoolean("welcomeMessage");
        metrics.addCustomChart(new SimplePie("welcome_message",()-> String.valueOf(welcome_message)));


        //Start UpdateChecker in a seperate thread to not completely block the server
        Thread updateChecker = new UpdateChecker(this);
        updateChecker.start();

    }


    // run this code when plugin should be disabled
    @Override
    public void onDisable(){

        // display message when the plugin is disabled
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BetterNav plugin disabled");
    }

}