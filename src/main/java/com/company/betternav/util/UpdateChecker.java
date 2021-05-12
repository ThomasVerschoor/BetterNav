package com.company.betternav.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
*
* Checks for updates online and informs the user if there is a new version available or not
*
* This is done in a seperate thread to NOT block the server
*
**/


public class UpdateChecker extends Thread {

    private final Plugin plugin;

    public UpdateChecker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {

        // url of the betternav resource
        URL url = null;

        try
        {
            // get the latest version of the betternav resource
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=89438");
        }
        catch (MalformedURLException ignored) {}

        // make connection with Spigot api
        URLConnection conn = null;

        try
        {
            conn = Objects.requireNonNull(url).openConnection();
        }
        catch (IOException | NullPointerException e)
        {
            Bukkit.getLogger().info(ChatColor.RED + "An error occured while retrieving the latest version");
        }

        try
        {
            // read out the version
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(conn).getInputStream()));

            // get the latest version
            String latestVersion = reader.readLine();

            // delete the V out the version
            latestVersion = latestVersion.replaceAll("V", "");

            // get the current version
            String currentVersion = plugin.getDescription().getVersion();

            // check if the current and latest version are the same or not
            if (latestVersion.equalsIgnoreCase(currentVersion))
            {
                // Message that you are using the latest version
                Bukkit.getLogger().info("[BetterNav] You are using the latest version: " + currentVersion);
            }
            else
            {
                // Message that you're using an older version of BetterNav
                Bukkit.getLogger().info(ChatColor.RED + "[BetterNav] You are using " + currentVersion + " and the latest release is " + latestVersion);
            }

        }
        catch (IOException | NullPointerException e)
        {
            // Error messaging
            Bukkit.getLogger().info(ChatColor.RED + "An error occurred while retrieving the latest version!");
        }
    }
}