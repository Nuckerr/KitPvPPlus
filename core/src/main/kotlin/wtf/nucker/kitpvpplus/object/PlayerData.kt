package wtf.nucker.kitpvpplus.`object`

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*

@Entity("PlayerData")
@DatabaseTable(tableName = "playerdata")
@ConfigSerializable
data class PlayerData(
    @Id @DatabaseField(id = true) val uuid: String? = null /*** UUID must not be null for databases. Only for local storage. */ ,
    @DatabaseField var exp: Double = 0.0,
    @DatabaseField var money: Double = 0.0,
    @DatabaseField var kills: Int = 0,
    @DatabaseField var deaths: Int = 0
)