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


    //    private val database: MongoCollection<Document>
//    private val gson = Gson()
//
//    init {
//        logger.info("Loading MongoDB database")
//        try {
//            configSettings.host!!
//            configSettings.port!!
//            configSettings.database!!
//        }catch (ex: NullPointerException) {
//            logger.error("If you are seeing this, you have missed out a crucial piece of information in your config.")
//            logger.error("Please make sure your host, port & database is defined in the settings.yml")
//            throw NullPointerException(ex.message)
//        }
//        val connectionString: String = if(configSettings.authentication.enabled) {
//            "mongodb://${configSettings.authentication.username}:${configSettings.authentication.password}@${configSettings.host}:${configSettings.port}"
//        } else {
//            "mongodb://${configSettings.host}:${configSettings.port}"
//        }
//
//        val client = MongoClient(MongoClientURI(connectionString))
//        val mongoDb: MongoDatabase = client.getDatabase(configSettings.database)
//        database = mongoDb.getCollection(configSettings.database)
//    }
//
//    override fun getPlayerData(uuid: UUID): PlayerData {
//        if(database.find(Document("_id", uuid.toString())).first() == null) {
//            return PlayerData(uuid).also {
//                database.insertOne(Document.parse(serializePlayerData(it, uuid)))
//            }
//        }
//
//       return this.deserializePlayerData(database.find(Document("_id", uuid.toString())).first()!!.toJson())
//    }
//
//    override fun updatePlayerData(uuid: UUID, data: PlayerData) {
//        database.replaceOne(Document("_id", uuid.toString()), Document.parse(serializePlayerData(data, uuid)))
//    }
//
//    private fun serializePlayerData(playerData: PlayerData, uuid: UUID): String {
//        val data: JsonObject = gson.toJsonTree(playerData) as JsonObject
//        data.addProperty("_id", uuid.toString())
//        return gson.toJson(data)
//    }
//
//    private fun deserializePlayerData(dataStr: String): PlayerData {
//        val data: JsonObject = gson.fromJson(dataStr, JsonObject::class.java)
//        data.remove("_id")
//        return gson.fromJson(data, PlayerData::class.java)
//    }
}