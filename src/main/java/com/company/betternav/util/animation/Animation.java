package com.company.betternav.util.animation;

import com.company.betternav.util.animation.location.IVariableLocation;
import org.bukkit.Location;

/*

    Abstract class : animation template

 */

public abstract class Animation
{

    private final IVariableLocation location;
    protected boolean isPlaying = false;

    public Animation(IVariableLocation location)
    {
        this.location = location;
    }

    public Location getOrigin()
    {
        return location.getLocation();
    }

    public void startAnimation()
    {
        this.isPlaying = true;
    }

    public void stopAnimation()
    {
        this.isPlaying = false;
    }

}
