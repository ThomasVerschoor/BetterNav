package com.company.betternav.events;

import com.company.betternav.Goal;
import com.company.betternav.PlayerGoals;
import com.company.betternav.commands.Commands_Handler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.UUID;

import static com.company.betternav.commands.Commands_Handler.*;
import static java.lang.Integer.valueOf;

public class Event_Handler implements Listener {

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private final PlayerGoals playerGoals;

    public Event_Handler( PlayerGoals playerGoals )
    {
        this.playerGoals = playerGoals;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Betternav plugin enabled: /bn to get help");
    }



    //check if player has moved
    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event){

        // Early return if the player did not move in a relevant way (vertical/looks around)

        int prevX = event.getFrom().getBlockX();
        int prevZ = event.getFrom().getBlockZ();

        Location loc = event.getTo();
        if(loc == null) {
            return;
        }

        int currX = loc.getBlockX();
        int currZ = loc.getBlockZ();


        if (currX==prevX && prevZ == currZ){
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

        //System.out.println("Key: " + k + ", Value: " + goal);
        //player.sendMessage("Key"+ k +" value:" +goal);

        String goalName = goal.getName();

        //get x value
        double x = goal.getLocation().getX();
        //get z value
        double z = goal.getLocation().getZ();

        navPlayer.sendMessage("UUID "+uuid +" is navigating to " + goalName + " at (" +x +", "+z + ")");


        //get current coordinates
        int x_nav = navPlayer.getLocation().getBlockX();
        int y_nav = navPlayer.getLocation().getBlockY();
        int z_nav = navPlayer.getLocation().getBlockZ();

        //calculate euclidean distance

        double distance = Math.sqrt(Math.pow(((Math.round( x-x_nav ))),2)+(Math.pow(Math.round(z-z_nav),2)));
        distance = round(distance,2);
        navPlayer.sendMessage(String.valueOf(distance));

        if(distance < 2){

            navPlayer.sendMessage("You arrived at "+goal);

            //delete player at navigating people
            this.playerGoals.removePlayerGoal( uuid );
        }







        //boolean navigating = comms.getNav();

        /*
        if(navigating){
            //player.sendMessage("Navigating");

            //get current coordinates
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();

            //calculate euclidean distance

            double distance = Math.sqrt(Math.pow(((Integer.parseInt(x_goal)-x)),2)+(Math.pow((Integer.parseInt(z_goal)-z),2)));
            player.sendMessage(String.valueOf(distance));

            if(distance < 2){

                comms.setNav(false);
                player.sendMessage("You arrived at "+goal);
            }

        }
        */




    }



}
