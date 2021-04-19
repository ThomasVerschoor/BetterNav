package com.company.betternav.util.animation;

import com.company.betternav.BetterNav;
import com.company.betternav.util.animation.location.IVariableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class SpiralAnimation extends Animation {

    private final Particle particle;
    private final double radius;
    private final double height;
    private final int durationMilliseconds;
    private final int numParticlesPerRevolution;
    private final double yIncrement;

    public SpiralAnimation(IVariableLocation location, Particle particle, double radius, double height, int durationMilliseconds, int numParticlesPerRevolution, double numCircles)
    {
        super(location);
        this.particle = particle;
        this.radius = radius;
        this.height = height;
        this.durationMilliseconds = durationMilliseconds;
        this.numParticlesPerRevolution = numParticlesPerRevolution;
        this.yIncrement = height / (numCircles * numParticlesPerRevolution);
    }

    @Override
    public void startAnimation()
    {
        Bukkit.getScheduler().runTaskAsynchronously(BetterNav.getInstance(), () -> {
            double tIncrement = 2*Math.PI / numParticlesPerRevolution;
            double numSpawns = (height / (yIncrement * numParticlesPerRevolution)) * numParticlesPerRevolution;
            long timePerSpawn = (long) (durationMilliseconds / numSpawns);

            double yOffset = 0;
            while( yOffset <= height)
            {
                for (double t = 0; t < 2 * Math.PI && yOffset <= height; t += tIncrement) {

                    Location origin = getOrigin();
                    World world = origin.getWorld();
                    if (world == null) return;

                    double xOffset = this.radius * Math.sin(t);
                    double zOffset = this.radius * Math.cos(t);

                    double x = origin.getX() + xOffset;
                    double y = origin.getY() + yOffset;
                    double z = origin.getZ() + zOffset;

                    Location particleLocation = new Location(world, x, y, z);
                    world.spawnParticle(particle, particleLocation, 1);

                    yOffset += yIncrement;

                    try {
                        Thread.sleep(timePerSpawn);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }
}
