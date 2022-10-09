package wtf.nucker.kitpvpplus.util

import cloud.commandframework.context.CommandContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wtf.nucker.kitpvpplus.KitPvPPlus
import wtf.nucker.kitpvpplus.`object`.PlayerData
import java.util.function.Consumer

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
object KotlinExtensions {

    lateinit var plugin: KitPvPPlus

    var OfflinePlayer.playerData
        get() = plugin.database.getPlayerData(this)
        set(data) = plugin.database.updatePlayerData(this, data)

    fun OfflinePlayer.editPlayerData(editor: Consumer<PlayerData>) {
        val data = playerData
        editor.accept(data)
        playerData = data
    }

    fun Player.playBukkitSound(sound: Sound, location: Location = getLocation(), pitch: Float = 1f, volume: Float = 1f) {
        playSound(location, sound, volume, pitch)
    }

    val Any.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    val String.component
        get() = Component.text(this)

    infix fun String.component(color: TextColor): Component {
        return Component.text(this, color)
    }

    infix fun String.component(decoration: TextDecoration): Component {
        return Component.text(this, Style.style(decoration))
    }

    infix fun String.component(style: Style): Component {
        return Component.text(this, style)
    }


    fun Component.placeholder(identifier: String, value: Component): Component {
        return this.replaceText {
            it.matchLiteral("%$identifier%")
            it.replacement(value)
        }
    }

    fun Component.placeholder(identifier: String, value: String): Component = placeholder(identifier, Component.text(value))

    fun Component.placeholder(identifier: String, value: Any): Component = placeholder(identifier, value.toString())


    fun List<Component>.placeholder(identifier: String, value: Component): List<Component> {
        val newList: MutableList<Component> = mutableListOf()
        forEach {
            newList.add(it.placeholder(identifier, value))
        }
        return newList.toList() // Turn immutable
    }

    fun List<Component>.placeholder(identifier: String, value: String): List<Component> = placeholder(identifier, Component.text(value))

    fun List<Component>.placeholder(identifier: String, value: Any): List<Component> = placeholder(identifier, value.toString())

    fun List<String>.adaptStringsToComponents(): List<Component> {
        val list: MutableList<Component> = mutableListOf()
        val mm = MiniMessage.miniMessage()
        forEach {
            list.add(mm.deserialize(it))
        }
        return list.toList() // Make immutable
    }

    fun Component.sendTo(sender: CommandSender, target: OfflinePlayer = sender as Player) {
        sender.sendMessage(this
            .placeholder("player", target.name ?: target.uniqueId.toString())
            .placeholder("sender", sender.name)
        )
    }

    fun List<Component>.sendTo(sender: CommandSender, target: OfflinePlayer = sender as Player) {
        forEach {
            it.sendTo(sender, target = target)
        }
    }

    fun Title.placeholder(identifier: String, value: Component): Title {
        return Title.title(
            title().placeholder(identifier, value),
            subtitle().placeholder(identifier, value)
        )
    }

    fun Title.placeholder(identifier: String, value: String): Title = placeholder(identifier, Component.text(value))

    fun Title.placeholder(identifier: String, value: Any): Title = placeholder(identifier, value.toString())

    fun CommandContext<CommandSender>.argumentOrSender(targetArgumentName: String = "target"): Boolean {
        if(sender !is Player && getOptional<Player>(targetArgumentName).isEmpty) {
            sender.sendMessage(plugin.langConfig.mustBeInGame)
            return false
        }
        return true
    }
}