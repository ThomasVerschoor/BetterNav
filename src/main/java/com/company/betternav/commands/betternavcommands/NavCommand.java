package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.navigation.Goal;
import com.company.betternav.navigation.LocationWorld;
import com.company.betternav.navigation.PlayerGoals;
import com.company.betternav.util.FileHandler;
import com.company.betternav.util.animation.Animation;
import com.company.betternav.util.animation.LineAnimation;
import com.company.betternav.util.animation.location.PlayerLocation;
import com.company.betternav.util.animation.location.StaticLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NavCommand extends BetterNavCommand {

    private final YamlConfiguration config;

    private final FileHandler fileHandler;
    private final PlayerGoals playerGoals;

    public NavCommand(FileHandler fileHandler, PlayerGoals playerGoals, YamlConfiguration config) {
        this.fileHandler = fileHandler;
        this.playerGoals = playerGoals;

        this.config = config;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args) {
        // if location provided
        if (args.length == 1) {
            try {

                // get the UUID of the player
                UUID PlayersUUID = player.getUniqueId();


                // get the location needed
                String location = args[0];

                // read coordinates out of file
                LocationWorld coordinates = fileHandler.readFile(location,player);

                // error handling when location is wrong
                if(coordinates==null){
                    player.sendMessage("/bn to get information about how to use bn commands");
                    return true;
                }

                //get coordinates to the goal
                String goal = coordinates.getName();
                double x = coordinates.getX();
                double y = coordinates.getY();
                double z = coordinates.getZ();

                Goal playerGoal = new Goal( goal, new Location( Bukkit.getWorld(player.getWorld().getName()), x, y, z ) );

                player.sendMessage("Navigating to "+goal);
                player.sendMessage("Navigating to "+x+" "+z);

                this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

                if (config.getBoolean("enableAnimations"))
                    new LineAnimation(
                            new PlayerLocation(player), new StaticLocation(playerGoal.getLocation()),
                            Particle.COMPOSTER, 7.0, 0.05, 0.5, 500, 3
                    ).startAnimation();

            } catch (IllegalArgumentException e) {
                player.sendMessage("§c§l(!) §cThat is not a valid entity!");
            }
        }
        return true;
    }
}
