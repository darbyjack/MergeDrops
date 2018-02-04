package me.glaremasters.mergedrops;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by GlareMasters on 12/26/2017.
 */
public class Listener implements org.bukkit.event.Listener {


    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Map<Material, Integer> materials = new HashMap<>();
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        int total = 0;
        for (ItemStack drops : event.getDrops()) {
            total = total += drops.getAmount();
            if (!(materials.containsKey(drops.getType()))) {
                materials.put(drops.getType(), total);
            } else {
                materials.put(drops.getType(), total);
            }
        }
        event.getDrops().clear();
        for (Material material : materials.keySet()) {
            ItemStack drops = new ItemStack(material, materials.get(material));
            event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), drops);
        }

    }
}
