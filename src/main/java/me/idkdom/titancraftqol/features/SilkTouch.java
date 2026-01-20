package me.idkdom.titancraftqol.features;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
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
        if (event.getBlock().getType() != Material.REINFORCED_DEEPSLATE || event.getBlock().getType() != Material.BUDDING_AMETHYST) {
            return;
        }
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
            if (plugin.getConfig().getBoolean("silk-touch.reinforced-deepslate", true)) {
                event.setDropItems(true);
            }
            if (plugin.getConfig().getBoolean("silk-touch.budding-amethyst", true)) {
                event.setDropItems(true);
            }
        }
    }






}
