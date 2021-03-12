package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.PlayerGoal;
import com.company.betternav.navigation.PlayerGoals;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NavPlayerCommand extends BetterNavCommand {

    private final PlayerGoals playerGoals;

    public NavPlayerCommand(PlayerGoals playerGoals) {
        this.playerGoals = playerGoals;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args) {
        // if location provided
        if (args.length == 1) {
            try {

                // get the UUID of the player
                UUID PlayersUUID = player.getUniqueId();


                // get the location needed
                String playerName = args[0];

                Player navto = Bukkit.getPlayer(playerName);

                if(navto==null){
                    player.sendMessage("Could not find player "+playerName);
                    return true;
                }

                //get coordinates to the goal
                PlayerGoal playerGoal = new PlayerGoal(playerName, navto);

                player.sendMessage("Navigating to "+ playerName);


                this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

            } catch (IllegalArgumentException e) {
                player.sendMessage("§c§l(!) §cThat is not a valid entity!");
            }
        }

        return true;
    }
}
