package wtf.nucker.kitpvpplus.adapter

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.Type

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 23/09/2022
 */
class ComponentAdapter: Adapter<Component> {

    override val type = Component::class.java

    companion object {
        val INSTANCE = ComponentAdapter()
        private val miniMessage = MiniMessage.miniMessage()
    }

    override fun deserialize(type: Type, source: ConfigurationNode): Component? {
        return miniMessage.deserialize(source.string ?: return null)
    }

    override fun serialize(type: Type, component: Component?, target: ConfigurationNode) {
        if(component == null) {
            target.raw(null)
            return
        }

        target.set(miniMessage.serialize(component))
    }
}