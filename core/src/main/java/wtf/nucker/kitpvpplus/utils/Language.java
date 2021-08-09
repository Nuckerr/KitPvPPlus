package wtf.nucker.kitpvpplus.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.PlayerData;
import wtf.nucker.kitpvpplus.managers.PlayerBank;

import java.util.Collections;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public enum Language {

    EXP_MESSAGE("general.exp-given", "&eYou have received %amount% exp"),
    STATS("general.stats-message", "null"),
    SPAWN("general.spawn-message", "&eYou have been sent to spawn!"),
    SET_SPAWN("admin.spawn-set", "&aSpawn has been set"),
    ARENA("general.arena-message", "&aYou are now in the arena"),
    SET_ARENA("admin.arena-set", "&aArena has been set"),
    BALANCE_MESSAGE("economy.balance-message", "&eYour balance is %balance%"),
    OTHER_BAL_MESSAGE("economy.other-balance-message", "&e%player%'s balance is %balance%"),
    BALANCE_SET("economy.balance-set", "&aYou have set %target%'s balance to %balance%$"),
    RESET_BALANCE("economy.balance-reset", "&cYou have reset %target%'s balance"),
    BALANCE_GIVEN("economy.balance-given", "&aYou have given %givenAmount%$ to %target%"),
    ECO_HELP("help-commands.eco-help", "null"),
    PAYMENT_SENT("economy.pay-message", "&aYou successfully paid %target% %amount%$"),
    INSUFFICIENT_BAL("economy.insufficient-balance", "&cYou dont have enough money for this"),
    PAID_MESSAGE("economy.paid-message", "&aYou where paid %amount%$ by %payer%"),
    MONEY_SENT_TO_SELF("economy.sent-to-self", "&cYou cant send money to yourself!"),
    PERMISSION_MESSAGE("general.permission-message", "&cYou dont have permission to run this command"),
    ON_COOLDOWN("abilities.cooldown-message", "&cYour still on cooldown"),
    NO_COOLDOWN_NOW("abilities.no-longer-on-cooldown", "&a%name% is no longer on cooldown"),
    SONIC_ACTIVATION("abilities.sonic-activation-message", "&9Speed activated for 1 minute"),
    FIREMAN_ACTIVATION("abilities.fireman-activation-message", "&6Put you out!"),
    KIT_LOADED("kits.kit-loaded", "&aLoaded kit %kitname%"),
    KIT_NOT_OWNED("kits.kit-not-owned", "&cYou dont own this kit"),
    KIT_ALREADY_OWNED("kits.kit-already-owned", "&cYou already own this kit"),
    KIT_GIVEN("kits.kit-given", "&aYou where given %kitname% by %player%"),
    KIT_SENT("kits.kit-sent", "&aYou sent %target% %kitname%"),
    KIT_CREATED("kits.kit-created", "&aCreated %kitname%"),
    KIT_DELETED("kits.kit-deleted", "&cDeleted kit %kitname%"),
    KIT_EDIT_DISPLAYNAME("kits.kit-edit-displayname", "&aSet kit %kitname%'s displayname to %newname%"),
    KIT_EDIT_ICON("kits.kit-edit-icon", "&aSet kit %kitname%'s icon to %itemname%"),
    KIT_EDIT_CONTENTS("kits.kit-edit-contents", "&aSet kit %kitname%'s contents to your inventory"),
    KIT_EDIT_PERMISSION("kits.kit-edit-permission", "&aSet %kitname%'s permission to %permission%"),
    KIT_EDIT_PRICE("kits.kit-edit-price", "&aSet %kitname%'s price to %price%"),
    KIT_EDIT_COOLDOWN("kits.kit-edit-cooldown", "&aSet %kitname%'s cooldown to %cooldown%"),
    KIT_ON_COOLDOWN("kits.kit-on-cooldown", "&cThis kit is on cooldown"),
    KIT_DOESNT_EXIST("kits.kit-dosent-exist", "&cThat kit dosent exist"),
    KIT_ALREADY_EXISTS("kits.kit-already-exists", "&cThat kit already exists you numpty"),
    KIT_HELP_COMMAND("help-commands.kit-command", "null"),
    KIT_ADMIN_HELP("help-commands.kit-admin", "null"),
    KIT_PURCHASE_CANCELED("kits.kit-purchase-canceled", "&cPurchase canceled"),
    OPENING_PV("general.opening-playervault", "&aOpening player vault %page%"),
    KIT_PURCHASED("kits.kit-purchase", "&aYou purchased kit"),
    KIT_MENU("kits.kit-menu-opening", "&aOpening kit"),
    NO_LAST_PAGE("general.no-last-page", "&cNo previous page"),
    KILLED_MESSAGE("events.killed", "&aYou killed %victim%"),
    DEATH_MESSAGE("events.death", "&cYou where killed by %killer%"),
    DEATH_BROADCAST("events.broadcast-death", "&a&l%victim% was killed by %killer%"),
    ARROW_HIT("events.arrow-hit", "&aYou hit %victim%"),
    NO_NEXT_PAGE("general.no-next-page", "&cNo next page"),
    CORE_HELP("help-commands.core-command", "null"),
    NOT_CONSOLE_COMMAND("general.console-command", "&cConsole cannot run this command"),
    PLUGIN_RELOADED("admin.plugin-reloaded", "&aPlugin has been reloaded"),
    SIGN_SET("sign-contents.sign-set", "&aSign has been set to %sign_type%"),
    SIGN_DELETED("sign-contents.sign-deleted", "&cSign deleted"),
    SPAWN_SIGN("sign-contents.spawn-sign", "Click to go to spawn"),
    KIT_GUI_SIGN("sign-contents.kit-gui-sign", "Click to open the kit GUI"),
    ARENA_SIGN("sign-contents.arena-sign", "CLick to go to the arena!");

    private final String path;
    private final String def;
    private final KitPvPPlus core = KitPvPPlus.getInstance();

    Language(String path, String def) {
        this.path = path;
        this.def = def;
    }

    public String get() {
        return Language.format(ChatUtils.translate(core.getMessages().getString("general.prefix", "") + core.getMessages().getString(this.path, this.def)));
    }

    public String get(Player player) {
        String message = ChatUtils.translate(Language.format(this.get(), player));
        if (KitPvPPlus.getInstance().getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), message);
        }
        return message;
    }

    public List<String> getAsStringList() {
        if (core.getMessages().getStringList(this.path).isEmpty())
            return ChatUtils.translate(Collections.singletonList(this.def));
        return ChatUtils.translate(core.getMessages().getStringList(this.path));
    }

    public static String getMessage(String path, String def) {
        return KitPvPPlus.getInstance().getMessages().getString(path, def);
    }

    public static String format(String message, Player player) {
       return message
                .replace("%player%", player.getName())
                .replace("%bar%", ChatUtils.CHAT_BAR)
                .replace("%blank%", ChatUtils.BLANK_MESSAGE)
                .replace("%left_arrow%", ChatUtils.Symbols.ARROW_LEFT.getSymbol())
                .replace("%right_arrow%", ChatUtils.Symbols.ARROW_RIGHT.getSymbol())
                .replace("%cross%", ChatUtils.Symbols.CROSS.getSymbol())
                .replace("%health%", ChatUtils.Symbols.HEALTH.getSymbol())
                .replace("%warning%", ChatUtils.Symbols.WARNING.getSymbol());

    }

    public static String format(String message) {
        return message
                .replace("%bar%", ChatUtils.CHAT_BAR)
                .replace("%blank%", ChatUtils.BLANK_MESSAGE)
                .replace("%left_arrow%", ChatUtils.Symbols.ARROW_LEFT.getSymbol())
                .replace("%right_arrow%", ChatUtils.Symbols.ARROW_RIGHT.getSymbol())
                .replace("%cross%", ChatUtils.Symbols.CROSS.getSymbol())
                .replace("%heart%", ChatUtils.Symbols.HEALTH.getSymbol())
                .replace("%warning%", ChatUtils.Symbols.WARNING.getSymbol());
    }

    public String getDef() {
        return def;
    }

    public String getPath() {
        return path;
    }

    public static String formatPlaceholders(Player p, String params) {
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(p.getPlayer());

        switch (params) {
            // Statistics
            case "kpvp_deaths":
                return String.valueOf(data.getDeaths());
            case "kpvp_kills":
                return String.valueOf(data.getKills());
            case "kpvp_exp":
                return String.valueOf(data.getExp());
            case "kpvp_level":
                return String.valueOf(data.getLevel());
            case "kpvp_bal":
                return String.valueOf(new PlayerBank(p).getBal());
            case "kpvp_killstreak":
                return String.valueOf(data.getKillStreak());
            case "kpvp_kdr":
                return String.valueOf(data.getKDR());
            default:
                return null;
        }
    }
}
