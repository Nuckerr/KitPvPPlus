package wtf.nucker.kitpvpplus.parser

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import io.leangen.geantyref.TypeToken
import org.bukkit.command.CommandSender
import wtf.nucker.kitpvpplus.arena.Arena
import wtf.nucker.kitpvpplus.arena.ArenaManager
import java.util.*

class ArenaParser(private val arenaManager: ArenaManager) : ArgumentParser<CommandSender, Arena> {

    init {
        instance = this
    }

    override fun parse(commandContext: CommandContext<CommandSender>, inputQueue: Queue<String>): ArgumentParseResult<Arena> {
        val id = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        val arena = arenaManager.getArena(id) ?: return ArgumentParseResult.failure(NullPointerException("No arena with the id $id exists"))
        if(!arena.hasPermission(commandContext.sender)) return ArgumentParseResult.failure(IllegalStateException("You don't have permission to $id"))
        inputQueue.remove()
        return ArgumentParseResult.success(arena)
    }

    override fun suggestions(commandContext: CommandContext<CommandSender>, input: String): MutableList<String> =
        arenaManager.arenas.filter { it.hasPermission(commandContext.sender) }.map { it.id }.toMutableList()

    companion object {
        private lateinit var instance: ArenaParser

        private fun build(name: String, argumentDescription: ArgumentDescription, required: Boolean): CommandArgument<CommandSender, *> {
            return CommandArgument(
                required,
                name,
                instance,
                "",
                TypeToken.get(Arena::class.java),
                null,
                argumentDescription
            )
        }

        fun of(name: String, argumentDescription: ArgumentDescription = ArgumentDescription.empty()): CommandArgument<CommandSender, *> {
            return build(name, argumentDescription, true)
        }

        fun optional(name: String, argumentDescription: ArgumentDescription = ArgumentDescription.empty()): CommandArgument<CommandSender, *> {
            return build(name, argumentDescription, false)
        }
    }
}