package me.idkdom.titancraftqol;

import me.idkdom.titancraftqol.features.*;
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
        getConfig().addDefault("cauldron-concrete-conversion.enabled", false);
        getConfig().addDefault("silk-touch.reinforced-deepslate", false);
        getConfig().addDefault("silk-touch.budding-amethyst", false);
        getConfig().addDefault("baby-mobs.enabled", false);
        getConfig().addDefault("baby-mobs.name", "baby");
        getConfig().options().copyDefaults(true);
        saveConfig();
        //Register features
        //Silent Mobs
        SilentMobs silentMobs = new SilentMobs(this);
        getServer().getPluginManager().registerEvents(silentMobs, this);
        silentMobs.updateAllEntities();
        //Anti Enderman Grief
        getServer().getPluginManager().registerEvents(new AntiEndermanGrief(this), this);
        //No Anvil Limit
        getServer().getPluginManager().registerEvents(new NoAnvilLimit(this), this);
        //Cauldron Concrete Conversion
        getServer().getPluginManager().registerEvents(new CauldronConcreteConversion(this), this);
        //Silk Touch
        getServer().getPluginManager().registerEvents(new SilkTouch(this), this);
        //Baby Mobs
        BabyMobs babyMobs = new BabyMobs(this);
        getServer().getPluginManager().registerEvents(babyMobs, this);
        babyMobs.updateAllEntities();

        //Register commands
        getCommand("titancraftqol").setExecutor((sender, command, label, args) -> {
            reloadConfig();
            silentMobs.updateAllEntities();
            babyMobs.updateAllEntities();
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
