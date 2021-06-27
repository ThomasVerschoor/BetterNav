package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.Goal;
import com.company.betternav.navigation.LocationWorld;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.FileHandler;
import com.company.betternav.util.animation.LineAnimation;
import com.company.betternav.util.animation.location.PlayerLocation;
import com.company.betternav.util.animation.location.StaticLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class NavCommand extends BetterNavCommand
{

    private final YamlConfiguration config;
    private final FileHandler fileHandler;
    private final PlayerGoals playerGoals;

    public NavCommand(FileHandler fileHandler, PlayerGoals playerGoals, YamlConfiguration config)
    {
        this.fileHandler = fileHandler;
        this.playerGoals = playerGoals;
        this.config = config;
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
                String location = args[0];

                // read coordinates out of file
                LocationWorld coordinates = fileHandler.readFile(location,player);

                // error handling when location is wrong
                if(coordinates==null)
                {
                    player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
                    return true;
                }

                //get coordinates to the goal
                String goal = coordinates.getName();
                double x = coordinates.getX();
                double y = coordinates.getY();
                double z = coordinates.getZ();

                Goal playerGoal = new Goal( goal, new Location( Bukkit.getWorld(player.getWorld().getName()), x, y, z ) );

                // send message to player
                String primaryColor = messages.getOrDefault("primary_color", "§d");
                String secondaryColor = messages.getOrDefault("secondary_color", "§2");

                String message = primaryColor + messages.getOrDefault("navigating_to", "Navigating to") + " " + location;

                // only send x and z if height check is not enabled
                if(config.getBoolean("height_check"))
                {
                    message = message + secondaryColor+ " "+x+" "+y+" "+z;
                }
                else
                {
                    message = message + secondaryColor+ " "+x+" "+z;
                }

                player.sendMessage(message);

                this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

                if (config.getBoolean("enableAnimations"))
                    new LineAnimation(
                            new PlayerLocation(player), new StaticLocation(playerGoal.getLocation()),
                            Particle.COMPOSTER, 7.0, 0.05, 0.5, 500, 3
                    ).startAnimation();

            }
            catch (IllegalArgumentException e)
            {
                player.sendMessage( messages.getOrDefault("error", "/bn to get information about how to use Betternav commands"));
            }
        }
        return true;
    }
}
