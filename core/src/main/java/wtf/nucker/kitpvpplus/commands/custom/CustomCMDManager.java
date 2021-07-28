package wtf.nucker.kitpvpplus.commands.custom;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.Plugin;
import wtf.nucker.kitpvpplus.utils.CommandMapUtils;
import wtf.nucker.kitpvpplus.utils.Logger;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public class CustomCMDManager extends BukkitCommandManager {


    public CustomCMDManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void registerCommand(BaseCommand command) {
        CommandMapUtils.unregisterCommandFromCommandMap(command.getExecCommandLabel(), true);
        super.registerCommand(command);
        Logger.debug("Registered /" + command.getExecCommandLabel() +" command");
    }
}
