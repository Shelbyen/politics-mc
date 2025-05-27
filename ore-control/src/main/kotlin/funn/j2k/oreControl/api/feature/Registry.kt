package funn.j2k.oreControl.api.feature

import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import java.util.*

class Registry<V : Keyed?> {
    private val values: MutableMap<NamespacedKey, V> = LinkedHashMap()

    fun get(key: NamespacedKey): Optional<V & Any> {
        return Optional.ofNullable(values[key])
    }

    fun register(value: V) {
        values[value!!.key] = value
    }

    fun getValues(): Map<NamespacedKey, V> {
        return values
    }
}
