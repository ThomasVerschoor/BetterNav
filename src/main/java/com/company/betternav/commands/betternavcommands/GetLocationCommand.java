package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class GetLocationCommand extends BetterNavCommand {

    // hashmap to hold the players with action bar enabled or not
    private final Map<UUID, Boolean> actionbarPlayers;

    public GetLocationCommand(Map<UUID, Boolean> actionbarPlayers)
    {
        this.actionbarPlayers = actionbarPlayers;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args,Map<String,String> messages) {
        // get the UUID of the player
        UUID PlayersUUID = player.getUniqueId();

        // check if player is NOT on list
        if( ! actionbarPlayers.containsKey(PlayersUUID)){
            // put player on the list
            actionbarPlayers.put(PlayersUUID,false);
        }

        // get the boolean
        boolean en = actionbarPlayers.get(PlayersUUID);

        // toggle the boolean
        en = !en;

        // set the toggled value
        actionbarPlayers.remove(PlayersUUID);
        actionbarPlayers.put(PlayersUUID,en);

        return true;
    }
}
