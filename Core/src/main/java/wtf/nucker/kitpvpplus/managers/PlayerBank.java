package wtf.nucker.kitpvpplus.managers;

import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.player.FlatFilePlayerData;
import wtf.nucker.kitpvpplus.utils.StorageType;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class PlayerBank {

    private final Player player;
    private static StorageType.BankStorageType storageType;
    private FlatFilePlayerData playerData;

    public PlayerBank(Player player) {
        this.player = player;
        this.playerData = new FlatFilePlayerData(player);
    }

    public double getBal() {
        switch (storageType) {
            case FLAT:
                return playerData.getSection().getDouble("balance");
            case VAULT:
                return KitPvPPlus.getInstance().getEconomy().bankBalance(player.getName()).balance;
        }

        return 0.0;
    }

    public void setBal(double newAmount) {
        switch (storageType) {
            case FLAT:
                playerData.getSection().set("balance", newAmount);
                KitPvPPlus.getInstance().getDataManager().getDataConfig().save();
                break;
            case VAULT:
                KitPvPPlus.getInstance().getEconomy().bankWithdraw(player.getName(), this.getBal());
                KitPvPPlus.getInstance().getEconomy().depositPlayer(player, newAmount);
                break;
        }
    }

    public void deposit(double amount) {
        switch (storageType) {
            case FLAT:
                playerData.getSection().set("balance", this.getBal() + amount);
                KitPvPPlus.getInstance().getDataManager().getDataConfig().save();
                break;
            case VAULT:
                KitPvPPlus.getInstance().getEconomy().depositPlayer(player, amount);
                break;
        }
    }

    public static StorageType.BankStorageType getStorageType() {
        return PlayerBank.storageType;
    }

    public static void setStorageType(StorageType.BankStorageType storageType) {
        PlayerBank.storageType = storageType;
    }
}
