package me.idkdom.titancraftqol;

import me.idkdom.titancraftqol.commands.QolCommand;
import me.idkdom.titancraftqol.commands.SitCommand;
import me.idkdom.titancraftqol.features.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public final class TitancraftQOL extends JavaPlugin {

    private Set<UUID> sitEnabled = new HashSet<>();

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
        getConfig().addDefault("vault-resetting.enabled", false);
        getConfig().addDefault("infinite-trading.enabled", false);
        getConfig().addDefault("sitting.enabled", false);
        getConfig().addDefault("sitting.list", new ArrayList<>());
        getConfig().options().copyDefaults(true);
        saveConfig();

        //Load config
        List<String> list = getConfig().getStringList("sitting.list");
        sitEnabled = list.stream().map(UUID::fromString).collect(Collectors.toSet());

        //Register commands
        QolCommand qolCommand = new QolCommand(this);
        getCommand("titancraftqol").setExecutor(qolCommand);
        getCommand("titancraftqol").setTabCompleter(qolCommand);

        SitCommand sitCommand = new SitCommand(this);
        getCommand("sit").setExecutor(sitCommand);

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
        //Vault Resetting
        if (getConfig().getBoolean("vault-resetting.enabled")) {
            getServer().getPluginManager().registerEvents(new VaultResetting(this), this);
        }
        //Infinite trading
        if (getConfig().getBoolean("infinite-trading.enabled")) {
            getServer().getPluginManager().registerEvents(new InfiniteTrading(this), this);
        }
        //Sitting
        if (getConfig().getBoolean("sitting.enabled")) {
            getServer().getPluginManager().registerEvents(new Sitting(this), this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean isSittingEnabled(Player player) {
        return sitEnabled.contains(player.getUniqueId());
    }

    public boolean toggleSitting(Player player) {
        UUID uuid = player.getUniqueId();
        boolean enabled;

        if (sitEnabled.contains(uuid)) {
            sitEnabled.remove(uuid);
            enabled = false;
        } else {
            sitEnabled.add(uuid);
            enabled = true;
        }

        saveSittingDate();
        return enabled;
    }

    public void saveSittingDate() {
        List<String> list = sitEnabled.stream().map(UUID::toString).collect(Collectors.toList());
        getConfig().set("sitting.list", list);
        saveConfig();
    }
}
