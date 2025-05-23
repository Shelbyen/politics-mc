package funn.j2k.politicsMc.ore_control.api.util

import funn.j2k.politicsMc.ore_control.api.feature.Value
import org.bukkit.Keyed

interface ValueType<V : Value<V, T, O>?, T : ValueType<V, T, O>?, O> : Keyed {
    val parser: Parser<V>?

    fun createNewValue(): V
}
