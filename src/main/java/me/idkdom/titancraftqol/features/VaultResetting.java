package me.idkdom.titancraftqol.features;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.Queue;

public class VaultResetting implements Listener {
    private final JavaPlugin plugin;

    public VaultResetting(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean featureEnabled() {
        return plugin.getConfig().getBoolean("vault-resetting.enabled", true);
    }

    public void resetVault(Block block) {
        BlockData blockData = block.getBlockData();
        block.setType(Material.AIR);

        Bukkit.getScheduler().runTask(plugin, () -> {
                    block.setType(Material.VAULT);
                    block.setBlockData(blockData);
                });
    }

    public void resetAllVaults(World world) {
        if (featureEnabled()) {
            WorldBorder border = world.getWorldBorder();
            Location center = border.getCenter();
            double size = border.getSize() / 2;

            int minChunkX = (int) (center.getX() - size) >> 4;
            int maxChunkX = (int) (center.getX() + size) >> 4;
            int minChunkZ = (int) (center.getZ() - size) >> 4;
            int maxChunkZ = (int) (center.getZ() + size) >> 4;
            Queue<int[]> chunkQueue = new LinkedList<>();

            for (int x = minChunkX; x <= maxChunkX; x++) {
                for (int z = minChunkZ; z <= maxChunkZ; z++) {
                    chunkQueue.add(new int[]{x, z});
                }
            }

            Bukkit.getScheduler().runTaskTimer(plugin, task -> {
                int chunksToCheck = 5;
                for (int i = 0; i < chunksToCheck; i++) {
                    if (chunkQueue.isEmpty()) {
                        task.cancel();
                        return;
                    }

                    int[] chunkCoords = chunkQueue.poll();
                    if (!world.isChunkLoaded(chunkCoords[0], chunkCoords[1])) continue;
                    Chunk chunk = world.getChunkAt(chunkCoords[0], chunkCoords[1]);

                    if (!plugin.getConfig().getBoolean("vault-resetting.reset-ungenerated-chunks", true)) {
                        if (!world.isChunkGenerated(chunkCoords[0], chunkCoords[1])) continue;
                    }

                    for (BlockState tile : chunk.getTileEntities()) {
                        if (tile.getType() == Material.VAULT) {
                            resetVault(tile.getBlock());
                        }
                    }
                }
            }, 0L, 1L);
        }
    }


}
