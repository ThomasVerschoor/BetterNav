package com.company.betternav.navigation;

import org.bukkit.Location;

public class Goal {

    private final String name;
    private final Location location;

    public Goal(String name, Location location)
    {
        this.name = name;
        this.location = location;
    }


    /**
     * Get the name of this goal
     *
     * @return goal name
     */
    public String getName()
    {
        // String is immutable so can be safely returned, no copy required
        return name;
    }


    /**
     * Get the location of this goal
     *
     * @return the location this goal is pointing to
     */
    public Location getLocation()
    {
        // Location is immutable so can be safely returned, no copy required
        return location;
    }

}
