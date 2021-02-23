package com.company.betternav;

import com.company.betternav.commands.Commands_Handler;
import com.company.betternav.events.Event_Handler;
import com.company.betternav.events.NavBossBar;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;


public class BetterNav extends JavaPlugin {



    //run this code when plugin is started
    @Override
    public void onEnable(){




        final PlayerGoals playerGoals = new PlayerGoals();
        final HashMap<UUID, Boolean> actionbarplayers = new HashMap<>();
        final HashMap<UUID, NavBossBar> bblist = new HashMap<>();

        Commands_Handler commands = new Commands_Handler( playerGoals, this,actionbarplayers,bblist );
        getServer().getPluginManager().registerEvents(new Event_Handler( playerGoals,this ,actionbarplayers,bblist),this);
        getCommand("bn").setExecutor(commands);
        getCommand("getlocation").setExecutor(commands);
        getCommand("savelocation").setExecutor(commands);
        getCommand("showlocations").setExecutor(commands);
        getCommand("showcoordinates").setExecutor(commands);
        getCommand("nav").setExecutor(commands);
        getCommand("del").setExecutor(commands);
        getCommand("navplayer").setExecutor(commands);
        getCommand("stopnav").setExecutor(commands);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BetterNav plugin enabled");


        ConfigYaml configuration = new ConfigYaml(this);


        //bstats
        int pluginId = 10444; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        // custom bstats
        int distance = configuration.getConfiguration().getInt("Distance");
        metrics.addCustomChart(new SimplePie("distance_to_goal", () -> String.valueOf(distance)));

        int maxlocations = configuration.getConfiguration().getInt("maximumWaypoints");
        metrics.addCustomChart(new SimplePie("maximum_locations", () -> String.valueOf(maxlocations)));

        int navbarmode = configuration.getConfiguration().getInt("BossBar");
        metrics.addCustomChart(new SimplePie("navbar_mode", () -> String.valueOf(navbarmode)));

        boolean privatewaypoints = configuration.getConfiguration().getBoolean("privateWayPoints");
        metrics.addCustomChart(new SimplePie("private_waypoints", () -> String.valueOf(privatewaypoints)));

        boolean welcome_message = configuration.getConfiguration().getBoolean("welcomeMessage");
        metrics.addCustomChart(new SimplePie("welcome_message",()-> String.valueOf(welcome_message)));







    }


    //run this code when plugin should be disabled
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BetterNav plugin disabled");
    }



}