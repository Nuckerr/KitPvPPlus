package wtf.nucker.kitpvpplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.KitLoadEvent;
import wtf.nucker.kitpvpplus.api.objects.*;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.objects.Ability;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public class APIConversion {

    public static Ability toInstanceAbility(wtf.nucker.kitpvpplus.api.objects.Ability ability) {
        return new Ability(ability.getId(), ability.getAbilityItem()) {
            @Override
            public void onActivate(Ability ability, ItemStack item, PlayerInteractEvent listener) {
                ability.onActivate(ability, item, listener);
            }
        };
    }

    public static Kit fromInstanceKit(wtf.nucker.kitpvpplus.objects.Kit kit) {
        return new Kit() {
            @Override
            public String getId() {
                return kit.getId();
            }

            @Override
            public int getPrice() {
                return kit.getPrice();
            }

            @Override
            public boolean isFree() {
                return kit.isFree();
            }

            @Override
            public int getCooldown() {
                return kit.getCooldown();
            }

            @Override
            public Material getIcon() {
                return kit.getIcon();
            }

            @Override
            public List<String> getLore() {
                return kit.getLore();
            }

            @Override
            public String getDisplayname() {
                return kit.getDisplayname();
            }

            @Override
            public String getPermission() {
                return kit.getPermission();
            }

            @Override
            public void loadKit(Player receiver, Player loader) {
                kit.fillInventory(receiver);
                Bukkit.getServer().getPluginManager().callEvent(new KitLoadEvent(this, receiver, loader));
            }

            @Override
            public void loadKit(Player player) {
                kit.fillInventory(player);
                Bukkit.getServer().getPluginManager().callEvent(new KitLoadEvent(this, player, player));
            }
        };
    }

    public static wtf.nucker.kitpvpplus.api.objects.Ability fromInstanceAbility(Ability ability) {
        return new wtf.nucker.kitpvpplus.api.objects.Ability(ability.getId(), ability.getItem()) {
            @Override
            public void onActivate(ItemStack item, wtf.nucker.kitpvpplus.api.objects.Ability ability, PlayerInteractEvent event) {
                ability.onActivate(item, ability, event);
            }
        };
    }

    public static PlayerState fromInstanceState(wtf.nucker.kitpvpplus.dataHandelers.PlayerState state) {
        switch (state) {
            case SPAWN:
                return PlayerState.SPAWN;
            case ARENA:
                return PlayerState.ARENA;
            case PROTECTED:
                return PlayerState.PROTECTED;
            default:
                return null;
        }
    }

    public static wtf.nucker.kitpvpplus.dataHandelers.PlayerState toInstanceState(PlayerState state) {
        switch (state) {
            case SPAWN:
                return wtf.nucker.kitpvpplus.dataHandelers.PlayerState.SPAWN;
            case ARENA:
                return wtf.nucker.kitpvpplus.dataHandelers.PlayerState.ARENA;
            case PROTECTED:
                return wtf.nucker.kitpvpplus.dataHandelers.PlayerState.PROTECTED;
            default:
                return null;
        }
    }

    public static PlayerData fromInstanceData(wtf.nucker.kitpvpplus.dataHandelers.PlayerData data) {
        return new PlayerData() {
            @Override
            public int getExp() {
                return data.getExp();
            }

            @Override
            public int getKills() {
                return data.getKills();
            }

            @Override
            public int getDeaths() {
                return data.getDeaths();
            }

            @Override
            public int getLevel() {
                return data.getLevel();
            }

            @Override
            public int getKillStreak() {
                return data.getKillStreak();
            }

            @Override
            public int getTopKillStreak() {
                return data.getTopKillStreak();
            }

            @Override
            public double getKDR() {
                return data.getKDR();
            }

            @Override
            public PlayerState getState() {
                return APIConversion.fromInstanceState(data.getState());
            }

            @Override
            public List<Kit> getOwnedKits() {
                List<Kit> res = new ArrayList<>();
                data.getOwnedKits().forEach(kit -> res.add(APIConversion.fromInstanceKit(kit)));

                return res;
            }

            @Override
            public boolean ownsKit(Kit kit) {
                return this.getOwnedKits().contains(kit);
            }

            @Override
            public List<Kit> purchaseKit(Kit kit) {
                List<wtf.nucker.kitpvpplus.objects.Kit> kits = data.purchaseKit(APIConversion.toInstanceKit(kit));
                List<Kit> res = new ArrayList<>();

                kits.forEach(k -> res.add(APIConversion.fromInstanceKit(k)));

                return res;
            }

            @Override
            public void setState(PlayerState state) {
                data.setState(APIConversion.toInstanceState(state));
            }

            @Override
            public void updateLevel() {
                data.updateLevel();
            }

            @Override
            public void addExp(int newAmount) {
                data.updateExp(newAmount);
            }

            @Override
            public OfflinePlayer getPlayer() {
                return data.getPlayer();
            }

            @Override
            public double getBal() {
                return new PlayerBank(data.getPlayer()).getBal();
            }
        };
    }

    public static Leaderboard fromInstanceLeaderboard(wtf.nucker.kitpvpplus.objects.Leaderboard leaderboard) {
        return new Leaderboard() {
            @Override
            public List<LeaderboardValue> getTop(int amount) {
                List<LeaderboardValue> res = new ArrayList<>();
                leaderboard.getTop(amount).forEach(value -> {
                    res.add(new LeaderboardValue(value.getPlayer(), value.getValue()));
                });

                return res;
            }

            @Override
            public int getPlace(OfflinePlayer player) {
                return leaderboard.getPlace(player);
            }

            @Override
            public List<LeaderboardValue> getList() {
                List<LeaderboardValue> res = new ArrayList<>();
                leaderboard.getList().forEach(value -> {
                    res.add(new LeaderboardValue(value.getPlayer(), value.getValue()));
                });

                return res;
            }

            @Override
            public String getDisplayname() {
                return leaderboard.getCollumName();
            }

            @Override
            public String getId() {
                return leaderboard.getId();
            }
        };
    }

    private static wtf.nucker.kitpvpplus.objects.Kit toInstanceKit(Kit kit) {
        return KitPvPPlus.getInstance().getKitManager().getKit(kit.getId());
    }
}
