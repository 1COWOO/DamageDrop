package io.github._1cowoo.damagedrop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class DamageDrop extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("플러그인이 활성화 됐습니다.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {

            // 인벤토리에서 아이템이 들어있는 슬롯 모으기
            List<Integer> slots = new ArrayList<>();
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    slots.add(i);
                }
            }

            // 아이템이 없으면 중단
            if (slots.isEmpty()) {
                return;
            }

            // 랜덤 슬롯 하나 선택
            Random random = new Random();
            int randomSlot = slots.get(random.nextInt(slots.size()));
            ItemStack itemToDrop = player.getInventory().getItem(randomSlot);

            if (itemToDrop != null && itemToDrop.getType() != Material.AIR) {
                ItemStack dropOne = itemToDrop.clone();
                dropOne.setAmount(1);

                int amountLeft = itemToDrop.getAmount() - 1;
                if (amountLeft > 0) {
                    itemToDrop.setAmount(amountLeft);
                } else {
                    player.getInventory().setItem(randomSlot, null);
                }
                ItemMeta meta = dropOne.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName("§c에베베 못먹겠징");
                    dropOne.setItemMeta(meta);
                }

                Item droppedItem = player.getWorld().dropItem(player.getLocation(), dropOne);
                droppedItem.setPickupDelay(Integer.MAX_VALUE);
                droppedItem.setCustomName(dropOne.getItemMeta().getDisplayName());
                droppedItem.setCustomNameVisible(true);


                // 드롭 후 줍지 못하게 설정
                Bukkit.getScheduler().runTaskLater(this, droppedItem::remove, 20 * 60);

            }


        }
    }

    @Override
    public void onDisable() {
        getLogger().info("플러그인이 비활성화 됐습니다.");
    }
}
