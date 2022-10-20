package wtf.nucker.kitpvpplus.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.concurrent.ThreadLocalRandom

class BlockRegion(val point1: Block, val point2: Block) {

    private val largerXPoint: Int
    private val largerYPoint: Int
    private val largerZPoint: Int

    private val smallerXPoint: Int
    private val smallerYPoint: Int
    private val smallerZPoint: Int

    init {
        if(point1.x >= point2.x) {
            largerXPoint = point1.x
            smallerXPoint = point2.x
        } else {
            largerXPoint = point2.x
            smallerXPoint = point1.x
        }

        if(point1.y >= point2.y) {
            largerYPoint = point1.y
            smallerYPoint = point2.y
        } else {
            largerYPoint = point2.y
            smallerYPoint = point1.y
        }

        if(point1.z >= point2.z) {
            largerZPoint = point1.z
            smallerZPoint = point2.z
        } else {
            largerZPoint = point2.z
            smallerZPoint = point1.z
        }
    }

    operator fun contains(location: Location): Boolean {

        return location.x > smallerXPoint && location.z > smallerZPoint && location.x < largerXPoint && location.z < largerZPoint
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