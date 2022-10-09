package wtf.nucker.kitpvpplus.economy

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.standard.DoubleArgument
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument
import cloud.commandframework.kotlin.extension.buildAndRegister
import org.bukkit.OfflinePlayer
import wtf.nucker.kitpvpplus.KitPvPPlus
import wtf.nucker.kitpvpplus.util.KotlinExtensions.editPlayerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.placeholder
import wtf.nucker.kitpvpplus.util.KotlinExtensions.playerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.sendTo

class EconomyModule(plugin: KitPvPPlus) {

    init {
        plugin.commandManager.buildAndRegister("eco", ArgumentDescription.of("Manage economy"), arrayOf("economy")) {
            permission("kitpvpplus.economy")

            plugin.commandManager.command(copy {
                literal("set", ArgumentDescription.of("Set the balance of a player"))
                permission("kitpvpplus.economy.modify")
                argument(OfflinePlayerArgument.of("target"))
                argument(DoubleArgument.of("amount"))
                handler {
                    val target: OfflinePlayer = it.get("target")
                    target.editPlayerData { data ->
                        data.money = it["amount"]
                    }
                    plugin.langConfig.economy.balanceSetTo
                        .placeholder("amount", target.playerData.money)
                        .sendTo(it.sender, target = target)
                }

            }.commandBuilder)

            plugin.commandManager.command(copy {
                literal("add", ArgumentDescription.of("Add to the balance of a player"))
                permission("kitpvpplus.economy.modify")
                argument(OfflinePlayerArgument.of("target"))
                argument(DoubleArgument.of("amount"))
                handler {
                    val target: OfflinePlayer = it.get("target")
                    target.editPlayerData { data ->
                        data.money += it.get<Double>("amount")
                    }
                    plugin.langConfig.economy.balanceAddedTo
                        .placeholder("amount", target.playerData.money)
                        .sendTo(it.sender, target = target)
                }
            }.commandBuilder)

            plugin.commandManager.command(copy {
                literal("clear", ArgumentDescription.of("Clear the balance of a player"))
                permission("kitpvpplus.economy.modify")
                argument(OfflinePlayerArgument.of("target"))
                handler {
                    val target: OfflinePlayer = it.get("target")
                    target.editPlayerData { data ->
                        data.money = 0.0
                    }
                    plugin.langConfig.economy.balanceCleared
                        .sendTo(it.sender, target = target)
                }
            }.commandBuilder)

            plugin.commandManager.command(copy {
                literal("query", ArgumentDescription.of("Clear the balance of a player"), "view")
                permission("kitpvpplus.economy.view")
                argument(OfflinePlayerArgument.of("target"))
                handler {
                    val target: OfflinePlayer = it.get("target")

                    plugin.langConfig.economy.balanceQueried
                        .placeholder("amount", target.playerData.money)
                        .sendTo(it.sender, target = target)
                }
            }.commandBuilder)
        }
    }
}