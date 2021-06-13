package com.company.betternav.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class BetterNavCommand {

    public abstract boolean execute(Player player, Command cmd, String s, String[] args, Map<String,String> messages);

}
