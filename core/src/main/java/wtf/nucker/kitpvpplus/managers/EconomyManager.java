package wtf.nucker.kitpvpplus.managers;

import net.milkbowl.vault.economy.Economy;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.intergrations.vault.VaultEcoService;
import wtf.nucker.kitpvpplus.intergrations.vault.VaultIntegrationManager;
import wtf.nucker.kitpvpplus.utils.StorageType;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 21/09/2021
 */
public class EconomyManager {

    private final KitPvPPlus instance;
    private final DataManager manager;
    private final StorageType.BankStorageType storageType;

    private boolean usingVault;
    private boolean supplyingVault;
    private Economy vaultEco;

    public EconomyManager(KitPvPPlus instance, DataManager manager, StorageType storageType) {
        this.instance = instance;
        this.manager = manager;

        if(instance.getIntegrationsManager().isVaultEnabled()) {
            this.usingVault = true;
            VaultIntegrationManager vManager = instance.getIntegrationsManager().getVaultManager();
            this.vaultEco = vManager.getEconomyProvider();
            if(!vManager.isProvidingRSP()) {
                this.storageType = StorageType.BankStorageType.VAULT;
                return;
            }
        }

        switch (storageType) {
            case SQL:
                this.storageType = StorageType.BankStorageType.SQL;
                break;
            case MONGO:
                this.storageType = StorageType.BankStorageType.MONGO;
                break;
            default:
                this.storageType = StorageType.BankStorageType.FLAT;
        }
    }

    public StorageType.BankStorageType getStorageType() {
        return storageType;
    }

    public boolean isUsingVault() {
        return usingVault;
    }

    public boolean isSupplyingVault() {
        return supplyingVault;
    }

    public Economy getVaultEco() {
        return vaultEco;
    }
}
