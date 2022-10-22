package wtf.nucker.kitpvpplus.arena.command

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.standard.BooleanArgument
import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.bukkit.parsers.location.LocationArgument
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import wtf.nucker.kitpvpplus.arena.Arena
import wtf.nucker.kitpvpplus.arena.ArenaManager
import wtf.nucker.kitpvpplus.manager.CommandManager
import wtf.nucker.kitpvpplus.parser.ArenaParser
import wtf.nucker.kitpvpplus.util.BlockRegion
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component
import wtf.nucker.kitpvpplus.util.KotlinExtensions.lang
import wtf.nucker.kitpvpplus.util.KotlinExtensions.parsedComponent
import wtf.nucker.kitpvpplus.util.KotlinExtensions.placeholder
import wtf.nucker.kitpvpplus.util.KotlinExtensions.sendTo

class ArenaCommands(manager: CommandManager, arenaManager: ArenaManager) {

    init {
        manager.buildAndQueueRegister("arena", ArgumentDescription.of("Manage arenas")) {
            permission("kitpvpplus.arenas")
            handler {
                "<red>Invalid Command Syntax. Correct command syntax is: <gray>/arena <create|delete|set|about|list>".parsedComponent
                    .sendTo(it.sender, target = null)
            }

            // create <name> <point1> <point2>
            manager.command(copy {
                literal("create")
                permission("kitpvpplus.arenas.create")

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
                // set name|region|location
                literal("set")
                permission("kitpvpplus.arenas.edit")
                handler {
                    "<red>Invalid Command Syntax. Correct command syntax is: <gray>/arena set <name|region|restricted> <arena> <value...>".parsedComponent
                        .sendTo(it.sender, target = null)
                }

                manager.command(copy {
                    literal("name")
                    argument(ArenaParser.of("arena"))
                    argument(StringArgument.of("name", StringArgument.StringMode.QUOTED))
                    handler {
                        val arena: Arena = it["arena"]
                        val oldName = arena.name
                        arena.name = MiniMessage.miniMessage().deserialize(it["name"])
                        arenaManager.editArena(arena)
                        it.lang.arena.arenaNameUpdated
                            .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                            .placeholder("old_name", oldName)
                            .placeholder("new_name", arena.name)
                            .sendTo(it.sender, target = null)
                    }
                }.commandBuilder)

                manager.command(copy {
                    literal("region", aliases = arrayOf("location"))
                    argument(ArenaParser.of("arena"))
                    argument(LocationArgument.of("point1"))
                    argument(LocationArgument.of("point2"))
                    handler {
                        val arena: Arena = it["arena"]
                        arena.region = BlockRegion(it.get<Location>("point1").block, it.get<Location>("point2").block)
                        arenaManager.editArena(arena)

                        it.lang.arena.arenaRegionUpdated
                            .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                            .placeholder("name", arena.name)
                            .placeholder("region_point_1", arena.region.point1.component)
                            .placeholder("region_point_2", arena.region.point2.component)
                            .sendTo(it.sender, target = null)
                    }
                }.commandBuilder)

                manager.command(copy {
                    literal("restricted")
                    argument(ArenaParser.of("arena"))
                    argument(BooleanArgument.optional("restricted"))
                    handler {
                        val arena: Arena = it["arena"]
                        arena.restrictedAccess = it.getOrDefault("restricted", !arena.restrictedAccess)!!
                        arenaManager.editArena(arena)

                        (if(arena.restrictedAccess) it.lang.arena.arenaNowRestricted else it.lang.arena.arenaNoLongerRestricted)
                            .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                            .placeholder("restricted", arena.restrictedAccess)
                            .placeholder("name", arena.name)
                            .sendTo(it.sender, target = null)
                    }
                }.commandBuilder)
            }.commandBuilder)

            // about
            manager.command(copy {
                literal("about")
                argument(ArenaParser.of("arena", applyPermission = false))
                handler {
                    val arena: Arena = it["arena"]
                    text {
                        append(arena.name)
                        append(Component.newline())
                        append(Component.empty())
                        append(Component.newline())

                        append("<yellow>Id: <white>${arena.id}".parsedComponent)
                        append(Component.newline())


                        append("Region: " component NamedTextColor.YELLOW)
                        append(arena.region.point1.component.color(NamedTextColor.WHITE))
                        append(" to " component TextDecoration.ITALIC)
                        append(arena.region.point2.component.color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                        append(Component.newline())

                        append("<yellow>Restricted: <gray>${arena.restrictedAccess}".parsedComponent)
                    }
                        .sendTo(it.sender, target = null)
                }
            }.commandBuilder)

            // list
            manager.command(copy {
                literal("list")
                handler {
                    if(arenaManager.arenas.isEmpty()) {
                        it.lang.arena.noArenasCreated.sendTo(it.sender, target = null)
                        return@handler
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
                permission("kitpvpplus.arenas.delete")
                argument(ArenaParser.of("arena"))
                handler {
                    val arena: Arena = it["arena"]
                    arenaManager.deleteArena(arena)
                    it.lang.arena.arenaDeleted
                        .placeholder("id", arena.id, identifierAlias = arrayOf("arena"))
                        .sendTo(it.sender, target = null)
                }
            }.commandBuilder)
        }
    }
}