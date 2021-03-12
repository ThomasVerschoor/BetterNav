package com.company.betternav.commands;

import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BetterNavCommand {

    public abstract boolean execute(Player player, Command cmd, String s, String[] args);

}
