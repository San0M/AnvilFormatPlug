package com.github.anvilformatplug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnvilFormatPlug extends JavaPlugin implements Listener {

    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AnvilFormatPlug загружен: форматирование в наковальне активно для всех!");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        String renameText = inventory.getRenameText();

        if (renameText == null || renameText.isEmpty() || !renameText.contains("&")) {
            return;
        }

        ItemStack firstItem = inventory.getItem(0);
        if (firstItem == null || firstItem.getType() == Material.AIR) {
            return;
        }

        ItemStack result = event.getResult();
        if (result == null || result.getType() == Material.AIR) {
            result = firstItem.clone();
        } else {
            result = result.clone();
        }

        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            Component formattedName = serializer.deserialize(renameText);

            // Убираем стандартный курсив, если не указан &o
            if (!renameText.toLowerCase().contains("&o")) {
                formattedName = formattedName.decoration(TextDecoration.ITALIC, false);
            }

            meta.displayName(formattedName);
            result.setItemMeta(meta);
            event.setResult(result);
        }
    }
}
