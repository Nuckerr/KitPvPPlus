package wtf.nucker.kitpvpplus.manager

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.kotlin.MutableCommandBuilder
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import io.leangen.geantyref.TypeToken
import org.bukkit.command.CommandSender
import wtf.nucker.kitpvpplus.KitPvPPlus
import wtf.nucker.kitpvpplus.arena.Arena
import wtf.nucker.kitpvpplus.parser.ArenaParser
import java.util.function.Function

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
class CommandManager(private val plugin: KitPvPPlus): PaperCommandManager<CommandSender>(
    plugin.bukkit,
    CommandExecutionCoordinator.simpleCoordinator(),
    Function.identity(),
    Function.identity()) {

    private val commandsToRegister: MutableList<QueuedCommand> = mutableListOf()

    init {
        registerAsynchronousCompletions()
        registerBrigadier()
        registerCommandPreProcessor {
            it.commandContext.set("lang", plugin.langConfig)
        }

    }

    fun registerParsers() {
        registerParser(TypeToken.get(Arena::class.java), ArenaParser(plugin.arenaManager, true))
    }

    private fun <T> registerParser(token: TypeToken<T>, instance: ArgumentParser<CommandSender, T>) {
        parserRegistry().registerParserSupplier(token) { instance }
    }

    fun buildAndQueueRegister(
        name: String,
        description: ArgumentDescription = ArgumentDescription.empty(),
        aliases: Array<String> = emptyArray(),
        lambda: MutableCommandBuilder<CommandSender>.() -> Unit
    ) {
        commandsToRegister.add(QueuedCommand(name, description, aliases, lambda))
    }

    fun registerCommands() {
        commandsToRegister.forEach {
            buildAndRegister(it.name, it.description, it.aliases, it.lambda)
        }
    }

    private data class QueuedCommand(
        val name: String,
        val description: ArgumentDescription,
        val aliases: Array<String>,
        val lambda: MutableCommandBuilder<CommandSender>.() -> Unit
    )
}