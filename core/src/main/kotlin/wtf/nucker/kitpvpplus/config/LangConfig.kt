package wtf.nucker.kitpvpplus.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component

@ConfigSerializable
class LangConfig {

    val permissionMessage: Component = "You don't have permission" component NamedTextColor.RED
}