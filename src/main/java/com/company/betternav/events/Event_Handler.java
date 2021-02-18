package com.company.betternav.events;

import com.company.betternav.Goal;
import com.company.betternav.PlayerGoals;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.UUID;


public class Event_Handler implements Listener {


    HashMap<UUID, NavBossBar> bblist = new HashMap<>();


    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private final PlayerGoals playerGoals;
    private final JavaPlugin plugin;

    public Event_Handler(PlayerGoals playerGoals, JavaPlugin plugin)
    {
        this.playerGoals = playerGoals;
        this.plugin = plugin;


    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Betternav plugin enabled: /bn to get help");
    }





    //check if player has moved
    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event){




        /*
        // Early return if the player did not move in a relevant way (vertical/looks around)

        int prevX = event.getFrom().getBlockX();
        int prevZ = event.getFrom().getBlockZ();

        int currX = loc.getBlockX();
        int currZ = loc.getBlockZ();


        if (currX==prevX && prevZ == currZ){
            return;
        }
        */

        Location loc = event.getTo();
        if(loc == null) {
            return;
        }



        //loop over the players that are navigating

        //playersNavigating.forEach((UUID,goal) -> player.sendMessage("User "+UUID +" navigated to" +goal.toString()));

        Player navPlayer = event.getPlayer();
        UUID uuid = navPlayer.getUniqueId();
        Goal goal = this.playerGoals.getPlayerGoal( uuid );

        // Return if Player has no active goal
        if (goal == null)
            return;

        //get name of the goal
        String goalName = goal.getName();

        //get x value
        double x = goal.getLocation().getX();
        //get z value
        double z = goal.getLocation().getZ();

        //navPlayer.sendMessage("UUID "+uuid +" is navigating to " + goalName + " at (" +x +", "+z + ")");


        //get current coordinates
        int x_nav = navPlayer.getLocation().getBlockX();
        int y_nav = navPlayer.getLocation().getBlockY();
        int z_nav = navPlayer.getLocation().getBlockZ();

        //calculate euclidean distance

        double distance = Math.sqrt(Math.pow(((Math.round( x-x_nav ))),2)+(Math.pow(Math.round(z-z_nav),2)));
        distance = round(distance,2);
        //navPlayer.sendMessage(String.valueOf(distance));



        //create new bossbar

        NavBossBar bb = new NavBossBar(plugin);


        //check if player exists
        if(bblist.containsKey(uuid)){

            // get bossbar of player
            NavBossBar navbb = bblist.get(uuid);

            // update the distance to the goal
            navbb.updateDistance(goalName,distance);

            // get vector of the player
            Vector directionPlayer = navPlayer.getLocation().getDirection();

            //System.out.println(directionPlayer.getX());
            //System.out.println(directionPlayer.getZ());

            // get viewing direction vector
            Vector viewingDirection = new Vector(directionPlayer.getX(),0,directionPlayer.getZ());

            // normalize the vector
            viewingDirection = viewingDirection.normalize();

            // calculate the vector between current loc and navigation loc
            double x_vector = x_nav - x;
            double y_vector = 0;
            double z_vector = z_nav - z;

            // create new vector with xyz
            Vector distanceDirection = new Vector(x_vector,y_vector,z_vector);

            // normalize the direction
            distanceDirection = distanceDirection.normalize();

            // calculate the angle between the vectors
            float angle = viewingDirection.angle(distanceDirection);

            // calculate the mapping to the barlevel
            double barlevel = angle/Math.PI;

            //System.out.println(angle2);

            // update the progress on the bar
            navbb.setProgress(barlevel);

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

        if(distance < 2){

            // set welcome message
            String message = "You arrived at ";

            // set locationname in different color
            String goalMessage = ChatColor.LIGHT_PURPLE + goalName;

            // send player the message
            navPlayer.sendMessage(message + goalMessage);

            // delete player at navigating people
            this.playerGoals.removePlayerGoal( uuid );

            // delete the bossbar
            NavBossBar delbb = bblist.get(uuid);
            delbb.delete(navPlayer);

            // remove the bar of the list
            bblist.remove(navPlayer.getUniqueId());

        }

    }

}
