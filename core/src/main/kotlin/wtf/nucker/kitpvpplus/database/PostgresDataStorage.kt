package wtf.nucker.kitpvpplus.database

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import wtf.nucker.kitpvpplus.`object`.PlayerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger
import java.util.*

class PostgresDataStorage(override val configSettings: DatabaseConfigSettings) : DataStorageMethod {

    private val connection: JdbcConnectionSource
    private val dao: Dao<PlayerData, String>

    init {
        try {
            configSettings.host!!
            configSettings.port!!
            configSettings.database!!
        }catch (ex: NullPointerException) {
            logger.error("If you are seeing this, you have missed out a crucial piece of information in your config.")
            logger.error("Please make sure your host, port & database is defined in the settings.yml")
            throw NullPointerException(ex.message)
        }

        connection = if(configSettings.authentication.enabled) {
            JdbcConnectionSource(
                "jdbc:postgresql://${configSettings.host}:${configSettings.port}/${configSettings.database}",
                configSettings.authentication.username, configSettings.authentication.password)
        }else {
            JdbcConnectionSource("jdbc:postgresql://${configSettings.host}:${configSettings.port}/${configSettings.database}")
        }
        dao = DaoManager.createDao(connection, PlayerData::class.java)
        TableUtils.createTableIfNotExists(connection, PlayerData::class.java)
    }

    override fun getPlayerData(uuid: UUID): PlayerData {
        dao.createIfNotExists(PlayerData(uuid = uuid.toString()))
        return dao.queryForId(uuid.toString())
    }

    override fun updatePlayerData(uuid: UUID, data: PlayerData) {
        dao.update(data)
    }

    override fun disconnect() {
        connection.closeQuietly()
    }
}