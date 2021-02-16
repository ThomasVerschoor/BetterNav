package com.company.betternav.events;

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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Betternav plugin enabled: /bn to get help");
    }



    //check if player has moved
    @EventHandler
    public static void onPlayerWalk(PlayerMoveEvent event){
        Commands_Handler comms = new Commands_Handler();
        Player player = event.getPlayer();

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
        for (HashMap.Entry<UUID, HashMap<String,String>> entry : playersNavigating.entrySet()) {
            UUID k = entry.getKey();
            HashMap<String,String> goal = entry.getValue();
            //System.out.println("Key: " + k + ", Value: " + goal);
            //player.sendMessage("Key"+ k +" value:" +goal);

            String goalString = goal.toString();

            //remove first character
            goalString = goalString.substring(1);

            //remove last character
            goalString = goalString.substring(0, goalString.length() - 1);

            //split the string
            String[] Coordinates = goalString.split("=");

            //get x value
            String x = Coordinates[0];
            //get z value
            String z = Coordinates[1];



            Player navplayer = Bukkit.getPlayer(k);

            navplayer.sendMessage("UUID "+k +" is navigating to "+x +" "+z);


            //get current coordinates
            int x_nav = navplayer.getLocation().getBlockX();
            int y_nav = navplayer.getLocation().getBlockY();
            int z_nav = navplayer.getLocation().getBlockZ();

            //calculate euclidean distance

            double distance = Math.sqrt(Math.pow(((Integer.parseInt(x)-x_nav)),2)+(Math.pow((Integer.parseInt(z)-z_nav),2)));
            distance = round(distance,2);
            navplayer.sendMessage(String.valueOf(distance));

            if(distance < 2){

                navplayer.sendMessage("You arrived at "+goal);

                //delete player at navigating people
                playersNavigating.remove(k);
            }



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
