package wtf.nucker.kitpvpplus.adapter

import org.bukkit.Bukkit
import org.bukkit.Location
import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.Type

class LocationAdapter: Adapter<Location> {

    override val type: Class<Location> = Location::class.java

    override fun deserialize(type: Type, node: ConfigurationNode): Location? {
        if(node.isNull) return null

        check(!node.node("x").isNull)
        check(!node.node("y").isNull)
        check(!node.node("z").isNull)

        check(!node.node("yaw").isNull)
        check(!node.node("pitch").isNull)

        val x = node.node("x").double
        val y = node.node("y").double
        val z = node.node("z").double

        val yaw = node.node("yaw").float
        val pitch = node.node("pitch").float

        val world = node.node("world").string!!

        return Location(Bukkit.getWorld(world), x, y, z, yaw, pitch)
     }

    override fun serialize(type: Type, obj: Location?, node: ConfigurationNode) {
        if(obj == null) {
            node.raw(null)
            return
        }

        node.node("x").set(obj.x)
        node.node("y").set(obj.y)
        node.node("z").set(obj.z)

        node.node("yaw").set(obj.yaw)
        node.node("pitch").set(obj.pitch)

        node.node("world").set(obj.world.name)
    }


}