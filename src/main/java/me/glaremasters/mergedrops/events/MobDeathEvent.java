package me.glaremasters.mergedrops.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by GlareMasters on 12/26/2017.
 */
public class MobDeathEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDeath(EntityDeathEvent event) {
        Map<Material, Integer> materials = new HashMap<>();
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
    public void onPickup(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return;
        }
        ItemMeta im = item.getItemMeta();
        String check = im.getLore().get(0);
        if (check.startsWith("MergeDrops")) {
            String[] values = check.split(" ");
            int amount = Integer.parseInt(values[1]);
            if (event.getEntity() instanceof Player) {
                Player player = ((Player) event.getEntity()).getPlayer();
                ItemStack updatedItem = new ItemStack(
                        Material.getMaterial(event.getItem().getItemStack().getType().toString()),
                        amount);
                player.getInventory().addItem(updatedItem);
                event.setCancelled(true);
                event.getItem().remove();
            }

        }
    }
}
