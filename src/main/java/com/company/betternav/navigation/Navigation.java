package com.company.betternav.navigation;

import com.company.betternav.util.animation.LineAnimation;
import com.company.betternav.util.animation.location.PlayerLocation;
import com.company.betternav.util.animation.location.StaticLocation;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Navigation {


    private final PlayerGoals playerGoals;
    private final Player player;
    private final Goal playerGoal;
    private final YamlConfiguration config;

    public Navigation(PlayerGoals playerGoals, Player player,Goal playerGoal,YamlConfiguration config)
    {
        this.playerGoals = playerGoals;
        this.player = player;
        this.playerGoal = playerGoal;
        this.config = config;
    }

    public void startNavigation()
    {
        UUID PlayersUUID = player.getUniqueId();
        this.playerGoals.addPlayerGoal(PlayersUUID, playerGoal);

        if (config.getBoolean("enableAnimations"))
            new LineAnimation(
                    new PlayerLocation(player), new StaticLocation(playerGoal.getLocation()),
                    Particle.COMPOSTER, 7.0, 0.05, 0.5, 500, 3
            ).startAnimation();
    }
}
