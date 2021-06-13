package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;

public class BnCommand extends BetterNavCommand
{

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages)
    {

        String command = messages.getOrDefault("getlocation_command", ChatColor.RED+"/getlocation");
        String command_1 = messages.getOrDefault("or",ChatColor.WHITE+" or ");
        String command_11 = messages.getOrDefault("toggle_command",ChatColor.RED+"toggle");
        String explanation = messages.getOrDefault("getlocationOrToggleExplanation",ChatColor.GREEN+" shows or hides your current location");

        String command2 = messages.getOrDefault("savelocation_command", ChatColor.RED+"/savelocation <location>");
        String command_2 = messages.getOrDefault("or",ChatColor.WHITE+" or ");
        String command_21 = messages.getOrDefault("save_command", ChatColor.RED+" /save");
        String explanation2 = messages.getOrDefault("savelocationOrSaveExplanation",ChatColor.GREEN+" saves waypoint");

        String command3 = messages.getOrDefault("showlocations_command", ChatColor.RED+"/showlocations");
        String command_3 = messages.getOrDefault("or",ChatColor.WHITE+" or ");
        String command_31 = messages.getOrDefault("showpossiblelocations_command", ChatColor.RED+" /showpossiblelocations");
        String explanation3 = messages.getOrDefault("showlocationsOrShowpossiblelocationsExplanation", ChatColor.GREEN+" shows list of all saved locations");

        String command4 = messages.getOrDefault("showcoordinates_command", ChatColor.RED+"/showcoordinates <location>");
        String command_4 = messages.getOrDefault("or",ChatColor.WHITE+" or ");
        String command_41 = messages.getOrDefault("getcoordinates_command", ChatColor.RED+" /getcoordinates <location>");
        String explanation4 = messages.getOrDefault("showcoordinatesOrGetcoordinatesExplanation", ChatColor.GREEN+" shows coordinates of saved location");

        String command5 = messages.getOrDefault("del_command", ChatColor.RED+"/del <location>");
        String explanation5 = messages.getOrDefault("delExplanation", ChatColor.GREEN+" deletes a location");

        String command6 = messages.getOrDefault("nav_command", ChatColor.RED+ "/nav <location>");
        String command_6 = messages.getOrDefault("or",ChatColor.WHITE+" or ");
        String command_61 = messages.getOrDefault("goto_command", ChatColor.RED+" /goto <location>");
        String explanation6 = messages.getOrDefault("navOrGotoExplanation", ChatColor.GREEN+" start navigation to location");

        String command7 = messages.getOrDefault("navplayer_command", ChatColor.RED+" /navplayer <player>");
        String explanation7 = messages.getOrDefault("navplayerExplanation", ChatColor.GREEN+" start navigating to player");

        String command8 = messages.getOrDefault("stopnav_command", ChatColor.RED+" /stopnav");
        String explanation8 = messages.getOrDefault("stopnavExplanation", ChatColor.GREEN+" stop navigation");

        player.sendMessage(command+command_1+command_11 + explanation);
        player.sendMessage(command2+command_2+command_21+explanation2);
        player.sendMessage(command3+command_3+command_31+explanation3);
        player.sendMessage(command4+command_4+command_41+explanation4);
        player.sendMessage(command5+explanation5);
        player.sendMessage(command6+command_6+command_61+explanation6);
        player.sendMessage(command7+explanation7);
        player.sendMessage(command8+explanation8);

        return true;
    }
}
