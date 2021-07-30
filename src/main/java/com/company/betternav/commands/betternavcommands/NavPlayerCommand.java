package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.Navigation;
import com.company.betternav.navigation.PlayerGoal;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.animation.LineAnimation;
import com.company.betternav.util.animation.location.PlayerLocation;
import com.company.betternav.util.animation.location.StaticLocation;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
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

                // get the UUID of the player
                UUID PlayersUUID = player.getUniqueId();

                // get the location needed
                String playerName = args[0];

                Player navto = Bukkit.getPlayer(playerName);

                if(navto==null)
                {
                    String primaryColor = messages.getOrDefault("primary_color", "§d");
                    String message = primaryColor + messages.getOrDefault("player_not_found", "Could not find player");
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
                /*

                this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

                if (config.getBoolean("enableAnimations"))
                    new LineAnimation(
                            new PlayerLocation(player), new StaticLocation(playerGoal.getLocation()),
                            Particle.COMPOSTER, 7.0, 0.05, 0.5, 500, 3
                    ).startAnimation();

                 */

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
