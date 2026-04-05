package me.idkdom.titancraftqol.commands;

import me.idkdom.titancraftqol.TitancraftQOL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SitCommand implements CommandExecutor {
    private final TitancraftQOL plugin;

    public SitCommand(TitancraftQOL plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!sender.hasPermission("titancraftqol.sit")) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (!plugin.getConfig().getBoolean("sitting.enabled")) {
            player.sendMessage("§cSit feature not enabled.");
            return true;
        }

        boolean enabled = plugin.toggleSitting(player);
        if (enabled) {
            player.sendMessage("§aSitting enabled.");
        } else {
            player.sendMessage("§cSitting disabled.");
        }

        return true;
    }
}
