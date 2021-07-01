package com.company.betternav.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class NavBossBar
{

    private int taskID = -1;
    private final JavaPlugin plugin;
    private BossBar bar;
    private final YamlConfiguration config;
    private final Map<String,String>messages;

    public String createMsg(String goal,double distance)
    {

        String primaryColor = messages.getOrDefault("primary_color", "§d");
        String secondaryColor = messages.getOrDefault("secondary_color", "§2");
        String destinationText = messages.getOrDefault("destination_text", ChatColor.BLUE + "Destination :");
        String distanceText = messages.getOrDefault("distance_text", ChatColor.BLUE + "Distance :");

        // Fill in values
        String message = primaryColor+destinationText + " " + secondaryColor + goal + "      " + primaryColor + distanceText + " " + secondaryColor + Double.toString(distance);

        return message;
    }

    public NavBossBar(JavaPlugin plugin, YamlConfiguration config, Map<String,String> messages){
        this.plugin = plugin;
        this.config = config;
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
        bar = Bukkit.createBossBar(msg,getBarcolor(),BarStyle.SOLID);
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

    /**
     * Allows users to change color of bossbar
     *
     */
    public BarColor getBarcolor()
    {
        BarColor color = null;
        try
        {
            color = BarColor.valueOf(config.getString("BossBar_color", "blue").toUpperCase());
        }
        catch (IllegalArgumentException ex)
        {
            getServer().getConsoleSender().sendMessage("Color '" + config.getString("BarColor") + "' doesn't exist, supported colors are pink, blue, red, green, yellow, purple, white");
            color = BarColor.BLUE;
        }
        return color;

    }
}