package com.company.betternav.util.animation;

import com.company.betternav.util.animation.location.IVariableLocation;
import org.bukkit.Location;

public abstract class Animation {

    private final IVariableLocation location;

    public Animation(IVariableLocation location)
    {
        this.location = location;
    }

    public Location getOrigin()
    {
        return location.getLocation();
    }

    public abstract void startAnimation();

}
