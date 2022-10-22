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

class ArenaParser<C : Any>(private val arenaManager: ArenaManager) : ArgumentParser<C, Arena> {

    init {
        instance = this
    }

    override fun parse(commandContext: CommandContext<C>, inputQueue: Queue<String>): ArgumentParseResult<Arena> {
        val id = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        val arena = arenaManager.getArena(id) ?: return ArgumentParseResult.failure(NullPointerException("No arena with the id $id exists"))
        inputQueue.remove()
        return ArgumentParseResult.success(arena)
    }

    override fun suggestions(commandContext: CommandContext<C>, input: String): MutableList<String> = arenaManager.arenas.map { it.id }.toMutableList()

    @Suppress("UNCHECKED_CAST")
    companion object {
        private lateinit var instance: ArenaParser<*>

        private fun build(name: String, argumentDescription: ArgumentDescription, required: Boolean): CommandArgument<CommandSender, *> {
            return CommandArgument(
                required,
                name,
                instance as ArenaParser<CommandSender>,
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