package com.company.betternav.events;

import com.company.betternav.navigation.Goal;
import com.company.betternav.bossbarcalculators.IBossBarCalculator;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.bossbarcalculators.AdvancedBossbarCalculator;
import com.company.betternav.bossbarcalculators.BasicCalculator;
import com.company.betternav.bossbarcalculators.IdeaBossBarCalculator;
import com.company.betternav.util.animation.SpiralAnimation;
import com.company.betternav.util.animation.location.PlayerLocation;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static java.lang.String.valueOf;


public class Event_Handler implements Listener
{

    private final JavaPlugin plugin;
    private final PlayerGoals playerGoals;
    private HashMap<UUID, NavBossBar> bblist = new HashMap<>();
    private final IBossBarCalculator bossBarCalculator;
    private HashMap<UUID,Boolean> actionbarplayers = new HashMap<>();
    private final YamlConfiguration config;
    private final int distance_to_goal;
    private final Map<String,String> messages;
    private final boolean heightCheck;

    // rounding function
    public double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Event_Handler(YamlConfiguration config, PlayerGoals playerGoals, JavaPlugin plugin, HashMap<UUID, Boolean> actionbarplayers, HashMap<UUID, NavBossBar> bblist, Map<String, String> messages)
    {
        this.config = config;
        this.playerGoals = playerGoals;
        this.plugin = plugin;
        this.actionbarplayers = actionbarplayers;
        this.bblist = bblist;
        this.messages = messages;

        // get bb value out of config file
        int bbcalc = config.getInt("BossBar");

        if(bbcalc==1)
        {
            this.bossBarCalculator = new IdeaBossBarCalculator();
        }
        else if (bbcalc==2)
        {
            this.bossBarCalculator = new BasicCalculator();
        }
        else
        {
            this.bossBarCalculator = new AdvancedBossbarCalculator();
        }

        // get distance to goal value out of config file
        distance_to_goal = config.getInt("Distance");

        // get boolean heightcheck out of config file
        heightCheck = config.getBoolean("height_check");

    }


    // send welcome message when player joined
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        // check if welcomeMessage is enabled in config file
        boolean message = config.getBoolean("welcomeMessage");

        // get the player that joined
        Player player = event.getPlayer();

        // send him message
        if(message)
        {
            player.sendMessage( messages.getOrDefault("welcome_message", ChatColor.LIGHT_PURPLE + "Betternav plugin enabled: /bn to get help"));
        }

        // check if player had a navigation set
        Goal hadNav = playerGoals.getPlayerGoal(player.getUniqueId());
        if(hadNav!=null)
        {
            NavBossBar bb = new NavBossBar(plugin,messages);

            // put the bar on the list
            bblist.put(player.getUniqueId(),bb);

            double distance = Math.sqrt(Math.pow(((Math.round( player.getLocation().getBlockX()-hadNav.getLocation().getBlockX() ))),2)+(Math.pow(Math.round(player.getLocation().getBlockX()-hadNav.getLocation().getBlockX()),2)));
            distance = round(distance,2);

            // create a bar
            bb.createBar(hadNav.getName(),distance);

            // add player to the bar
            bb.addPlayer(player);
        }
    }

    //check if player has moved
    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event)
    {

        Location loc = event.getTo();
        if(loc == null)
        {
            return;
        }

        Player navPlayer = event.getPlayer();
        UUID uuid = navPlayer.getUniqueId();

        // check for action bar
        if (actionbarplayers.containsKey(uuid))
        {
            // get boolean for player
            boolean actionbar = actionbarplayers.get(uuid);
            if(actionbar)
            {
                int X_Coordinate = navPlayer.getLocation().getBlockX();
                int Y_Coordinate = navPlayer.getLocation().getBlockY();
                int Z_Coordinate = navPlayer.getLocation().getBlockZ();

                String X = valueOf(X_Coordinate);
                String Y = valueOf(Y_Coordinate);
                String Z = valueOf(Z_Coordinate);

                String actionbarColor = messages.getOrDefault("actionbar_color", "§f");
                String message = actionbarColor+"X "+X+"          "+"Y "+Y+"          "+"Z "+Z;

                navPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            }
        }

        //check for bossbar
        Goal goal = this.playerGoals.getPlayerGoal( uuid );

        // Return if Player has no active goal
        if (goal == null)
            return;

        //get name of the goal
        String goalName = goal.getName();
        String ownWorld = navPlayer.getWorld().getName();
        String worldGoal = goal.getLocation().getWorld().getName();

        // if on different world
        if(ownWorld!=worldGoal)
        {
            // send message to navigating person
            String primaryColor = messages.getOrDefault("primary_color", "§d");
            String message = primaryColor + messages.getOrDefault("different_world", "Target on different world");
            navPlayer.sendMessage(message);

            // delete player at navigating people
            this.playerGoals.removePlayerGoal( uuid );

            try
            {
                // delete the bossbar
                NavBossBar delbb = bblist.get(uuid);
                delbb.delete(navPlayer);

                // remove the bar of the list
                bblist.remove(navPlayer.getUniqueId());
            }
            catch(Exception e)
            {

            }
            return;
        }

        //get x,y and z value
        double x = goal.getLocation().getX();
        double y = goal.getLocation().getY();
        double z = goal.getLocation().getZ();

        //get current coordinates
        int x_nav = navPlayer.getLocation().getBlockX();
        int y_nav = navPlayer.getLocation().getBlockY();
        int z_nav = navPlayer.getLocation().getBlockZ();

        //calculate euclidean distance
        double distance = Math.sqrt(Math.pow(((Math.round( x-x_nav ))),2)+(Math.pow(Math.round(z-z_nav),2)));
        distance = round(distance,2);

        //create new bossbar
        NavBossBar bb = new NavBossBar(plugin,messages);

        //check if player exists
        if(bblist.containsKey(uuid))
        {
            // get bossbar of player
            NavBossBar navbb = bblist.get(uuid);

            // update the distance to the goal
            navbb.updateDistance(goalName,distance);

            // get vector of the player
            double barLevel = this.bossBarCalculator.calculateBarLevel( navPlayer, goal.getLocation());

            // update the progress on the bar
            navbb.setProgress(barLevel);
        }

        //else create bossbar
        else
        {
            // put the bar on the list
            bblist.put(uuid,bb);

            // create a bar
            bb.createBar(goalName,distance);

            // add player to the bar
            bb.addPlayer(navPlayer);
        }

        if(distance < distance_to_goal)
        {


            // set arrived message
            String primaryColor = messages.getOrDefault("primary_color", "§d");
            String message = primaryColor + messages.getOrDefault("arrived", "You arrived at")+" "+ goalName;

            // send player the message
            navPlayer.sendMessage(message);

            // calculate absolute height
            double height = y_nav-y;
            double absheight = Math.abs(height);

            if(heightCheck)
            {
                // if target is more than 5 levels higher or lower
                if(absheight>5)
                {
                    absheight = round(absheight,2);
                    String secondaryColor = messages.getOrDefault("secondary_color", "§2");
                    String messageHeight = secondaryColor + messages.getOrDefault("your_goal", "Your goal is")+" "+absheight+" ";

                    // if lower
                    if(height>0)
                    {
                        messageHeight = messageHeight+messages.getOrDefault("lower", "blocks lower");
                    }

                    // else higher
                    else
                    {
                        messageHeight = messageHeight+messages.getOrDefault("higher", "blocks higher");
                    }

                    navPlayer.sendMessage(messageHeight);
                }
            }

            // delete player at navigating people
            this.playerGoals.removePlayerGoal( uuid );

            // delete the bossbar
            NavBossBar delbb = bblist.get(uuid);
            delbb.delete(navPlayer);

            // remove the bar of the list
            bblist.remove(navPlayer.getUniqueId());

            // Spawn particle effects when enabled
            if (config.getBoolean("enableAnimations"))
                new SpiralAnimation(
                        new PlayerLocation( navPlayer ),
                        Particle.COMPOSTER,
                        1.3,1.8,5000, 1000,5
                ).startAnimation();
        }
    }
}
