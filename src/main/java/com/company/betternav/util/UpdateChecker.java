package com.company.betternav.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class UpdateChecker extends Thread {

    private final Plugin plugin;

    public UpdateChecker(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        URL url = null;
        try {
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=89438");
        } catch (MalformedURLException ignored) {}

        URLConnection conn = null;
        try {
            conn = Objects.requireNonNull(url).openConnection();
        } catch (IOException | NullPointerException e) {
            Bukkit.getLogger().info("An error occured while retrieving the latest version");
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(conn).getInputStream()));
            String latestVersion = reader.readLine();

            String currentVersion = plugin.getDescription().getVersion();
            if(latestVersion.equalsIgnoreCase(currentVersion))
                Bukkit.getLogger().info("You are up to date. You are using " + currentVersion + ".");
            else
                Bukkit.getLogger().info("You are using " + currentVersion + " and the latest release is " + latestVersion);

        } catch (IOException | NullPointerException e) {
            Bukkit.getLogger().info("An error occurred while retrieving the latest version!");
        }
    }

}