package wtf.nucker.kitpvpplus.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * A class for easily connecting to and using MongoDB
 *
 * @author Nucker
 */
public class MongoDatabase {

    private final MongoClient client;

    /**
     * Connect to your database
     *
     * @param uri The full uri to connect to.
     * @see MongoDatabase#buildURI(String, int) MongoDatabase#buildURI(String, int) for building a uri without authentication by passing the params
     * @see MongoDatabase#buildURI(String, int, String, String) MongoDatabase#buildURI(String, int, String, String) for building a uri with authentication by passing params
     */
    public MongoDatabase(String uri) {
        this.client = new MongoClient(new MongoClientURI(uri));
    }

    /**
     * Used to easily update data in a collection
     *
     * @param collectionName The collection you want to save to
     * @param filter         The filter used to find the document you want to update
     * @param key            The key of the data you want to update
     * @param value          The updated value
     */
    public void save(String collectionName, Bson filter, String key, Object value) {
        getCollection(collectionName).replaceOne(filter, getCollection(collectionName).find(filter).first().append(key, value));
    }

    /**
     * Easily collection from your database
     *
     * @param name The name of the collection
     * @return An instance of the collection
     */
    public MongoCollection<Document> getCollection(String name) {
        return client.getDatabase("KitPvPPlus").getCollection(name);
    }

    /**
     * Get the instance of the client
     *
     * @return The instance of the client
     */
    public MongoClient getClient() {
        return client;
    }

    /**
     * Used for building uris by just passing params <b>This version does not support authentication</b>
     *
     * @param host The host of the database
     * @param port The port the database is on
     * @return The string of the uri with the params provided
     */
    public static String buildURI(String host, int port) {
        return "mongodb://[host]:[port]".replace("[host]", host).replace("[port]", String.valueOf(port));
    }

    /**
     * Used for building uris by just passing params <b>This version does not support authentication</b>
     *
     * @param host     The host of the database
     * @param port     The port the database is on
     * @param username The username of the user your logging into
     * @param password The password of the user your logging into
     * @return The string of the uri with the params provided
     */
    public static String buildURI(String host, int port, String username, String password) {
        return "mongodb://[username]:[password]@[host]:[port]/?authSource=admin".replace("[username]", "[password]").replace("[host]", host).replace("[port]",
                String.valueOf(port).replace("[username]", username).replace("[password]", password));
    }
}
