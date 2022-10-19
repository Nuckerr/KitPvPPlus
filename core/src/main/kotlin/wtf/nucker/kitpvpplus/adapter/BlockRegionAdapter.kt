package wtf.nucker.kitpvpplus.adapter

import org.bukkit.Location
import org.spongepowered.configurate.ConfigurationNode
import wtf.nucker.kitpvpplus.util.BlockRegion
import java.lang.reflect.Type

class BlockRegionAdapter: Adapter<BlockRegion> {

    override val type: Class<BlockRegion> = BlockRegion::class.java

    override fun deserialize(type: Type, node: ConfigurationNode): BlockRegion? {
        if(node.isNull) return null

        println(node.path().array().contentToString())
        val point1 = node.node("point1").get(Location::class.java)!!
        val point2 = node.node("point2").get(Location::class.java)!!
        return BlockRegion(point1.block, point2.block)
    }

    override fun serialize(type: Type, obj: BlockRegion?, node: ConfigurationNode) {
        if(obj == null) {
            node.raw(null)
            return
        }

        node.node("point1").set(obj.point1.location)
        node.node("point2").set(obj.point2.location)
    }

}