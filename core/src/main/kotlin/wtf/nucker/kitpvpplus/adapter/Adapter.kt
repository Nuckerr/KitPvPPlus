package wtf.nucker.kitpvpplus.adapter

import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

interface Adapter<T>: TypeSerializer<T> {

    val type: Class<T>

}

fun <T> TypeSerializerCollection.Builder.register(adapter: Adapter<T>): TypeSerializerCollection.Builder {
    this.register(adapter.type, adapter)
    return this
}