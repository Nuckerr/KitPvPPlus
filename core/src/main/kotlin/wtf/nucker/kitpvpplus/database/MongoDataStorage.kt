package wtf.nucker.kitpvpplus.database

import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.query.experimental.filters.Filters
import wtf.nucker.kitpvpplus.`object`.PlayerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger
import java.util.*

class MongoDataStorage(override val configSettings: DatabaseConfigSettings) : DataStorageMethod {

    private val datastore: Datastore

    init {
        logger.info("Loading MongoDB database")
        try {
            configSettings.host!!
            configSettings.port!!
            configSettings.database!!
        }catch (ex: NullPointerException) {
            logger.error("If you are seeing this, you have missed out a crucial piece of information in your config.")
            logger.error("Please make sure your host, port & database is defined in the settings.yml")
            throw NullPointerException(ex.message)
        }
        val connectionString: String = if(configSettings.authentication.enabled) {
            "mongodb://${configSettings.authentication.username}:${configSettings.authentication.password}@${configSettings.host}:${configSettings.port}"
        } else {
            "mongodb://${configSettings.host}:${configSettings.port}"
        }

        datastore = Morphia.createDatastore(MongoClients.create(connectionString), configSettings.database)
        datastore.mapper.map(PlayerData::class.java)
    }

    override fun getPlayerData(uuid: UUID): PlayerData {
        return datastore
            .find(PlayerData::class.java)
            .filter(Filters.eq("_id", uuid.toString())).first() ?: return PlayerData(uuid = uuid.toString()).also { datastore.save(it) }
    }

    override fun updatePlayerData(uuid: UUID, data: PlayerData) {
        datastore.save(data)
    }

    override fun disconnect() {
        datastore.session?.close()
    }
}
