package wtf.nucker.kitpvpplus.manager

import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import wtf.nucker.kitpvpplus.config.LangConfig
import java.util.function.Function

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
class CommandManager(plugin: Plugin, langConfig: LangConfig): PaperCommandManager<CommandSender>(
    plugin,
    CommandExecutionCoordinator.simpleCoordinator(),
    Function.identity(),
    Function.identity()) {

    init {
        registerAsynchronousCompletions()
        registerBrigadier()
        registerCommandPreProcessor {
            it.commandContext.set("lang", langConfig)
        }
    }
}