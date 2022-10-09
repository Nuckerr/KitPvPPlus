package wtf.nucker.kitpvpplus.`object`

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt

@Entity("PlayerData")
@DatabaseTable(tableName = "playerdata")
@ConfigSerializable
data class PlayerData(
    @Id @DatabaseField(id = true) val uuid: String? = null /*** UUID must not be null for databases. Only for local storage. */ ,
    @DatabaseField var exp: Double = 0.0,
    @DatabaseField var money: Double = 0.0,
    @DatabaseField var kills: Int = 0,
    @DatabaseField var deaths: Int = 0,
    @DatabaseField var topKillStreak: Int = 0
) {

    @DatabaseField var killStreak: Int = 0
        set(value) {
            field = value
            if(killStreak > topKillStreak) topKillStreak = killStreak
        }

    val killDeathRatio: Double
        get() {
            if(deaths <= 0 || kills <= 0) return 0.0
            val ratio = kills.toDouble() / deaths.toDouble()
            return (ratio * 100.0).roundToInt() / 100.0 // 2 dp rounding
        }

    val level: Int
        get() = floor(exp).toInt()

    val nextLevelPercentage: Int
        // Exp is level.percentage (eg 4.56). Take the level away leaves you with percentage in decimal. Times by 100 and boom!
        get() = ((exp - level) * 100).roundToInt()
}