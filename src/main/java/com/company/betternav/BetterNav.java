package com.company.betternav;

import be.dezijwegel.betteryaml.BetterLang;
import be.dezijwegel.betteryaml.OptionalBetterYaml;
import be.dezijwegel.betteryaml.validation.ValidationHandler;
import be.dezijwegel.betteryaml.validation.validator.Validator;
import com.company.betternav.commands.CommandsHandler;
import com.company.betternav.events.Event_Handler;
import com.company.betternav.events.NavBossBar;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.BstatsImplementation;
import com.company.betternav.util.UpdateChecker;
import com.company.betternav.util.validators.ColorCharValidator;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
* BetterNav plugin
* @author Thomas Verschoor & Dieter Nuytemans
**/

public class BetterNav extends JavaPlugin {

    private static BetterNav instance;

    public static BetterNav getInstance()
    {
        return instance;
    }

    // BetterLang implementation
    public Map<String,String> getMessages(YamlConfiguration config)
    {
        // get language out of config file
        String language = config.getString("language");

        Validator colorValidator = new ColorCharValidator();
        ValidationHandler validation = new ValidationHandler()
                .addValidator("primary_color", colorValidator)
                .addValidator("secondary_color", colorValidator)
                .addValidator("usage",colorValidator)
                .addValidator("savelocation_current",colorValidator)
                .addValidator("savelocation_coordinates",colorValidator)
                .addValidator("actionbar_color",colorValidator)
                .addValidator("location_saved",colorValidator)
                .addValidator("or", colorValidator);

        // Auto-updates the config on the server and loads a YamlConfiguration and File. Optionally, a boolean can be passed, which enables or disables logging.
        BetterLang messaging = new BetterLang("messages.yml", language+".yml", validation, this);

        // Get all message names and their mapped messages. Useful when sending named messages to players (eg: see below)
        return messaging.getMessages();
    }


    // run this code when plugin is started
    @Override
    public void onEnable()
    {

        BetterNav.instance = this;

        // get BetterYaml config
        OptionalBetterYaml optionalConfig = new OptionalBetterYaml("config.yml", this, true);
        Optional<YamlConfiguration> optionalYaml = optionalConfig.getYamlConfiguration();

        if (!optionalYaml.isPresent())
        {
            getServer().getConsoleSender().sendMessage( ChatColor.RED + "Warning! BetterNav cannot enable" );
            getServer().getPluginManager().disablePlugin( this );
            return;
        }

        YamlConfiguration config = optionalYaml.get();
        Map<String, String> messages = getMessages(config);

        final PlayerGoals playerGoals = new PlayerGoals();
        final HashMap<UUID, Boolean> actionbarplayers = new HashMap<>();
        final HashMap<UUID, NavBossBar> bblist = new HashMap<>();

        // start command handler
        CommandsHandler commands = new CommandsHandler( config, playerGoals, this, actionbarplayers, bblist, messages);
        getServer().getPluginManager().registerEvents(new Event_Handler( config, playerGoals,this ,actionbarplayers,bblist,messages),this);

        // set executor for the commands
        getCommand("bn").setExecutor(commands);
        getCommand("getlocation").setExecutor(commands);
        getCommand("savelocation").setExecutor(commands);
        getCommand("showlocations").setExecutor(commands);
        getCommand("showcoordinates").setExecutor(commands);
        getCommand("nav").setExecutor(commands);
        getCommand("del").setExecutor(commands);
        getCommand("navplayer").setExecutor(commands);
        getCommand("stopnav").setExecutor(commands);

        // display a plugin enabled message
        getServer().getConsoleSender().sendMessage( ChatColor.GREEN+"BetterNav plugin enabled" );

        // implement bstats
        BstatsImplementation bstatsImplementation = new BstatsImplementation(this,config);
        bstatsImplementation.run();

        //Start UpdateChecker in a seperate thread to not completely block the server
        Thread updateChecker = new UpdateChecker(this);
        updateChecker.start();

    }

    // run this code when plugin should be disabled
    @Override
    public void onDisable()
    {
        // display a plugin disabled message
        getServer().getConsoleSender().sendMessage( ChatColor.RED+"BetterNav plugin disabled" );

    }

}