package wtf.nucker.kitpvpplus;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageKeys;
import co.aikar.locales.MessageKeyProvider;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableList;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.nucker.kitpvpplus.abilities.Fireball;
import wtf.nucker.kitpvpplus.abilities.Fireman;
import wtf.nucker.kitpvpplus.abilities.Sonic;
import wtf.nucker.kitpvpplus.abilities.TNTShooter;
import wtf.nucker.kitpvpplus.api.KitPvPPlusAPI;
import wtf.nucker.kitpvpplus.commands.*;
import wtf.nucker.kitpvpplus.commands.custom.CustomCMDManager;
import wtf.nucker.kitpvpplus.dataHandlers.Mongo;
import wtf.nucker.kitpvpplus.dataHandlers.SQL;
import wtf.nucker.kitpvpplus.exceptions.KitNotExistException;
import wtf.nucker.kitpvpplus.integrations.VaultEcoService;
import wtf.nucker.kitpvpplus.integrations.api.APIMain;
import wtf.nucker.kitpvpplus.integrations.placeholderapi.KitPvPPlaceholderExpansion;
import wtf.nucker.kitpvpplus.integrations.worldguard.WorldGuardManager;
import wtf.nucker.kitpvpplus.listeners.*;
import wtf.nucker.kitpvpplus.managers.*;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.utils.*;
import wtf.nucker.simplemenus.spigot.MenuManager;
import wtf.nucker.simplemenus.spigot.MenuSettings;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public final class KitPvPPlus extends JavaPlugin {
    /*
    ==Feature List==

    - Statistics (DONE)
    - Locations (DONE)
    - Customisable Kits (done)
    - Kit gui (paginated)  (done)
    - Abilities (done)
    - MySQL & MongoDB support for data (done)
    - Flat file support for data (done)
    - Economy system vault & flat (done)
    - Scoreboard (done)
    - PlaceholderAPI (Done)
    - World guard (Done)
    - Version support (done)
    - Hex colors (done)

    (Going to) TODO:
    - Kit editor guiu
    - Duels

    (Could) TODO:
    - Make data manager in oop (DONE)
    - Use simplix storage (Not doing)
    - Kit gui signs (DONE)
    - Soup (DONE)
    - On death exp (DONE)
    - Kill streaks & kill:death ratio (kdr) stats (done)
    - Current kit placeholder (Not doing)
    - Leaderboard rankings placeholder (DONE)
    - Placeholderapi placeholders in all messages (DONE)
    - Death messages and arrow hit messages (DONE)
    - Arrow and death sounds (DONE)
    - Custom death screen
    - CUstomise menus (Not doing)
    - Leaderboards (DONE)
    - Health above name (Done)
    - Player vaults (DONE)
    - Kill tag thingy
    - Kill commands (DONE)
    - Fix death teleports (DONE)
    - Playerdata in api (DONE)
    - World guard for 1.12 and lower
     */

    private static KitPvPPlus instance;

    public final static boolean DEBUG = false; /* TURN THIS OFF FOR PRODUCTION */

    private Config messages;
    private DataManager dataManager;
    private Economy econ;
    private AbilityManager abilityManager;
    private KitManager kitManager;
    private MenuManager menuManager;
    private ScoreboardManager sbManager;
    private SignManager signManager;
    private MetricsLite metrics;
    private WorldGuardManager worldGuardManager;
    private KitPvPPlusAPI api;
    private PlayerVaultManager pvManager;
    private LeaderBoardManager leaderBoardManager;
    private VersionManager verManager;
    private Debugger debugger;

    @Override
    public void onLoad() {
        KitPvPPlus.instance = this;
        this.debugger = new Debugger();

        if(getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                this.worldGuardManager = new WorldGuardManager(this);
                this.worldGuardManager.getWorldGuard().registerFlags();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        long time = System.currentTimeMillis();
        Logger.info(new String[]{
                "&f" + ChatUtils.CONSOLE_BAR,
                "&cKitPvP Plus",
                "&fDeveloped by Nucker",
                "&f" + getDescription().getDescription(),
                "&f" + ChatUtils.CONSOLE_BAR
        });


        Logger.info("Creating bstats instance");
        this.metrics = new MetricsLite(this);
        if(metrics.isEnabled()) {
            Logger.success("Sending bstats information");
        }

        this.menuManager = new MenuManager(this);
        Logger.debug(menuManager.getOpenMenus() == null);
        MenuSettings settings = new MenuSettings();
        settings.setDefaultFillerItem(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());

        Logger.debug("Loading configs.");
        getConfig().options().copyDefaults(true); // Used for saving comments
        saveDefaultConfig();
        System.out.println(this.getSubVersion());
        if(this.getSubVersion() >= 13) {
            if(getConfig().getString("filler-item").equals("STAINED_GLASS_PANE")) {
                getConfig().set("filler-item", "BLACK_STAINED_GLASS_PANE");
            }
            if(getConfig().getString("abilities.fireball.material").equals("FIREBALL")) {
                getConfig().set("abilities.fireball.material", "FIRE_CHARGE");
            }
            saveConfig();
        }
        this.messages = new Config("messages.yml");
        for (Language message : Language.values()) {
            if (Language.getMessage(message.getPath(), "null").equals("null")) {
                this.messages.getConfig().set(message.getPath(), message.getDef());
            }
        }
        this.messages.save();
        Locations.setup();
        this.dataManager = new DataManager(this);
        this.signManager = new SignManager();
        Logger.success("Configs have been loaded");

        this.pvManager = new PlayerVaultManager();

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            PlayerBank.setStorageType(StorageType.BankStorageType.FLAT);
        } else {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                PlayerBank.setStorageType(StorageType.BankStorageType.VAULT);
                this.econ = rsp.getProvider();
            } else {
                PlayerBank.setStorageType(StorageType.BankStorageType.FLAT); // Set storage type to flat so that eco handles itself
                this.getServer().getServicesManager().register(VaultEcoService.class, new VaultEcoService(this), this, ServicePriority.Normal);
            }
        }
        this.leaderBoardManager = new LeaderBoardManager();

        this.abilityManager = new AbilityManager();
        KitManager.setup();
        this.kitManager = new KitManager();
        this.sbManager = new ScoreboardManager();


        this.registerEvents();
        this.registerCommands(new CustomCMDManager(this));
        this.registerAbilities();
        Logger.success("Registered events and commands");
        CooldownManager.setup();
        if (this.dataManager.getDataYaml().getConfigurationSection("playerdata") != null) {
            for (String uuid : this.dataManager.getDataYaml().getConfigurationSection("playerdata").getKeys(false)) {
                ConfigurationSection section = this.dataManager.getDataYaml().getConfigurationSection("playerdata." + uuid);
                if (section.contains("kit-cooldown")) {
                    for (String key : section.getConfigurationSection("kit-cooldown").getKeys(false)) {
                        //noinspection CatchMayIgnoreException
                        try {
                            Kit kit = this.getKitManager().getKit(key);
                            long currentTime = Instant.now().getEpochSecond();
                            long kitTime = section.getLong("kit-cooldown." + key);
                            if (currentTime < kitTime) {
                                CooldownManager.addKitCooldown(getServer().getOfflinePlayer(UUID.fromString(uuid)), kit, Math.toIntExact(kitTime - currentTime));
                            }
                        } catch (KitNotExistException e) {} // Ouch
                    }
                }
                section.set("kit-cooldown", null);
                this.getDataManager().getDataConfig().save();
            }
    }

        Logger.info("Attempting to support PlaceHolderAPI");
        if(this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Logger.success("Linked to PlaceHolderAPI. Registering expansion");
            new KitPvPPlaceholderExpansion().register();
        }

        if(this.isWGEnabled()) {
            Logger.success("Integrated with world guard");
        }

        ClockUtils.runCodeLater(10, runnable -> {
            if(!this.getDataManager().isConnected()) {
                Logger.error("Failed to connect to "+this.getDataManager().getStorageType().name().toLowerCase());
                this.getServer().getPluginManager().disablePlugin(this);
            }
        });
        api = this.setupAPI();

        Logger.debug("Successfully loaded KitPvP Plus");

        Logger.info("Running version tasks");
        try {
            this.verManager = new VersionManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long timeTaken = time - System.currentTimeMillis();
        Logger.success("KitPvP Plus has loaded up in " + timeTaken + " ms");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger.info("KitPvP Plus is shutting down");
        CooldownManager.getKitCooldowns().forEach((uuid, id) -> {
            Kit kit;
            try {
                kit = this.getKitManager().getKit(id);
            } catch (KitNotExistException e) {
                e.printStackTrace();
                return;
            }
            if (kit.getCooldownRunnable() != null) {
                OfflinePlayer p = getServer().getOfflinePlayer(uuid);
                kit.getCooldownRunnable().getRunnable().cancel();
                if (kit.getCooldownRunnable().getAmount() > 10) {
                    this.dataManager.getDataYaml().set("playerdata." + p.getUniqueId() + ".kit-cooldown." + kit.getId(), Instant.now().getEpochSecond() + kit.getCooldownRunnable().getAmount());
                    this.dataManager.getDataConfig().save();
                }
            }
        });
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new DamageListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListeners(), this);
        getServer().getPluginManager().registerEvents(new SignListeners(), this);

        if(this.isWGEnabled()) {
            this.worldGuardManager.getWorldGuard().subscribeListeners();
        }
    }

    private void registerCommands(CustomCMDManager manager) {
        Map<MessageKeyProvider, String> messages = new HashMap<>();
        messages.put(MessageKeys.PERMISSION_DENIED, ChatColor.stripColor(Language.PERMISSION_MESSAGE.get()));
        messages.put(MessageKeys.PERMISSION_DENIED_PARAMETER, ChatColor.stripColor(Language.PERMISSION_MESSAGE.get()));
        messages.put(MessageKeys.NOT_ALLOWED_ON_CONSOLE, ChatColor.stripColor(Language.NOT_CONSOLE_COMMAND.get()));
        manager.getLocales().addMessages(manager.getLocales().getDefaultLocale(), messages);

        manager.getCommandCompletions().registerCompletion("ownedkits", c -> {
            List<String> kitNames = new ArrayList<>();
            for (Kit kit : this.getKitManager().getKits()) {
                if (this.getDataManager().getPlayerData(c.getPlayer()).ownsKit(kit)) {
                    kitNames.add(kit.getId());
                }
            }
            return ImmutableList.copyOf(kitNames);
        });

        manager.getCommandCompletions().registerCompletion("kits", c -> {
            List<String> kitNames = new ArrayList<>();
            for (Kit kit : this.getKitManager().getKits()) {
                kitNames.add(kit.getId());
            }
            return ImmutableList.copyOf(kitNames);
        });

        manager.getCommandCompletions().registerCompletion("notownedkits", c -> {
            List<String> kitNames = new ArrayList<>();
            for (Kit kit : this.getKitManager().getKits()) {
                if (!this.getDataManager().getPlayerData(c.getPlayer()).ownsKit(kit)) {
                    kitNames.add(kit.getId());
                }
            }
            return ImmutableList.copyOf(kitNames);
        });

        manager.getCommandCompletions().registerCompletion("abilities", c -> {
            List<String> abilities = new ArrayList<>();
            AbilityManager.getAbilities().forEach((s, ability) -> abilities.add(ability.getId()));

            return ImmutableList.copyOf(abilities);
        });

        manager.getCommandContexts().registerContext(Kit.class, c -> {
            Kit res;
            try {
                res = this.getKitManager().getKit(c.popFirstArg());
            } catch (KitNotExistException e) {
                throw new InvalidCommandArgument(Language.KIT_DOESNT_EXIST.get(c.getPlayer()), true);
            }
            return res;
        });
        manager.getCommandCompletions().setDefaultCompletion("kits", Kit.class);

        manager.getCommandContexts().registerContext(Ability.class, c -> {
            Ability res;
            res = AbilityManager.getAbility(c.popFirstArg());
            if(res == null) throw new InvalidCommandArgument("&cThis ability dosent exist");

            return res;
        });
        manager.getCommandCompletions().setDefaultCompletion("abilities", Ability.class);

        manager.registerCommand(new StatsCommand());
        manager.registerCommand(new SpawnCommand());
        manager.registerCommand(new ArenaCommand());
        manager.registerCommand(new KitPvPCommand());
        manager.registerCommand(new KitCommand());
        manager.registerCommand(new PlayerVaultCommand());


        if (PlayerBank.getStorageType().equals(StorageType.BankStorageType.FLAT)) { /* Only registers commands if there is no economy manager */
            manager.registerCommand(new EconomyAdminCommand());
            manager.registerCommand(new EconomyCommands());
        }
        if (KitPvPPlus.DEBUG) {
            manager.registerCommand(new DebugCommand());
        }
    }

    private void registerAbilities() {
        this.getAbilityManager().registerAbility(new TNTShooter());
        this.getAbilityManager().registerAbility(new Fireball());
        this.getAbilityManager().registerAbility(new Sonic());
        this.getAbilityManager().registerAbility(new Fireman());
    }

    private KitPvPPlusAPI setupAPI() {
        return new APIMain(this);
    }

    public YamlConfiguration getMessages() {
        return this.messages.getConfig();
    }

    public Config getMessagesConfig() {
        return messages;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Economy getEconomy() {
        return econ;
    }

    public static KitPvPPlus getInstance() {
        return instance;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public ScoreboardManager getSbManager() {
        return sbManager;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public MetricsLite getMetrics() {
        return metrics;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public KitPvPPlusAPI getApi() {
        return api;
    }

    public VersionManager getVerManager() {
        return verManager;
    }

    public LeaderBoardManager getLeaderBoardManager() {
        return leaderBoardManager;
    }

    public PlayerVaultManager getPvManager() {
        return pvManager;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    public boolean isWGEnabled() {
        return this.worldGuardManager != null;
    }

    public boolean isHexEnabled() {
        return this.getSubVersion() >= 16;
    }

    public void reloadConfigs() {
        this.getMessagesConfig().reload();
        if (this.getDataManager().getStorageType().equals(StorageType.FLAT)) {
            this.getDataManager().getDataConfig().reload();
        }
        this.reloadConfig();
        this.dataManager = new DataManager(this);
    }

    public void reloadDatabase() {
        switch (this.getDataManager().getStorageType()) {
            case FLAT:
                this.getDataManager().getDataConfig().reload();
                break;
            case MONGO:
                Mongo.getClient().getClient().close();
                this.dataManager = new DataManager(this);
                break;
            case SQL:
                try {
                    SQL.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.dataManager = new DataManager(this);
                break;
        }
    }

    public void reloadSB() {
        if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> this.getSbManager().updateBoard(player));
        }
        ScoreboardManager.ENABLED = KitPvPPlus.getInstance().getConfig().getBoolean("scoreboard.enabled");
    }

    public int getSubVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").replace("v", "").split(",")[3];
        return Integer.parseInt(version.replace("1_", "").replaceAll("_R\\d", ""));
    }

    /*
    TODO: Implement this so we can have comments in configs - https://docs.bentobox.world/en/latest/BentoBox/Config-API/
    @Override
    public void saveConfig() {
        this.saveDefaultConfig();
    }
     */
}
