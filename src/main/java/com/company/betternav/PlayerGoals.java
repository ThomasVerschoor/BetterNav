package com.company.betternav;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerGoals {

    private final Map<UUID, Goal> playerGoals;

    public PlayerGoals()
    {
        this.playerGoals = new HashMap<>();
    }


    /**
     * Add a Player UUID and a corresponding goal
     *
     * @param uuid the UUID of the player that set a goal
     * @param goal the goal Location of this player
     * @return null if the player previously had no goal, otherwise the previous goal is returned
     */
    public Goal addPlayerGoal(UUID uuid, Goal goal)
    {
        return this.playerGoals.put( uuid, goal );
    }


    /**
     * Get the goal of the player with given UUID
     * Will return null if the player has no Goal at this time
     *
     * @param uuid the UUID of the player
     * @return the player's goal, null if player has no goal
     */
    public Goal getPlayerGoal(UUID uuid)
    {
        return playerGoals.get( uuid );
    }


    /**
     * Remove a player's goal from storage
     *
     * @param uuid the player whose goal should be wiped
     * @return the Goal this player had, null if Player had no goal
     */
    public Goal removePlayerGoal(UUID uuid)
    {
        return playerGoals.remove( uuid );
    }

}
