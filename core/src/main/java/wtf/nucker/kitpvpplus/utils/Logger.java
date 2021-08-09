package wtf.nucker.kitpvpplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import wtf.nucker.kitpvpplus.KitPvPPlus;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class Logger {

    public static void info(String message) {
        Logger.log(Logger.buildMessage("INFO", message, ChatColor.BLUE));
    }

    public static void info(String[] lines) {
        for (String line : lines) {
            Logger.info(line);
        }
    }

    public static void success(String message) {
        Logger.log(Logger.buildMessage("SUCCESS", message, ChatColor.GREEN));
    }

    public static void warn(String message) {
        Logger.log(Logger.buildMessage("WARNING", message, ChatColor.GOLD));
    }

    public static void warn(String[] message) {
        for (String line : message) {
            Logger.warn(line);
        }
    }

    public static void error(String message) {
        Logger.log(Logger.buildMessage("ERROR", message, ChatColor.RED));
    }

    public static void debug(String message) {
        if (!KitPvPPlus.DEBUG) return;
        if(message == null) message = "null";
        Logger.log(Logger.buildMessage("DEBUG", message, ChatColor.DARK_AQUA));
    }

    public static void debug(Object message) {
        if (!KitPvPPlus.DEBUG) return;
        if (message == null) message = "null";
        Logger.log(Logger.buildMessage("DEBUG", message.toString(), ChatColor.AQUA));
    }

    private static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.translate(message));
    }


    private static String buildMessage(String type, String message, ChatColor color) {
        return color + "[KitPvPPlus] [" + type + "] " + message;
    }
}
