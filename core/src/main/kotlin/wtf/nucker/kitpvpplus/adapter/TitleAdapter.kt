package wtf.nucker.kitpvpplus.adapter

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.Type

class TitleAdapter: Adapter<Title> {
    override val type: Class<Title> = Title::class.java

    override fun deserialize(type: Type, node: ConfigurationNode): Title? {
        if(node.isNull) return null
        val title = node.node("title").get(Component::class.java)!!
        val subtitle = node.node("subtitle").get(Component::class.java)!!
        return Title.title(title, subtitle)
    }

    override fun serialize(type: Type, title: Title?, node: ConfigurationNode) {
        if(title == null) {
            node.raw(null)
            return
        }
        node.node("title").set(title.title())
        node.node("subtitle").set(title.subtitle())
    }

    companion object {
        val INSTANCE = TitleAdapter()
    }
}