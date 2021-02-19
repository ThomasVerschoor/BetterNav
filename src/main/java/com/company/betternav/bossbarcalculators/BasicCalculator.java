package com.company.betternav.bossbarcalculators;

import com.company.betternav.IBossBarCalculator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BasicCalculator implements IBossBarCalculator {

    @Override
    public double calculateBarLevel(Player player, Location goal) {

        // Get player location and looking direction
        Location playerLocation = player.getLocation();
        Vector directionPlayer = playerLocation.getDirection();

        // get viewing direction vector
        Vector viewingDirection = new Vector(directionPlayer.getX(),0,directionPlayer.getZ());

        // normalize the vector
        viewingDirection = viewingDirection.normalize();

        // calculate the vector between current loc and navigation loc
        double x_vector = goal.getX() - playerLocation.getX();
        double y_vector = 0;
        double z_vector = goal.getZ() - playerLocation.getZ();

        // create new vector with xyz
        Vector distanceDirection = new Vector(x_vector,y_vector,z_vector);

        // normalize the direction
        distanceDirection = distanceDirection.normalize();

        // calculate the angle between the vectors
        float angle = viewingDirection.angle(distanceDirection);

        // calculate the mapping to the barlevel
        return angle/Math.PI;
    }

}
