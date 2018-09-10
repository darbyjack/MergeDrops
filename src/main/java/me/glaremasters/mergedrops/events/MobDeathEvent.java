package me.glaremasters.mergedrops.events;

import me.glaremasters.mergedrops.Mergedrops;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GlareMasters
 * Date: 9/10/2018
 * Time: 4:34 PM
 */
public class MobDeathEvent implements Listener {

    private Mergedrops mergedrops;

    public MobDeathEvent(Mergedrops mergedrops) {
        this.mergedrops = mergedrops;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDeath(EntityDeathEvent event) {
        FileConfiguration c = mergedrops.getConfig();
        Map<Material, Integer> materials = new HashMap<>();
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Monster)) return;
        int total = 0;
        for (ItemStack drops : event.getDrops()) {
            total += drops.getAmount();
            if (c.getStringList("materials").contains(drops.getType().name())) {
                materials.put(drops.getType(), total);
            }
        }
        event.getDrops().clear();
        for (Material material : materials.keySet()) {
            ItemStack drops = new ItemStack(material, materials.get(material));
            if (drops.getAmount() > 64) {
                List<String> lore = new ArrayList<>();
                ItemMeta meta = drops.getItemMeta();
                lore.add("MergeDrops " + String.valueOf(drops.getAmount()));
                meta.setLore(lore);
                drops.setItemMeta(meta);
                drops.setAmount(1);
                entity.getWorld().dropItem(entity.getLocation(), drops);
                return;
            }
            entity.getWorld().dropItem(entity.getLocation(), drops);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return;
        ItemMeta im = item.getItemMeta();
        String check = im.getLore().get(0);
        if (check.startsWith("MergeDrops")) {
            String[] values = check.split(" ");
            int amount = Integer.parseInt((values[1]));
            ItemStack updatedItem = new ItemStack(Material.getMaterial(event.getItem().getItemStack().getType().name()), amount);
            event.getPlayer().getInventory().addItem(updatedItem);
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getSource().getType() != InventoryType.HOPPER) return;
        if (event.getDestination().getType() != InventoryType.CHEST) return;
        ItemStack item = event.getItem();
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return;
        List<String> lore = new ArrayList<>();
        ItemMeta im = item.getItemMeta();
        String check = im.getLore().get(0);
        if (check.startsWith("MergeDrops")) {
            String[] values = check.split(" ");
            int amount = Integer.parseInt(values[1]);
            lore.add(null);
            im.setLore(lore);
            event.getItem().setItemMeta(im);
            ItemStack updatedItem = new ItemStack(Material.getMaterial(event.getItem().getType().name()), amount);
            event.getDestination().addItem(updatedItem);

        }
    }

}
