package wtf.nucker.kitpvpplus.managers;

import org.bukkit.Bukkit;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.intergrations.vault.VaultEcoService;
import wtf.nucker.kitpvpplus.intergrations.vault.VaultIntegrationManager;
import wtf.nucker.kitpvpplus.intergrations.worldguard.WorldGuardBase;
import wtf.nucker.kitpvpplus.intergrations.worldguard.WorldGuardIntegration;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 20/09/2021
 */
public class IntegrationsManager {

    private VaultIntegrationManager vaultManager;
    private WorldGuardBase worldGuard = null;

    public IntegrationsManager() {
        if(this.isWorldGuardEnabled()) {
            worldGuard = new WorldGuardIntegration();
        }
        if(this.isVaultEnabled()) {
            //TODO: Vault eco service & move bank storage settings here
            this.vaultManager = new VaultIntegrationManager();
        }
    }

    public WorldGuardBase getWorldGuard() {
        return  worldGuard;
    }

    public VaultIntegrationManager getVaultManager() {
        return vaultManager;
    }

    public boolean isWorldGuardEnabled() {
        return (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) && KitPvPPlus.getInstance().getSubVersion() >= 12;
    }

    public boolean isVaultEnabled() {
        return this.vaultManager.isVaultEnabled();
    }
}
