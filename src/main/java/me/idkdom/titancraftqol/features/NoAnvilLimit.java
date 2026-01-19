package me.idkdom.titancraftqol.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class NoAnvilLimit implements Listener {
    private final JavaPlugin plugin;

    public NoAnvilLimit(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("removal")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        if (!plugin.getConfig().getBoolean("no-anvil-limit.enabled", true)) {
            anvil.setMaximumRepairCost(39);
            return;
        }
        anvil.setMaximumRepairCost(Integer.MAX_VALUE);
    }
}
