package com.company.betternav.util.animation.location;

import org.bukkit.Location;

/*

    Return static location

 */

public class StaticLocation implements IVariableLocation
{

    private final Location location;

    public StaticLocation(Location location)
    {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
