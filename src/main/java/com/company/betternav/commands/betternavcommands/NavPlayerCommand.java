package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.Navigation;
import com.company.betternav.navigation.PlayerGoal;
import com.company.betternav.navigation.PlayerGoals;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class NavPlayerCommand extends BetterNavCommand
{

    private final YamlConfiguration config;
    private final PlayerGoals playerGoals;

    public NavPlayerCommand(YamlConfiguration config, PlayerGoals playerGoals)
    {
        this.config = config;
        this.playerGoals = playerGoals;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages)
    {
        // if location provided
        if (args.length == 1)
        {
            try
            {

                // get the location needed
                String playerName = args[0];

                Player navto = Bukkit.getPlayer(playerName);

                // if location is null
                if(navto==null)
                {
                    String primaryColor = messages.getOrDefault("primary_color", "§d");
                    String message = primaryColor + messages.getOrDefault("player_not_found", "Could not find player");
                    player.sendMessage(message);
                    return true;
                }

                //if player casts navplayer command to himself
                if(playerName.equals(navto.getName()))
                {
                    String primaryColor = messages.getOrDefault("primary_color", "§d");
                    String message = primaryColor + messages.getOrDefault("nav_to_yourself", "Cannot cast navigation to yourself");
                    player.sendMessage(message);
                    return true;
                }

                //get coordinates to the goal
                PlayerGoal playerGoal = new PlayerGoal(playerName, navto);

                String primaryColor = messages.getOrDefault("primary_color", "§d");
                String secondaryColor = messages.getOrDefault("secondary_color", "§2");
                String message = primaryColor+messages.getOrDefault("navigating_to", "Navigating to")+" "+secondaryColor+ playerName;

                player.sendMessage(message);

                Navigation nav = new Navigation(playerGoals,player,playerGoal,config);
                nav.startNavigation();

            }
            catch (IllegalArgumentException e)
            {
                String primaryColor = messages.getOrDefault("primary_color", "§d");
                player.sendMessage( primaryColor +messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
            }
        }
        return true;
    }
}
