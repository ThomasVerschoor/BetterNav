package com.company;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    //run this code when plugin is started
    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "BetterAnchievements plugin enabled");
    }


    //run this code when plugin should be disabled
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "BetterAnchievements plugin disabled");
    }

}