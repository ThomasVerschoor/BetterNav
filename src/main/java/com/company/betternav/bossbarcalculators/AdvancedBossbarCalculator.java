package com.company.betternav.bossbarcalculators;

import com.company.betternav.IBossBarCalculator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AdvancedBossbarCalculator implements IBossBarCalculator {

    Boolean wasFlipped;

    @Override
    public double calculateBarLevel(Player player, Location goal) {

        // Get player location and looking direction
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();

        // get viewing direction vector
        Vector viewingDirection = new Vector(playerDirection.getX(),0,playerDirection.getZ()).normalize();

        // calculate the vector between current loc and navigation loc
        double x_vector = goal.getX() - playerLocation.getX();
        double y_vector = 0;
        double z_vector = goal.getZ() - playerLocation.getZ();
        Vector distanceDirection = new Vector(x_vector,y_vector,z_vector).normalize();

        // calculate the angle between the vectors
        // See: https://stackoverflow.com/questions/53970131/how-to-find-the-clockwise-angle-between-two-vectors-in-python
//        double a = viewingDirection.getX() * playerDirection.getZ() - viewingDirection.getZ() * playerDirection.getX();
        double theta = -Math.asin(
                (
                        viewingDirection.getX() * distanceDirection.getZ() -
                        viewingDirection.getZ() * distanceDirection.getX()
                ) / (
                    viewingDirection.length() * distanceDirection.length()
                    )
        );

        player.sendMessage("angle: " + theta);
        return (theta + Math.PI/2) / (Math.PI);
    }

}
