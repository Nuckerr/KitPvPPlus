package wtf.nucker.kitpvpplus.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
object KotlinExtensions {


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