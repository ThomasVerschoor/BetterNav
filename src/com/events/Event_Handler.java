package com.events;

import com.commands.Commands_Handler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.commands.Commands_Handler.*;
import static java.lang.Integer.valueOf;

public class Event_Handler implements Listener {

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

        boolean navigating = comms.getNav();

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



    }



}
