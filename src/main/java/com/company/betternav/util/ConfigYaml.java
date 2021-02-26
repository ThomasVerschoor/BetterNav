package com.company.betternav.util;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigYaml {

    private final JavaPlugin plugin;
    private final FileConfiguration configuration;

    public void saveDefaultConfig(File file) {

        // if file doesn't exist
        if (!file.exists())
        {
            Bukkit.getLogger().info(ChatColor.GREEN + "Copying a new " + "config.yml" + " ...");
            plugin.saveResource("config.yml", false);


        }



    }

    public ConfigYaml(JavaPlugin plugin) {
        this.plugin = plugin;

        File configDirectory = new File(plugin.getDataFolder(),"config.yml");

        saveDefaultConfig(configDirectory);

        //if existed, read again. If new, copy
        configDirectory = new File(plugin.getDataFolder(),"config.yml");

        this.configuration = YamlConfiguration.loadConfiguration(configDirectory);

    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
