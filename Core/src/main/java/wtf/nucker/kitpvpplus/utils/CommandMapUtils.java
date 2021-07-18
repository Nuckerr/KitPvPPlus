package wtf.nucker.kitpvpplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class CommandMapUtils {
    public static CommandMap getCommandMap() {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Error while obtaing commandmap.");
        }
        return null;
    }


    public static void unregisterCommandFromCommandMap(String label, boolean removeAliases) {
        try {
            PluginCommand command = Bukkit.getPluginCommand(label);
            if (command != null) {
                Field commandField = Command.class.getDeclaredField("commandMap");
                commandField.setAccessible(true);

                if (command.isRegistered())
                    command.unregister((CommandMap) commandField.get(command));
            }


            final Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);

            final Map<String, Command> cmdMap = (Map<String, Command>) f.get(getCommandMap());

            cmdMap.remove(label);

            if (command != null && removeAliases)
                for (final String alias : command.getAliases())
                    cmdMap.remove(alias);

        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Error while unregister command", exception);
        }
    }
}
