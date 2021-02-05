package com.company;

import com.commands.Commands_Handler;
import com.events.Event_Handler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    //run this code when plugin is started
    @Override
    public void onEnable(){
        Commands_Handler commands = new Commands_Handler();
        getServer().getPluginManager().registerEvents(new Event_Handler(),this);
        getCommand("heal").setExecutor(commands);
        getCommand("feed").setExecutor(commands);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BetterAnchievements plugin enabled");
    }


    //run this code when plugin should be disabled
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BetterAnchievements plugin disabled");
    }

}