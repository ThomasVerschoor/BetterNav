package com.company.betternav.bossbarcalculators;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class IdeaBossBarCalculator implements IBossBarCalculator
{

    @Override
    public double calculateBarLevel(Player player, Location goal)
    {

        // Get player location and looking direction
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();

        // get viewing direction vector
        Vector viewingDirection = new Vector(playerDirection.getX(),0,playerDirection.getZ()).normalize();

        // calculate the vector between current loc and navigation loc
        double x_vector = goal.getX() - playerLocation.getX();
        double y_vector = 0;
        double z_vector = goal.getZ() - playerLocation.getZ();
        Vector goalDirection = new Vector(x_vector,y_vector,z_vector).normalize();

        // calculate the angle between the vectors
        // See: https://stackoverflow.com/questions/53970131/how-to-find-the-clockwise-angle-between-two-vectors-in-python
        double theta = -Math.asin(
                (
                        viewingDirection.getX() * goalDirection.getZ() -
                                viewingDirection.getZ() * goalDirection.getX()
                ) / (
                        viewingDirection.length() * goalDirection.length()
                )
        );

        // Apply the desired range
        theta = (theta + Math.PI/2) / (Math.PI);

        double bossbarLevel;
        boolean isInversed = viewingDirection.dot( goalDirection ) < 0;
        if (isInversed)
        {
            if (theta < 0.5)
                bossbarLevel = 0;
            else
                bossbarLevel = 1;
        }
        else
        {
            bossbarLevel = theta;
        }

        return 1-bossbarLevel;
    }

}
