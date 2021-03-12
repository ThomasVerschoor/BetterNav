package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.events.NavBossBar;
import com.company.betternav.navigation.PlayerGoals;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StopNavCommand extends BetterNavCommand {

    private final PlayerGoals playerGoals;
    // hashmap to keep track of players with their bossbar
    private final HashMap<UUID, NavBossBar> bblist;

    public StopNavCommand(PlayerGoals playerGoals, HashMap<UUID, NavBossBar> bblist) {
        this.playerGoals = playerGoals;
        this.bblist = bblist;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args) {
        try {

            // delete player at navigating people
            this.playerGoals.removePlayerGoal(player.getUniqueId());

            // delete the bossbar
            NavBossBar delbb = bblist.remove(player.getUniqueId());
            delbb.delete(player);

            // remove the bar of the list
            bblist.remove(player.getUniqueId());

            // set locationname in different color
            String endMessage = ChatColor.LIGHT_PURPLE + "ending navigation";

            // send player the message
            player.sendMessage(endMessage);
        }catch (Exception e){
            player.sendMessage("Cannot end navigation");
        }

        return true;
    }
}
