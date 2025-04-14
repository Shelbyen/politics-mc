package funn.j2k.politicsMc.custom_map.maps

import funn.j2k.politicsMc.custom_map.FragmentData
import funn.j2k.politicsMc.custom_map.RenderBuffer
import funn.j2k.politicsMc.utilities.Grid
import org.bukkit.Color

val CLEAR_COLOR = Color.fromARGB(50, 0, 0, 0)

interface Map {
    fun getMap(): Grid<Color>
    fun update() {}
}

class EmptyMap : Map {
    private val buffer = RenderBuffer(64, 64) { FragmentData(CLEAR_COLOR) }
    override fun getMap() = buffer.map { it.color }
}

