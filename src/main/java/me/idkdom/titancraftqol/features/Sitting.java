package me.idkdom.titancraftqol.features;

import me.idkdom.titancraftqol.TitancraftQOL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Sitting implements Listener {
    private final TitancraftQOL plugin;

    public Sitting(TitancraftQOL plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (!plugin.isSittingEnabled(player)) return;

        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!isValidSeat(block)) return;
        if (player.isInsideVehicle()) return;

        event.setCancelled(true);
        sitPlayer(player, block);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getDismounted() instanceof ArmorStand armorStand) {
            Bukkit.getScheduler().runTaskLater(plugin, armorStand::remove, 1L);
        }
    }

    private boolean isValidSeat(Block block) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Stairs) return true;
        if (blockData instanceof Slab slab) {
            return slab.getType() == Slab.Type.BOTTOM;
        }
        return false;
    }

    private void sitPlayer(Player player, Block block) {
        Location loc = block.getLocation().add(0.5, 0.2, 0.5);

        ArmorStand seat = (ArmorStand) block.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        seat.setInvisible(true);
        seat.setMarker(true);
        seat.setGravity(false);
        seat.setInvulnerable(true);

        seat.addPassenger(player);
    }
}
