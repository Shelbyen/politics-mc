package funn.j2k.politicsMc.custom_map.maps

import funn.j2k.politicsMc.custom_map.bitmapToRenderEntities
import funn.j2k.politicsMc.custom_map.maps
import funn.j2k.politicsMc.custom_map.utilities.Grid
import funn.j2k.politicsMc.custom_map.utilities.rendering.SharedEntityRenderer
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.bukkit.util.noise.SimplexNoiseGenerator


class PerlinNoiseMap(
    player: Player, localPosition: Vector = Vector(0.8f, -0.4f, 1f)
) : Map(player, localPosition) {
    private val noiseGenerator = SimplexNoiseGenerator(123123123)
    var octaves = 8
    var frequency = 0.009
    var amplitude = 1.0

    init {
        updateMap()
    }

    fun updateMap() {
        for ((px, py) in bitmap.indices()) {
            val noiseValue = noiseGenerator.noise(
                (player.chunk.x - size / 2 + px).toDouble(),
                (player.chunk.z - size / 2 + py).toDouble(),
                octaves, frequency, amplitude
            ) - 1

            val color = if ((noiseValue * 255 / 2).toInt() in 0..255) (noiseValue * 255 / 2).toInt() else 0

            bitmap[px to py] = Color.fromRGB(color, color, color)
        }
    }

    fun zoom(dz: Int) {
        size += dz
        bitmap = Grid(size, size) { CLEAR_COLOR }
        update()
        updateMap()
    }

    override fun update() {
        if (!player.isOnline) {
            maps.remove(player.name)
            return
        }
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