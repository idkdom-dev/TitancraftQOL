package me.idkdom.titancraftqol.features;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Vault;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class VaultResetting implements Listener {
    private final JavaPlugin plugin;
    private final Set<Location> processedVaults = new HashSet<>();

    /**
     * Vault Resetting constructor
     * @param plugin instance of plugin
     */
    public VaultResetting(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Method to reset trial vault
     * @param block trial vault block
     */
    public void resetVault(Block block) {
        BlockData blockData = block.getBlockData();

        // Capture vault data and remove the vault block
        Vault vaultState = (Vault) block.getState();
        LootTable rewardTable = vaultState.getLootTable();
        ItemStack keyItem = vaultState.getKeyItem();
        block.setType(Material.AIR);
        // Replace vault and the loot table
        Bukkit.getScheduler().runTask(plugin, () -> {
            block.setType(Material.VAULT);
            block.setBlockData(blockData);

            Vault newVaultState = (Vault) block.getState();
            newVaultState.setLootTable(rewardTable);
            newVaultState.setKeyItem(keyItem);
            newVaultState.update(true);
        });
    }

/**
 * Check for trial vaults on chunk load and reset
 * @param event chunk load event
 */
@EventHandler
public void onChunkLoad(ChunkLoadEvent event) {
    Chunk chunk = event.getChunk();

    for (BlockState tile : chunk.getTileEntities()) {
        if (tile.getType() == Material.VAULT) {
            Location loc = tile.getLocation();
            if (processedVaults.contains(loc)) continue;
            processedVaults.add(loc);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                resetVault(tile.getBlock());
            }, 1L);
        }
    }
}

}
