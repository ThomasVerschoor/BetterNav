package com.company;

import com.events.Event_Handler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    //run this code when plugin is started
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new Event_Handler(),this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BetterAnchievements plugin enabled");
    }


    //run this code when plugin should be disabled
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BetterAnchievements plugin disabled");
    }

}