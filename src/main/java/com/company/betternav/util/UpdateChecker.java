package com.company.betternav.util;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class UpdateChecker extends Thread {

    @Override
    public void run()
    {
        URL url = null;
        try {
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource={RESOURCE_ID_HIER}");
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

            String currentVersion = "${project.version}";
            if(latestVersion.equalsIgnoreCase(currentVersion))
                Bukkit.getLogger().info("You are up to date");
            else
                Bukkit.getLogger().info("You are using " + currentVersion + " and the latest release is " + latestVersion);

        } catch (IOException | NullPointerException e) {
            Bukkit.getLogger().info("An error occurred while retrieving the latest version!");
        }
    }

}