package me.glaremasters.mergedrops.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.Map;

/**
 * Created by GlareMasters on 12/26/2017.
 */
public class MobDeathEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDeath(EntityDeathEvent event) {
        Map<Material, Integer> materials = new HashMap<>();
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        int total = 0;
        for (ItemStack drops : event.getDrops()) {
            total += drops.getAmount();
            materials.put(drops.getType(), total);
        }
        event.getDrops().clear();

        for (Material material : materials.keySet()) {
            ItemStack drops = new ItemStack(material, materials.get(material));

            if (drops.getAmount() > 64) {
                ArrayList<String> lore = new ArrayList<String>();
                ItemMeta meta = drops.getItemMeta();
                lore.add("MergeDrops " + String.valueOf(drops.getAmount()));
                meta.setLore(lore);
                drops.setItemMeta(meta);
                drops.setAmount(1);
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), drops);
                return;
            }

            event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), drops);
        }

    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return;
        }
        ItemMeta im = item.getItemMeta();
        String check = im.getLore().get(0);
        if (check.startsWith("MergeDrops")) {
            String[] values = check.split(" ");
            int amount = Integer.parseInt(values[1]);
            ItemStack updatedItem = new ItemStack(
                    Material.getMaterial(event.getItem().getItemStack().getType().toString()),
                    amount);
            event.getPlayer().getInventory().addItem(updatedItem);
            event.setCancelled(true);
            event.getItem().remove();
        }
    }


    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {

        if (event.getSource().getType() == InventoryType.HOPPER) {
            if (event.getDestination().getType() == InventoryType.CHEST) {
                ItemStack item = event.getItem();
                if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
                    return;
                }
                ArrayList<String> lore = new ArrayList<String>();
                ItemMeta im = item.getItemMeta();
                String check = im.getLore().get(0);
                if (check.startsWith("MergeDrops")) {
                    String[] values = check.split(" ");
                    int amount = Integer.parseInt(values[1]);
                    lore.add(null);
                    im.setLore(lore);
                    event.getItem().setItemMeta(im);
                    ItemStack updatedItem = new ItemStack(Material.getMaterial(event.getItem().getType().toString()), amount);
                    event.getDestination().addItem(updatedItem);

                }
            }
        }

    }
}
