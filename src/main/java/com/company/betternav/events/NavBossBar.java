package com.company.betternav.events;


import com.company.betternav.BetterNav;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static java.lang.String.format;

public class NavBossBar {

    private int taskID = -1;
    private final JavaPlugin plugin;
    private BossBar bar;

    public String createMsg(String goal,double distance){

        String destination = ChatColor.BLUE + "Destination : ";
        String g = ChatColor.GREEN + goal;
        String distanceString = ChatColor.BLUE + " Distance : ";
        String d = ChatColor.GREEN + Double.toString(distance);

        String msg = destination + g + distanceString + d;

        return msg;
    }

    public NavBossBar(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void addPlayer(Player player){
        bar.addPlayer(player);
    }

    public BossBar getBar(){
        return bar;
    }

    public void createBar(String goal, double distance){

        String msg = createMsg(goal,distance);

        //bar = Bukkit.createBossBar(format("&cDestination: "+ChatColor.GREEN +goal + " &cDistance: "+ChatColor.GREEN + distance), BarColor.BLUE, BarStyle.SOLID);
        bar = Bukkit.createBossBar(msg,BarColor.BLUE,BarStyle.SOLID);
        bar.setVisible(true);
    }

    public void updateDistance(String goal,double distance){

        String msg = createMsg(goal,distance);

        //bar.setTitle(format("&cDestination: "+ChatColor.GREEN +goal + " Distance: "+ChatColor.GREEN + distance));
        bar.setTitle(msg);
    }

    public void setProgress(double progress){
        bar.setProgress(progress);
    }


    private String format(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public void delete(Player player){
        bar.removePlayer(player);

    }



}

