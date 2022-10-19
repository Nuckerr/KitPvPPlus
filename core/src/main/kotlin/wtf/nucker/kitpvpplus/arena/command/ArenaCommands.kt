package wtf.nucker.kitpvpplus.arena.command

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.bukkit.parsers.location.LocationArgument
import cloud.commandframework.kotlin.extension.buildAndRegister
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import wtf.nucker.kitpvpplus.arena.ArenaManager
import wtf.nucker.kitpvpplus.manager.CommandManager
import wtf.nucker.kitpvpplus.util.BlockRegion
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component
import wtf.nucker.kitpvpplus.util.KotlinExtensions.lang
import wtf.nucker.kitpvpplus.util.KotlinExtensions.placeholder
import wtf.nucker.kitpvpplus.util.KotlinExtensions.sendTo

class ArenaCommands(manager: CommandManager, arenaManager: ArenaManager) {

    init {
        manager.buildAndRegister("arena", ArgumentDescription.of("Manage arenas")) {
            permission("kitpvpplus.arenas")

            // create <name> <point1> <point2>
            manager.command(copy {
                literal("create")

                argument(StringArgument.of("id"))
                argument(LocationArgument.of("point1"))
                argument(LocationArgument.of("point2"))
                handler {
                    val id: String = it.get("id")
                    val point1: Location = it.get("point1")
                    val point2: Location = it.get("point2")
                    arenaManager.createArena(id, BlockRegion(point1.block, point2.block)).let { arena ->
                        it.lang.arena.arenaCreated
                            .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                            .placeholder("name", arena.name)
                            .placeholder("region_point_1", arena.region.point1.component)
                            .placeholder("region_point_2", arena.region.point2.component)
                            .sendTo(it.sender, target = null)
                    }
                }
            }.commandBuilder)

            manager.command(copy {
                // set  name|region|location
                literal("set")

                manager.command(copy {
                    literal("name")
                    argument(StringArgument.of("id"))
                    argument(StringArgument.of("name", StringArgument.StringMode.QUOTED))
                    handler {
                        try {
                            arenaManager.editArena(it["id"]) { arena ->
                                val oldName = arena.name
                                arena.name = MiniMessage.miniMessage().deserialize(it["name"])
                                it.lang.arena.arenaNameUpdated
                                    .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                                    .placeholder("old_name", oldName)
                                    .placeholder("new_name", arena.name)
                                    .sendTo(it.sender, target = null)
                            }
                        }catch (ex: NullPointerException) {
                            it.lang.arena.arenaDoesntExist
                                .placeholder("id", it.get<String>("id"), identifierAlias = arrayOf("arena"))
                                .sendTo(it.sender, target = null)
                        }
                    }
                }.commandBuilder)

                manager.command(copy {
                    literal("region", aliases = arrayOf("location"))
                    argument(StringArgument.of("id"))
                    argument(LocationArgument.of("point1"))
                    argument(LocationArgument.of("point2"))
                    handler {
                        try {
                            arenaManager.editArena(it["id"]) { arena ->
                                arena.region = BlockRegion(it.get<Location>("point1").block, it.get<Location>("point2").block)
                                it.lang.arena.arenaNameUpdated
                                    .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                                    .placeholder("name", arena.name)
                                    .placeholder("region_point_1", arena.region.point1.component)
                                    .placeholder("region_point_2", arena.region.point2.component)
                                    .sendTo(it.sender, target = null)
                            }
                        }catch (ex: NullPointerException) {
                            it.lang.arena.arenaDoesntExist
                                .placeholder("id", it.get<String>("id"), identifierAlias = arrayOf("arena"))
                                .sendTo(it.sender, target = null)
                        }
                    }
                }.commandBuilder)
            }.commandBuilder)

            // list
            manager.command(copy {
                literal("list")
                handler {
                    if(arenaManager.arenas.isEmpty()) {
                        it.lang.arena.noArenasCreated.sendTo(it.sender, target = null)
                        //return@handler
                    }
                    text {
                        append("Arenas:" component NamedTextColor.YELLOW)
                        arenaManager.arenas.forEach { arena ->
                            append(Component.newline())
                            append(" - " component NamedTextColor.WHITE)
                            append(arena.name)
                        }
                    }.sendTo(it.sender, target = null)
                }
            }.commandBuilder)

            // delete <id>
            manager.command(copy {
                literal("delete")
                argument(StringArgument.of("id"))
                handler {
                    if(arenaManager.getArena(it["id"]) == null) {
                        it.lang.arena.arenaDoesntExist
                            .placeholder("id", it.get<String>("id"), identifierAlias = arrayOf("arena"))
                            .sendTo(it.sender, target = null)
                    }
                    arenaManager.deleteArena(it["id"])
                    it.lang.arena.arenaDeleted
                        .placeholder("id", it.get<String>("id"), identifierAlias = arrayOf("arena"))
                        .sendTo(it.sender, target = null)
                }
            }.commandBuilder)
        }
    }
}