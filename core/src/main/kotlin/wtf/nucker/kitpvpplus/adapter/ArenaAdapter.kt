package wtf.nucker.kitpvpplus.adapter

import net.kyori.adventure.text.Component
import org.spongepowered.configurate.ConfigurationNode
import wtf.nucker.kitpvpplus.arena.Arena
import wtf.nucker.kitpvpplus.util.BlockRegion
import java.lang.reflect.Type

class ArenaAdapter: Adapter<Arena> {

    override val type: Class<Arena> = Arena::class.java

    override fun deserialize(type: Type, node: ConfigurationNode): Arena? {
        if(node.isNull) return null
        val id = node.node("id").string!!
        val name = node.node("name").get(Component::class.java)!!
        val blockRegion = node.node("region").get(BlockRegion::class.java)!!
        return object : Arena {
            override val id: String = id
            override var name: Component = name
            override var region: BlockRegion = blockRegion

        }
    }

    override fun serialize(type: Type, obj: Arena?, node: ConfigurationNode) {
        if(obj == null) {
            node.raw(null)
            return
        }

        node.node("id").set(obj.id)
        node.node("name").set(obj.name)
        node.node("region").set(obj.region)
    }

}