package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;

public class BnCommand extends BetterNavCommand
{

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages) {




        String command = messages.getOrDefault("getlocation_command", ChatColor.RED+"/getlocation");
        String command_1 = messages.getOrDefault("or",ChatColor.WHITE+" or ");
        String command_11 = messages.getOrDefault("toggle_command",ChatColor.RED+"toggle");
        String explanation = messages.getOrDefault("getlocationOrToggleExplanation",ChatColor.GREEN+" shows or hides your current location");

        String command2 = ChatColor.RED+"/savelocation <location>";
        String command_2 = ChatColor.WHITE+" or";
        String command_21 = ChatColor.RED+" /save";
        String explanation2 = ChatColor.GREEN+" saves waypoint";

        String command3 = ChatColor.RED+"/showlocations";
        String command_3 = ChatColor.WHITE+" or";
        String command_31 = ChatColor.RED+" /showpossiblelocations";
        String explanation3 = ChatColor.GREEN+" shows list of all saved locations";

        String command4 = ChatColor.RED+"/showcoordinates <location>";
        String command_4 = ChatColor.WHITE+" or";
        String command_41 = ChatColor.RED+" /getcoordinates <location>";
        String explanation4 = ChatColor.GREEN+" shows coordinates of saved location";

        String command5 = ChatColor.RED+"/del <location>";
        String explanation5 = ChatColor.GREEN+" deletes a location";


        String command6 = ChatColor.RED+ "/nav <location>";
        String command_6 = ChatColor.WHITE+" or";
        String command_61 = ChatColor.RED+" /goto <location>";
        String explanation6 = ChatColor.GREEN+" start navigation to location";


        String command7 = ChatColor.RED+" /navplayer <player>";
        String explanation7 = ChatColor.GREEN+" start navigating to player";

        String command8 = ChatColor.RED+" /stopnav";
        String explanation8 = ChatColor.GREEN+" stop navigation";


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
