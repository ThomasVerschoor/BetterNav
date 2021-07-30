package com.company.betternav.util;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BstatsImplementation
{

    JavaPlugin javaPlugin;
    YamlConfiguration config;

    public BstatsImplementation(JavaPlugin javaPlugin, YamlConfiguration config)
    {
        this.javaPlugin = javaPlugin;
        this.config = config;
    }

    public void run()
    {
        // bstats addon
        int pluginId = 10444; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(javaPlugin, pluginId);

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

        // get the setting if animations are enabled or not
        boolean enableAnimations = config.getBoolean("enableAnimations");
        metrics.addCustomChart(new SimplePie("animations",()-> String.valueOf(enableAnimations)));

        // get the color of the bossbar
        String bossbarColor = config.getString("BossBar_color");
        metrics.addCustomChart(new SimplePie("bossbar_color",()-> bossbarColor));

        String language = config.getString("language");
        metrics.addCustomChart(new SimplePie("language",()-> language));

        Boolean heightCheck = config.getBoolean("height_check");
        metrics.addCustomChart(new SimplePie("height_check",()-> String.valueOf(heightCheck)));

        Boolean deathLoc = config.getBoolean("death_location_save");
        metrics.addCustomChart(new SimplePie("death_locations",()-> String.valueOf(deathLoc)));

        Boolean deathNav = config.getBoolean("death_nav");
        metrics.addCustomChart(new SimplePie("death_nav",()-> String.valueOf(deathNav)));

    }
}
