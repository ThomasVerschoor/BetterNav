package com.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands_Handler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String [] strings){
        if(!(sender instanceof Player)){
            sender.sendMessage("only players can use that command");

            return true;}

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("heal")){
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
            player.setHealth(maxHealth);
            player.sendMessage("You have been healed");
            return true;
        }

        else if(cmd.getName().equalsIgnoreCase("feed")){
            player.setFoodLevel(20);
            player.sendMessage("You have been fed");
        }

        return true;

    }
}
