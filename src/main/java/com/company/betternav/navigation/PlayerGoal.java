package com.company.betternav.navigation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerGoal extends Goal {

    private final Player playerGoal;


    public PlayerGoal(String name, Player playerGoal) {
        super(name, playerGoal.getLocation());
        this.playerGoal = playerGoal;
    }

    @Override
    public Location getLocation() {

        return playerGoal.getLocation();
        //return super.getLocation();
    }




}
