package wtf.nucker.kitpvpplus.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
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

    var Player.playerData
        get() = plugin.database.getPlayerData(this)
        set(data) = plugin.database.updatePlayerData(this, data)

    fun Player.editPlayerData(editor: Consumer<PlayerData>) {
        val data = playerData
        editor.accept(data)
        playerData = data
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
}