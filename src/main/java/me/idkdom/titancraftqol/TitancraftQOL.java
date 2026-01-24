package me.idkdom.titancraftqol;

import me.idkdom.titancraftqol.features.*;
import org.bukkit.event.HandlerList;
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

        //Register command
        QolCommand command = new QolCommand(this);
        getCommand("titancraftqol").setExecutor(command);
        getCommand("titancraftqol").setTabCompleter(command);

        //Register features
        reloadFeatures();

        getLogger().info("TitancraftQOL enabled!");
    }

    /**
     * Registers all the functionalities of the plugin
     */
    public void reloadFeatures() {
        HandlerList.unregisterAll(this);

        //Silent Mobs
        if (getConfig().getBoolean("silent-mobs.enabled")) {
            SilentMobs silentMobs = new SilentMobs(this);
            getServer().getPluginManager().registerEvents(silentMobs, this);
            silentMobs.updateAllEntities();
        }
        //Anti Enderman Grief
        if (getConfig().getBoolean("anti-enderman-grief.enabled")) {
            getServer().getPluginManager().registerEvents(new AntiEndermanGrief(this), this);
        }
        //No Anvil Limit
        if (getConfig().getBoolean("no-anvil-limit.enabled")) {
            getServer().getPluginManager().registerEvents(new NoAnvilLimit(this), this);
        }
        //Cauldron Concrete Conversion
        if (getConfig().getBoolean("cauldron-concrete-conversion.enabled")) {
            getServer().getPluginManager().registerEvents(new CauldronConcreteConversion(this), this);
        }
        //Silk Touch
        getServer().getPluginManager().registerEvents(new SilkTouch(this), this);
        //Baby Mobs
        if (getConfig().getBoolean("baby-mobs.enabled")) {
            BabyMobs babyMobs = new BabyMobs(this);
            getServer().getPluginManager().registerEvents(babyMobs, this);
            babyMobs.updateAllEntities();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
