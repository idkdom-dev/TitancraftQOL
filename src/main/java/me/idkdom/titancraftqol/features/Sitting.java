package me.idkdom.titancraftqol.features;

import me.idkdom.titancraftqol.TitancraftQOL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
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
        if (!player.getInventory().getItemInMainHand().isEmpty()) return;

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
        if (blockData instanceof Stairs stairs) {
            return stairs.getHalf() == Bisected.Half.BOTTOM;
        }
        if (blockData instanceof Slab slab) {
            return slab.getType() == Slab.Type.BOTTOM;
        }
        return false;
    }

    private void sitPlayer(Player player, Block block) {
        Location loc = block.getLocation().add(0.5, 0.5, 0.5);
        float yaw = player.getLocation().getYaw();

        if (block.getBlockData() instanceof Stairs stairs) {
            BlockFace facing = stairs.getFacing().getOppositeFace();
            yaw = getYawFromFace(facing);
            switch (facing) {
                case NORTH -> loc.add(0, 0, -0.1);
                case SOUTH -> loc.add(0, 0, 0.1);
                case EAST -> loc.add(0.1, 0, 0);
                case WEST -> loc.add(-0.1, 0, 0);
            }
        }
        loc.setYaw(yaw);

        ArmorStand seat = (ArmorStand) block.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        seat.setInvisible(true);
        seat.setMarker(true);
        seat.setGravity(false);
        seat.setInvulnerable(true);

        seat.addPassenger(player);
    }

    private float getYawFromFace(BlockFace face) {
        return switch (face) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST -> 90f;
            case EAST -> -90f;
            default -> 0f;
        };
    }
}
