package wtf.nucker.kitpvpplus.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.concurrent.ThreadLocalRandom

class BlockRegion(val point1: Block, val point2: Block) {

    operator fun contains(location: Location): Boolean {
        val x1: Int = point1.x
        val z1: Int = point1.z

        val x2: Int = point2.x
        val z2: Int = point2.z

        return location.x > x1 && location.z > z1 && location.x < x2 && location.z < z2
    }

    operator fun contains(player: Player): Boolean = player.location in this

    fun fill(block: Material) {
        for (x in point1.x rangeBetween point2.x) {
            for (y in point1.y rangeBetween point2.y) {
                for (z in point1.z rangeBetween point2.z) {
                    point1.world.getBlockAt(x, y, z).type = block
                }
            }
        }
    }

    private infix fun Int.rangeBetween(other: Int) =
        if (this > other) this downTo other else this .. other

    fun randomX(): Int {
        val x1 = point1.x
        val x2 = point2.x
        val random = ThreadLocalRandom.current()
        return if(x1 == x2) {
            x1
        }else if(x1 > x2) {
            random.nextInt(x2, x1)
        }else {
            random.nextInt(x1, x2)
        }
    }

    fun randomY(): Int {
        val x1 = point1.y
        val x2 = point2.y
        val random = ThreadLocalRandom.current()
        return if(x1 == x2) {
            x1
        }else if(x1 > x2) {
            random.nextInt(x2, x1)
        }else {
            random.nextInt(x1, x2)
        }
    }

    fun randomZ(): Int {
        val x1 = point1.z
        val x2 = point2.z
        val random = ThreadLocalRandom.current()
        return if(x1 == x2) {
            x1
        }else if(x1 > x2) {
            random.nextInt(x2, x1)
        }else {
            random.nextInt(x1, x2)
        }
    }
}