package me.idkdom.titancraftqol.features;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SilkTouch implements Listener {
    private final JavaPlugin plugin;

    /**
     * Silk Touch constructor
     * @param plugin instance of plugin
     */
    public SilkTouch(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        if (type != Material.REINFORCED_DEEPSLATE && type != Material.BUDDING_AMETHYST) {
            return;
        }
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0 && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (plugin.getConfig().getBoolean("silk-touch.reinforced-deepslate", true) && type == Material.REINFORCED_DEEPSLATE) {
                event.setDropItems(false);
                ItemStack drop = new ItemStack(type);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
            if (plugin.getConfig().getBoolean("silk-touch.budding-amethyst", true) && type == Material.BUDDING_AMETHYST) {
               event.setDropItems(false);
               ItemStack drop = new ItemStack(type);
               event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
        }
    }

}
