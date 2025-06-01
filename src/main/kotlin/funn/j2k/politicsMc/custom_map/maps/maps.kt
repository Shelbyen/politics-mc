package funn.j2k.politicsMc.custom_map.maps

import funn.j2k.politicsMc.custom_map.FragmentData
import funn.j2k.politicsMc.custom_map.RenderBuffer
import funn.j2k.politicsMc.custom_map.utilities.Grid
import funn.j2k.politicsMc.custom_map.utilities.rotate
import funn.j2k.politicsMc.custom_map.utilities.yawRadians
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3f

val CLEAR_COLOR = Color.fromARGB(50, 0, 0, 0)

interface Bitmap {
    fun getMap(): Grid<Color>
    fun update() {}
}


abstract class Map(
    val player: Player,
    val localPosition: Vector = Vector(0.8f, -0.4f, 1f)
) : Bitmap {
    var size = 16
    protected var bitmap = Grid(size, size) { CLEAR_COLOR }
    protected var lastChunkPosition = player.chunk
    override fun getMap() = bitmap

    protected fun calculatePosition(): Pair<Vector, Quaternionf> {
        val localPositionRotate = Vector(localPosition.x, localPosition.y, localPosition.z).rotate(
            Quaterniond().rotateYXZ(
                player.eyeLocation.yawRadians().toDouble(),
                0.0, 0.0
            )
        )
        val quaternion = Quaternionf().rotateAxis(
            player.eyeLocation.yawRadians() + Math.toRadians(180.0).toFloat(),
            Vector3f(0f, 1f, 0f)
        )
        return Pair(player.eyeLocation.toVector().add(localPositionRotate), quaternion)
    }
}


class EmptyMap(player: Player, localPosition: Vector) : Map(player, localPosition) {
    private val buffer = RenderBuffer(64, 64) { FragmentData(CLEAR_COLOR) }
    override fun getMap() = buffer.map { it.color }
}

