package me.idkdom.titancraftqol;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class QolCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;

    /**
     * Qol Command constructor
     * @param plugin instance of plugin
     */
    public QolCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Command handler
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true once command is finished
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("titancraftqol.admin")) {
            sender.sendMessage("§You do not have permission.");
            return true;
        }

        if (args.length == 0) {
            sendRootHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help" -> sendHelp(sender);
            case "reload" -> reload(sender);
            case "config" -> handleConfig(sender, args);
            default -> sender.sendMessage("§cUnknown usage. Try '/titancraftqol help'");
        }

        return true;
    }

    /**
     * Send list of commands
     * @param sender Source of the command
     */
    private void sendRootHelp(CommandSender sender) {
        sender.sendMessage("§6TitancraftQOL Commands:");
        sender.sendMessage("§e/titancraftqol config");
        sender.sendMessage("§e/titancraftqol help");
        sender.sendMessage("§e/titancraftqol reload");
    }

    /**
     * Sends help menu
     * @param sender Source of the command
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6TitancraftQOL Help Menu");
        sender.sendMessage("§e/titancraftqol help");
        sender.sendMessage("§7Opens this menu");
        sender.sendMessage("§e/titancraftqol config <feature> <key> <value>");
        sender.sendMessage("§7Example '/titancraftqol config silent-mobs enabled false'");
        sender.sendMessage("§e/titancraftqol reload");
        sender.sendMessage("§7Reloads the configs and functionality of the plugin");
    }

    /**
     * Sends available configs
     * @param sender Source of the command
     * @param args arguments
     */
    private void handleConfig(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage("§6Available features:");
            plugin.getConfig().getKeys(false).forEach(key ->
                    sender.sendMessage("§e- " + key));
            return;
        }

        String section = args[1];

        if (!plugin.getConfig().isConfigurationSection(section)) {
            sender.sendMessage("§cUnknown config: " + section);
            return;
        }

        // /titancraftqol config <feature>
        if (args.length == 2) {
            sender.sendMessage("§6Configs in §e" + section + "§6: ");
            plugin.getConfig().getConfigurationSection(section).getKeys(false).forEach(key ->
                    sender.sendMessage("§e- " + key));
            return;
        }

        String key = args[2];
        String path = section + "." + key;

        if (!plugin.getConfig().contains(path)) {
            sender.sendMessage("§cUnknown config key " + path);
            return;
        }

        if (args.length == 3) {
            Object value = plugin.getConfig().get(path);
            sender.sendMessage("§6" + path + " §7= §e" + value);
            return;
        }

        if (args.length != 4) {
            sender.sendMessage("§cUsage: '/titancraftqol config <feature> <key> <value>'");
            return;
        }

        // /titancraftqol config <feature> <key>
        Object existing = plugin.getConfig().get(path);
        String rawValue = args[3];
        Object parsedValue;

        //future-proof type-aware parsing
        if (existing instanceof Boolean) {
            if (!rawValue.equalsIgnoreCase("true") && !rawValue.equalsIgnoreCase("false")) {
                sender.sendMessage("§cInvalid value. Expected §etrue§c or §efalse§c.");
                return;
            }
            parsedValue = Boolean.parseBoolean(rawValue);

        } else if (existing instanceof Integer) {
            try {
                parsedValue = Integer.parseInt(rawValue);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid number. Expected an integer.");
                return;
            }

        } else if (existing instanceof Double) {
            try {
                parsedValue = Double.parseDouble(rawValue);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid number. Expected a decimal.");
                return;
            }

        } else if (existing instanceof String) {
            parsedValue = rawValue;

        } else {
            sender.sendMessage("§cThis config type cannot be modified in-game.");
            return;
        }

        plugin.getConfig().set(path, parsedValue);
        plugin.saveConfig();
        plugin.reloadConfig();
        ((TitancraftQOL) plugin).reloadFeatures();

        sender.sendMessage("§aUpdated §e" + path + " §ato §e" + parsedValue);
    }

    /**
     * Reloads configs
     * @param sender Source of the command
     */
    private void reload(CommandSender sender) {
        plugin.reloadConfig();
        ((TitancraftQOL) plugin).reloadFeatures();
        sender.sendMessage("§aTitancraftQOL reloaded.");
    }

    /**
     * Tab completion handler
     * @param sender Source of the command.  For players tab-completing a
     *     command inside of a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param alias Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return List of tab-completable options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("config", "help", "reload");
        }

        if (args[0].equalsIgnoreCase("config")) {
            if (args.length == 2) {
                return new ArrayList<>(plugin.getConfig().getKeys(false));
            }

            if (args.length == 3) {
                String section = args[1];
                if (!plugin.getConfig().isConfigurationSection(section)) return List.of();

                return new ArrayList<>(plugin.getConfig().getConfigurationSection(section).getKeys(false));
            }

            if (args.length == 4) {
                return List.of("true", "false");
            }
        }
        return List.of();
    }
}
