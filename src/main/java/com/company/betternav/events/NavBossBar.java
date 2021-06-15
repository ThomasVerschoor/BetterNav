package com.company.betternav.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class NavBossBar
{
    private int taskID = -1;
    private final JavaPlugin plugin;
    private BossBar bar;
    private final Map<String,String>messages;

    public String createMsg(String goal,double distance)
    {
        String destination = messages.getOrDefault("destination_text", ChatColor.BLUE + "Destination : ");
        String g = messages.getOrDefault("destination_location_color"+goal, ChatColor.GREEN +"<location>"+ goal);
        g.replace("<location>"," ");

        String distanceString = messages.getOrDefault("distance_text", ChatColor.BLUE + "Distance : ");
        String d = messages.getOrDefault("distance_to_location_color"+Double.toString(distance), ChatColor.GREEN +"<distance>"+ goal);
        d.replace("<distance>"," ");
        String msg = destination + g + distanceString + d;

        return msg;
    }

    public NavBossBar(JavaPlugin plugin, Map<String,String> messages){
        this.plugin = plugin;
        this.messages = messages;
    }

    public void addPlayer(Player player){
        bar.addPlayer(player);
    }

    public BossBar getBar(){
        return bar;
    }

    public void createBar(String goal, double distance)
    {
        String msg = createMsg(goal,distance);
        bar = Bukkit.createBossBar(msg,BarColor.BLUE,BarStyle.SOLID);
        bar.setVisible(true);
    }

    public void updateDistance(String goal,double distance)
    {
        String msg = createMsg(goal,distance);
        bar.setTitle(msg);
    }

    public void setProgress(double progress){
        bar.setProgress(progress);
    }

    public void delete(Player player)
    {
        bar.removePlayer(player);
    }
}