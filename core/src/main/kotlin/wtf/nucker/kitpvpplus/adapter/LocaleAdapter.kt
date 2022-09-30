package wtf.nucker.kitpvpplus.adapter

import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.Type
import java.util.*

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 23/09/2022
 */
class LocaleAdapter: Adapter<Locale> {

    override val type = Locale::class.java

    override fun deserialize(type: Type, target: ConfigurationNode): Locale? {
        if(target.isNull) return null
        return Locale.forLanguageTag(target.string)
    }

    override fun serialize(type: Type, locale: Locale?, target: ConfigurationNode) {
        if(locale == null) {
            target.raw(null)
            return
        }

        target.set(locale.toLanguageTag())
    }

    companion object {
        val INSTANCE = LocaleAdapter()
    }
}