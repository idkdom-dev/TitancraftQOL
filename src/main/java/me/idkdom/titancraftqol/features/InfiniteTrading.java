package me.idkdom.titancraftqol.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class InfiniteTrading implements Listener {
    private final JavaPlugin plugin;

    /**
     * Infinite trading constructor
     * @param plugin instance of plugin
     */
    public InfiniteTrading(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Method to allow infinite trades on acquire trade event
     * @param event villager acquire trade event
     */
    @EventHandler
    public void onVillagerTrade(VillagerAcquireTradeEvent event) {
        MerchantRecipe recipe = event.getRecipe();
        recipe.setMaxUses(Integer.MAX_VALUE);
        recipe.setUses(0);
    }

    /**
     * Method to reset trades to be infinite on replenish
     * @param event villager replenish trade event
     */
    @EventHandler
    public void onVillagerReplenish(VillagerReplenishTradeEvent event) {
        MerchantRecipe recipe = event.getRecipe();
        recipe.setMaxUses(Integer.MAX_VALUE);
    }
}
