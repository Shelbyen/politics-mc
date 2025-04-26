package funn.j2k.politicsMc.ore_control.api.feature

import funn.j2k.politicsMc.ore_control.api.util.LocatedAble
import funn.j2k.politicsMc.ore_control.api.util.SaveAble
import funn.j2k.politicsMc.ore_control.api.util.ValueType
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import org.bukkit.util.BlockVector
import java.util.*

interface Value<V : Value<V, T, O>?, T : ValueType<V, T, O>?, O> : Cloneable, SaveAble,
    LocatedAble {
    val valueType: T

    fun getValue(worldInfo: WorldInfo, random: Random, position: BlockVector, limitedRegion: LimitedRegion): O

    public override fun clone(): Value<*, *, *>
}
