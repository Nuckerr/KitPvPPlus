package wtf.nucker.kitpvpplus.menus;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.KitLoadEvent;
import wtf.nucker.kitpvpplus.exceptions.InsufficientBalance;
import wtf.nucker.kitpvpplus.exceptions.PermissionException;
import wtf.nucker.kitpvpplus.managers.CooldownManager;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.utils.*;
import wtf.nucker.simplemenus.spigot.Button;
import wtf.nucker.simplemenus.spigot.Menu;
import wtf.nucker.simplemenus.spigot.PaginatedMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 17/10/2021
 */
public class KitMenus {

    public static Menu buildPurchaseMenu(Player player, Kit kit) {
        Menu menu = new Menu(5, "&0Purchase " + ChatColor.stripColor(kit.getDisplayname()), true);

        ItemStack icon = new ItemStack(kit.getIcon());
        ItemMeta meta = icon.getItemMeta();
        meta.setLore(ChatUtils.translate(kit.getLore()));
        meta.setDisplayName(ChatUtils.translate(kit.getDisplayname()));
        icon.setItemMeta(meta);
        menu.setItem(13, icon);

        ItemStack purchaseItem = ItemUtils.buildItem("&cNo thanks", XMaterial.RED_WOOL.parseMaterial(),
                1, "&7Click here to cancel your purchase");
        purchaseItem.setDurability(DyeColor.RED.getWoolData());

        menu.setButton(new Button(purchaseItem) {
            @Override
            public void onClick(InventoryClickEvent event) {
                Player p = (Player) event.getWhoClicked();
                p.playSound(p.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1f, 1f);
                p.sendMessage(ChatUtils.translate("&cPurchase canceled"));
                p.closeInventory();
            }
        }, 30);

        ItemStack item = ItemUtils.buildItem("&aPurchase", XMaterial.GREEN_WOOL.parseMaterial(),
                1, "&7Click here to purchase this kit");
        item.setDurability(DyeColor.GREEN.getWoolData());
        menu.setButton(new Button(item) {
            @Override
            public void onClick(InventoryClickEvent event) {
                try {
                    KitPvPPlus.getInstance().getDataManager().getPlayerData((Player) event.getWhoClicked()).purchaseKit(kit);
                    event.getWhoClicked().sendMessage(Language.KIT_PURCHASED.get(player).replace("%kit%", kit.getId()));
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1f, 250f);
                    event.getWhoClicked().closeInventory();
                } catch (InsufficientBalance e) {
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1f, 1f);
                    event.getWhoClicked().sendMessage(Language.INSUFFICIENT_BAL.get((Player) event.getWhoClicked()).replace("%kitname%", kit.getId()));
                    event.getWhoClicked().closeInventory();
                }catch (PermissionException e) {
                    player.sendMessage(Language.PERMISSION_MESSAGE.get(player));
                }
            }
        }, 32);

        menu.fillMenu();

        menu.open(player);
        return menu;
    }

    public static PaginatedMenu buildKitGUI(Player player) {
        List<ItemStack> items = new ArrayList<>();

        for (Kit kit : KitPvPPlus.getInstance().getKitManager().getKits()) {
            if(KitPvPPlus.getInstance().getDataManager().getPlayerData(player).ownsKit(kit)) {
                ItemStack icon = new ItemStack(kit.getIcon());
                ItemMeta meta = icon.getItemMeta();
                meta.setLore(ChatUtils.translate(kit.getLore()));
                meta.setDisplayName(ChatUtils.translate(kit.getDisplayname()));
                icon.setItemMeta(meta);

                NBTItem nbt = new NBTItem(icon);
                nbt.setString("kit", kit.getId());
                icon = nbt.getItem();

                items.add(icon);
            }
        }

        if(items.size() <= 0) {
            ItemStack noKitsItem = XMaterial.RED_STAINED_GLASS.parseItem();
            ItemMeta meta = noKitsItem.getItemMeta();
            meta.setDisplayName(ChatUtils.translate("&cYou dont own any kits"));
            meta.setLore(ChatUtils.translate(Arrays.asList("&cYou dont own any kits. Use the", "&c/kit purchase command to purchase", "&cone.")));
            noKitsItem.setItemMeta(meta);
            for (int i = 0; i < 9; i++) {
                items.add(noKitsItem);
            }
        }

        PaginatedMenu menu = MenuUtils.buildPaginatedGUI("&0Kits", items, event -> {
            ItemStack item = event.getCurrentItem();
            if(!(event.getWhoClicked() instanceof Player)) return;
            Player p = (Player) event.getWhoClicked();
            if(!new NBTItem(item).hasKey("kit")) {
                p.sendMessage(ChatUtils.translate("&cUnable to find kit"));
                return;
            }

            Kit kit = KitPvPPlus.getInstance().getKitManager().getKit(new NBTItem(item).getString("kit").replace("\"", ""));
            if (!KitPvPPlus.getInstance().getDataManager().getPlayerData(p).ownsKit(kit)) {
                p.sendMessage(Language.KIT_NOT_OWNED.get(p).replace("%kitname%", kit.getId()).replace("%permission%", kit.getPermission()));
                return;
            }
            if (CooldownManager.kitCooldown(p, kit)) {
                p.sendMessage(Language.KIT_ON_COOLDOWN.get(p).replace("%kitname%", kit.getId()).replace("%time%", ClockUtils.formatSeconds(kit.getCooldownRunnable().getAmount())));
                return;
            }
            kit.fillInventory(p);
            p.sendMessage(Language.KIT_LOADED.get(p).replace("%kitname%", kit.getId()));
            player.closeInventory();
            if (kit.getCooldown() > 0) CooldownManager.addKitCooldown(p, kit, kit.getCooldown());
            Bukkit.getPluginManager().callEvent(new KitLoadEvent(APIConversion.fromInstanceKit(kit), p, p));
        });

        menu.open(player);
        return menu;
    }
}
