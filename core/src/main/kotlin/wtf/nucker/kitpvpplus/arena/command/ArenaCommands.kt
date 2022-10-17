package wtf.nucker.kitpvpplus.arena.command

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.bukkit.parsers.location.LocationArgument
import cloud.commandframework.kotlin.extension.buildAndRegister
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
                    val point1: Location = it.get("id")
                    val point2: Location = it.get("id")
                    arenaManager.createArena(id, BlockRegion(point1.block, point2.block)).let { arena ->
                        it.lang.arena.arenaCreated
                            .placeholder("id", arena.id)
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
                    argument(StringArgument.of("name"))
                    handler {
                        try {
                            arenaManager.editArena(it["id"]) { arena ->
                                val oldName = arena.name
                                arena.name = MiniMessage.miniMessage().deserialize(it["name"])
                                it.lang.arena.arenaNameUpdated
                                    .placeholder("id", arena.id)
                                    .placeholder("old_name", oldName)
                                    .placeholder("new_name", arena.name)
                            }
                        }catch (ex: NullPointerException) {
                            it.lang.arena.arenaDoesntExist
                                .placeholder("id", it.get<String>("id"))
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
                                    .placeholder("id", arena.id)
                                    .placeholder("name", arena.name)
                                    .placeholder("region_point_1", arena.region.point1.component)
                                    .placeholder("region_point_2", arena.region.point2.component)
                            }
                        }catch (ex: NullPointerException) {
                            it.lang.arena.arenaDoesntExist
                                .placeholder("id", it.get<String>("id"))
                                .sendTo(it.sender, target = null)
                        }
                    }
                }.commandBuilder)
            }.commandBuilder)

            // delete <id>
            manager.command(copy {
                literal("delete")
                argument(StringArgument.of("id"))
                handler {
                    if(arenaManager.getArena(it["id"]) == null) {
                        it.lang.arena.arenaDoesntExist
                            .placeholder("id", it.get<String>("id"))
                            .sendTo(it.sender, target = null)
                    }
                    arenaManager.deleteArena(it["id"])
                    it.lang.arena.arenaDeleted
                        .placeholder("id", it.get<String>("id"))
                }
            }.commandBuilder)
        }
    }
}