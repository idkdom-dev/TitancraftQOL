package me.idkdom.titancraftqol;

import me.idkdom.titancraftqol.features.NoAnvilLimit;
import me.idkdom.titancraftqol.features.SilentMobs;
import me.idkdom.titancraftqol.features.AntiEndermanGrief;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitancraftQOL extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig(); //create config if missing
        //Plugin config defaults
        getConfig().addDefault("silent-mobs.enabled", false);
        getConfig().addDefault("silent-mobs.name", "silence");
        getConfig().addDefault("anti-enderman-grief.enabled", false);
        getConfig().addDefault("no-anvil-limit.enabled", false);
        getConfig().options().copyDefaults(true);
        saveConfig();
        //Register features
        //Silent Mobs
        SilentMobs silentMobs = new SilentMobs(this);
        getServer().getPluginManager().registerEvents(silentMobs, this);
        silentMobs.updateAllLoadedEntities();
        //Anti Enderman Grief
        getServer().getPluginManager().registerEvents(new AntiEndermanGrief(this), this);
        //No Anvil Limit
        getServer().getPluginManager().registerEvents(new NoAnvilLimit(this), this);

        //Register commands
        getCommand("titancraftqol").setExecutor((sender, command, label, args) -> {
            reloadConfig();
            silentMobs.updateAllLoadedEntities();
            sender.sendMessage("TitancraftQOL config reloaded!");
            return true;
        });

        getLogger().info("TitancraftQOL enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
