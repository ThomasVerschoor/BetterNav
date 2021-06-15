package com.company.betternav.util.animation;

import com.company.betternav.BetterNav;
import com.company.betternav.util.animation.location.IVariableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

public class LineAnimation extends Animation
{

    private final IVariableLocation goal;

    private final Particle particle;
    private final double length;
    private final double stepSize;
    private final double offset;
    private final int durationMilliseconds;
    private final int iterations;

    private final long delay;

    public LineAnimation(IVariableLocation location, IVariableLocation goal, Particle particle, double length, double stepSize, int durationMilliseconds)
    {
        this(location, goal, particle, length, stepSize, 0.0, durationMilliseconds, 0);
    }

    /**
     * This animation will animate a line of particles between two given locations (IVariableLocation so moving locations are supported).
     * @param start The starting location of the animation
     * @param end The ending location of this animation
     * @param particle The Particle that will be spawned
     * @param length How long the animated line must be
     * @param stepSize The distance between each spawned particle, has no effect on animation duration
     * @param offset How far from the player the first particle should spawn
     * @param durationMilliseconds Duration of the full animation, determines the delay between particle spawns
     * @param iterations The number of times that the full animation is ran before stopping
     */
    public LineAnimation(IVariableLocation start, IVariableLocation end, Particle particle, double length, double stepSize, double offset, int durationMilliseconds, int iterations)
    {
        super(start);

        this.goal = end;

        this.particle = particle;
        this.length = length;
        this.stepSize = stepSize;
        this.offset = offset;
        this.durationMilliseconds = durationMilliseconds;
        this.iterations = iterations;

        int numSteps = (int) (length / stepSize);
        this.delay = durationMilliseconds / numSteps;
    }

    @Override
    public void startAnimation()
    {
        // Handle the isPlaying variable
        super.startAnimation();

        // Do calculations async
        Bukkit.getScheduler().runTaskAsynchronously(BetterNav.getInstance(), () -> {

            for (int i = 0; i < this.iterations; i++)
            for (double t = this.offset; t < length + this.offset; t += stepSize)
            {
                // Stop execution when the animation is stopped
                if (!super.isPlaying) return;

                Location origin = getOrigin();
                Location goal = this.goal.getLocation();

                double deltaX = goal.getX() - origin.getX();
                double deltaY = goal.getY() - origin.getY();
                double deltaZ = goal.getZ() - origin.getZ();

                // Normalize the delta's to make our length and stepSize accurate
                double norm = Math.sqrt( Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2) );
                deltaX = deltaX / norm;
                deltaY = deltaY / norm;
                deltaZ = deltaZ / norm;

                double x = origin.getX() + t * deltaX;
                double y = origin.getY() + t * deltaY + 1;
                double z = origin.getZ() + t * deltaZ;

                // Make sure world is not null and goal + origin are in the same world
                if (origin.getWorld() == null) return;
                if (origin.getWorld() != goal.getWorld()) return;

                Location spawnLocation = new Location(origin.getWorld(), x, y, z);
                origin.getWorld().spawnParticle(particle, spawnLocation, 1);

                try {
                    Thread.sleep( delay );
                } catch (InterruptedException ignored) {}
            }

            // Mark the animation as finished
            super.isPlaying = false;
        });
    }
}
