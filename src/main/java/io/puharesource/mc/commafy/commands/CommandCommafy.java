package io.puharesource.mc.commafy.commands;

import io.puharesource.mc.commafy.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCommafy implements CommandExecutor {

    Main plugin;

    public CommandCommafy(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("commafy")) {
            if (sender.isOp()) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "You have reloaded the config!");
            } else sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
            return true;
        }
        return false;
    }
}
