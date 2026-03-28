package me.idkdom.titancraftqol.features;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class VaultResetting implements Listener {
    private final JavaPlugin plugin;
    private final Set<Location> processedVaults = new HashSet<>();

    public VaultResetting(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void resetVault(Block block) {
        BlockData blockData = block.getBlockData();
        block.setType(Material.AIR);

        Bukkit.getScheduler().runTask(plugin, () -> {
                    block.setType(Material.VAULT);
                    block.setBlockData(blockData);
                });
    }

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
