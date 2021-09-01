package wtf.nucker.kitpvpplus.managers;

import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.FlatFile;
import wtf.nucker.kitpvpplus.utils.StorageType;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class PlayerBank {

    private final OfflinePlayer player;
    private static StorageType.BankStorageType storageType;

    public PlayerBank(OfflinePlayer player) {
        this.player = player;
    }

    public double getBal() {
        switch (storageType) {
            case FLAT:
                return KitPvPPlus.getInstance().getDataManager().getDataYaml().getDouble("playerdata." + player.getUniqueId() + ".balance");
            case VAULT:
                return KitPvPPlus.getInstance().getEconomy().bankBalance(player.getName()).balance;
        }

        return 0.0;
    }

    public void setBal(double newAmount) {
        switch (storageType) {
            case FLAT:
                KitPvPPlus.getInstance().getDataManager().getDataYaml().getDouble("playerdata." + player.getUniqueId() + ".balance");
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
                KitPvPPlus.getInstance().getDataManager().getDataYaml().set("playerdata." + player.getUniqueId() + ".balance", this.getBal() + amount);
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
