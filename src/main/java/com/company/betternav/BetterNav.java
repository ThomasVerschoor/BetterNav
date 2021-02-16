package com.company.betternav;

import com.company.betternav.commands.Commands_Handler;
import com.company.betternav.events.Event_Handler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;



public class BetterNav extends JavaPlugin {



    //run this code when plugin is started
    @Override
    public void onEnable(){

        final PlayerGoals playerGoals = new PlayerGoals();

        Commands_Handler commands = new Commands_Handler( playerGoals );
        getServer().getPluginManager().registerEvents(new Event_Handler( playerGoals ),this);
        getCommand("bn").setExecutor(commands);
        getCommand("getlocation").setExecutor(commands);
        getCommand("savelocation").setExecutor(commands);
        getCommand("showlocation").setExecutor(commands);
        getCommand("showcoordinates").setExecutor(commands);
        getCommand("nav").setExecutor(commands);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BetterNav plugin enabled");
    }


    //run this code when plugin should be disabled
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BetterNav plugin disabled");
    }



}