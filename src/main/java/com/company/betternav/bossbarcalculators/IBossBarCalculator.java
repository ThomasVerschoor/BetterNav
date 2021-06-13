package com.company.betternav.bossbarcalculators;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IBossBarCalculator
{
    double calculateBarLevel(Player player, Location goal);
}
