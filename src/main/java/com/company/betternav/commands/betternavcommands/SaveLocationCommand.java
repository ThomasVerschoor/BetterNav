package com.company.betternav.commands.betternavcommands;

import com.company.betternav.commands.BetterNavCommand;
import com.company.betternav.util.FileHandler;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SaveLocationCommand extends BetterNavCommand {

    private final FileHandler fileHandler;

    public SaveLocationCommand(FileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
    }

    @Override
    public boolean execute(Player player, Command cmd, String s, String[] args) {
        // if location provided
        if (args.length == 1) {
            try {
                String location = args[0];

                fileHandler.writeFile(location,player);


            } catch (IllegalArgumentException e) {
                player.sendMessage("§c§l(!) §cThat is not a valid entity!");
            }
        } else if (args.length > 1) {
            try {
                String location = args[0];
                String X = args[1];
                String Z = args[2];

                player.sendMessage("§c§l(!) §c Location " + location + " saved on: " + X + " " + Z);
                fileHandler.writeFile(location,player);


            } catch (IllegalArgumentException e) {
                player.sendMessage("§c§l(!) §cThat is not a valid entity!");
            }

        } else {
            player.sendMessage("§c§l(!) §cusage ");
            player.sendMessage("§c§l(!) §c/savelocation <name of your location> ");
            player.sendMessage("§c§l(!) §c/savelocation <name of your location> <X coordinate> <Z coordinate> ");
        }
        return true;
    }
}
