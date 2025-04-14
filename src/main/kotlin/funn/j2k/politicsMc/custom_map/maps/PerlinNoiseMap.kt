package funn.j2k.politicsMc.custom_map.maps

import funn.j2k.politicsMc.custom_map.bitmapToRenderEntities
import funn.j2k.politicsMc.utilities.*
import funn.j2k.politicsMc.utilities.rendering.SharedEntityRenderer
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.bukkit.util.noise.SimplexNoiseGenerator
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3f


class PerlinNoiseMap(
    private val player: Player,
    private val localPosition: Vector
) : Map {
    private val size = 16
    private val bitmap = Grid(size, size) { CLEAR_COLOR }
    private var lastChunkPosition = player.chunk
    override fun getMap() = bitmap

    init {
        updateMap()
    }

    private fun updateMap() {
        for ((px, py) in bitmap.indices()) {
            val color = ((SimplexNoiseGenerator.getInstance().noise(
                (player.chunk.x - size / 2 + px).toDouble(),
                (player.chunk.z - size / 2 + py).toDouble()) + 1) * 255 / 2).toInt()
            bitmap[px to py] = Color.fromRGB(color, color, color)
        }
    }

    private fun calculatePosition(): Vector {
        val localPositionRotate = Vector(localPosition.x, localPosition.y, localPosition.z).rotate(
            Quaterniond().rotateYXZ(
                player.eyeLocation.yawRadians().toDouble(),
                0.0, 0.0
            )
        )
        return (player.eyeLocation.toVector().add(localPositionRotate))
    }

    override fun update() {
        if (lastChunkPosition.x != player.chunk.x || lastChunkPosition.z != player.chunk.z) {
            updateMap()
            lastChunkPosition = player.chunk
        }

        val quaternion = Quaternionf().rotateAxis(player.eyeLocation.yawRadians() + Math.toRadians(180.0).toFloat(), Vector3f(0f, 1f, 0f))

        SharedEntityRenderer.render(
            this, bitmapToRenderEntities(
                world = player.world,
                position = calculatePosition(),
                quaternion = quaternion,
                bitmap = getMap()
            )
        )
    }
}