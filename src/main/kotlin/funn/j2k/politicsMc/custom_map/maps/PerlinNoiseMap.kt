package funn.j2k.politicsMc.custom_map.maps

import funn.j2k.politicsMc.custom_map.bitmapToRenderEntities
import funn.j2k.politicsMc.utilities.rendering.SharedEntityRenderer
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.bukkit.util.noise.SimplexNoiseGenerator


class PerlinNoiseMap(
    player: Player, localPosition: Vector = Vector(0.8f, -0.4f, 1f)
) : Map(player, localPosition) {
    init {
        updateMap()
    }

    private fun updateMap() {
        for ((px, py) in bitmap.indices()) {
            val color = ((SimplexNoiseGenerator.getInstance().noise(
                (player.chunk.x - size / 2 + px).toDouble(),
                (player.chunk.z - size / 2 + py).toDouble()
            ) + 1) * 255 / 2).toInt()
            bitmap[px to py] = Color.fromRGB(color, color, color)
        }
    }

    override fun update() {
        if (lastChunkPosition.x != player.chunk.x || lastChunkPosition.z != player.chunk.z) {
            updateMap()
            lastChunkPosition = player.chunk
        }

        val posValues = calculatePosition()

        SharedEntityRenderer.render(
            this, bitmapToRenderEntities(
                world = player.world,
                position = posValues.first,
                quaternion = posValues.second,
                bitmap = getMap()
            )
        )
    }
}