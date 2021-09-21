package wtf.nucker.kitpvpplus.intergrations.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import wtf.nucker.kitpvpplus.KitPvPPlus;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 21/09/2021
 */
public class VaultIntegrationManager {

    private final boolean isVaultEnabled;
    private final boolean providingRSP;
    private Economy eco = null;
    private VaultEcoService ecoService = null;

    public VaultIntegrationManager() {
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            this.isVaultEnabled = true;
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if(rsp == null) {
                this.providingRSP = true;
                this.ecoService = new VaultEcoService(KitPvPPlus.getInstance());
            }else {
                this.providingRSP = false;
                this.eco = rsp.getProvider();
            }
        }else {
            this.isVaultEnabled = false;
            this.providingRSP = false;
        }
    }


    public boolean isVaultEnabled() {
        return isVaultEnabled;
    }

    public boolean isProvidingRSP() {
        return providingRSP;
    }

    public Economy getEconomyProvider() {
        return eco;
    }

    public VaultEcoService getEcoService() {
        return ecoService;
    }
}
