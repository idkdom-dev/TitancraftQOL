package me.idkdom.titancraftqol.features;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CauldronConcreteConversion implements Listener {
    private final JavaPlugin plugin;

    /**
     * Cauldron Concrete Conversion constructor
     * @param plugin instance of plugin
     */
    public CauldronConcreteConversion(JavaPlugin plugin) { this.plugin = plugin; }

    /**
     * Converts concrete powder to concrete
     * @param event player drop item event
     */
    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        //Check config before continuing
        if (!plugin.getConfig().getBoolean("cauldron-concrete-conversion.enabled", true)) return;
        //Check to convert
        Item item = event.getItemDrop();
        //20-tick delay
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!item.isValid()) return;
            //Check if in a water cauldron
            Block block = item.getLocation().getBlock();
            if (block.getType() != Material.WATER_CAULDRON) return;
            //Check if stack is concrete powder
            ItemStack stack = item.getItemStack();
            Material material = stack.getType();
            if (!material.name().endsWith("_CONCRETE_POWDER")) return;
            //Solidify the concrete powder
            Material solidify = Material.valueOf(material.name().replace("_POWDER", ""));
            ItemStack newStack = stack.withType(solidify);
            item.setItemStack(newStack);
        }, 20L);
    }

}
