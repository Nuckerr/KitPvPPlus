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

class ArenaParser(arenaManager: ArenaManager, private val applyPermission: Boolean = true) : ArgumentParser<CommandSender, Arena> {

    init {
        Companion.arenaManager = arenaManager
    }

    override fun parse(commandContext: CommandContext<CommandSender>, inputQueue: Queue<String>): ArgumentParseResult<Arena> {
        val id = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(this::class.java, commandContext))
        val arena = arenaManager.getArena(id) ?: return ArgumentParseResult.failure(NullPointerException("No arena with the id $id exists"))
        if(!arena.hasPermission(commandContext.sender) && applyPermission) return ArgumentParseResult.failure(IllegalStateException("You don't have permission to $id"))
        inputQueue.remove()
        return ArgumentParseResult.success(arena)
    }

    override fun suggestions(commandContext: CommandContext<CommandSender>, input: String): MutableList<String> =
        arenaManager.arenas.filter { if(applyPermission) it.hasPermission(commandContext.sender) else true }.map { it.id }.toMutableList()
    companion object {
        private lateinit var arenaManager: ArenaManager

        private fun build(name: String, argumentDescription: ArgumentDescription, required: Boolean, applyPermission: Boolean): CommandArgument<CommandSender, *> {
            return CommandArgument(
                required,
                name,
                ArenaParser(arenaManager, applyPermission),
                "",
                TypeToken.get(Arena::class.java),
                null,
                argumentDescription,
            )
        }

        fun of(name: String, argumentDescription: ArgumentDescription = ArgumentDescription.empty(), applyPermission: Boolean = true): CommandArgument<CommandSender, *> {
            return build(name, argumentDescription, true, applyPermission)
        }

        fun optional(name: String, argumentDescription: ArgumentDescription = ArgumentDescription.empty(), applyPermission: Boolean = true): CommandArgument<CommandSender, *> {
            return build(name, argumentDescription, false, applyPermission)
        }
    }
}