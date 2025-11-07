package funn.j2k.politicsMc.gui.maps

import funn.j2k.politicsMc.gui.utilities.data_structures.Grid
import funn.j2k.politicsMc.gui.utilities.maths.denormalize
import funn.j2k.politicsMc.gui.utilities.rendering.RenderGroup
import org.bukkit.Color
import org.bukkit.World
import org.bukkit.util.Vector
import org.bukkit.util.noise.SimplexNoiseGenerator
import org.joml.Matrix4f
import org.joml.Quaternionf
import java.util.WeakHashMap
import kotlin.math.pow


enum class OreMode {
	IRON,
	COAL,
}


class MapState {
	var shutDownTime = -1
	var ticksLived = 0
	val ui = MapUIState()
}

class Map {
	var selectedOre = OreMode.IRON
    var octaves = 2
    var frequency = 4.0f
    var scale = 0.007
	var noiseSeed = 1

	var mapScale = 2f

	var gridSize = 65
	var bitmap = Grid(gridSize, gridSize) { Color.fromARGB(50, 0, 0, 0) }
	var transform = Matrix4f()
	var renderControls = true


	var state: MapState
		get() = mapStates.getOrPut(this) { MapState() }
		set(it) { mapStates[this] = it }

	fun changeResolution(dz: Int) {
		gridSize = dz
		bitmap = Grid(gridSize, gridSize) { Color.fromARGB(50, 0, 0, 0) }
		update()
	}

	fun update() {
		state.ticksLived++
		for ((px, py) in bitmap.indices()) {
            val noiseValue = SimplexNoiseGenerator(noiseSeed.toLong()).getNormaliseNoise(
                (gridSize / 2 + px).toDouble(),
                (gridSize / 2 + py).toDouble(),
                scale, octaves, frequency.toDouble(), powScale = 0.2
            )
            val color = noiseValue.denormalize(0.0, 255.0).toInt()
            bitmap[px to py] = Color.fromRGB(color, color, color)
        }
	}

	fun render(world: World, position: Vector): RenderGroup {

		val grid = buildMap(
			world = world,
			position = position,
			quaternion = transform.getNormalizedRotation(Quaternionf()),
			bitmap = bitmap,
			mapScale = mapScale
		)


		val controls = buildMapControls(
			world = world,
			position = position,
			this,
		)



		return RenderGroup().apply {
			this[0] = grid
			if (renderControls) this[1] = controls
		}
	}

	companion object {
		val mapStates = WeakHashMap<Map, MapState>()
	}
}

fun SimplexNoiseGenerator.getNormaliseNoise(x: Double, y: Double, coordinateScale: Double = 0.007,
                                            octaves: Int = 2, frequency: Double = 4.0, amplitude: Double = 1.0,
                                            powScale: Double = 1.0): Double {
	val noiseValue = (this.noise(
		x * coordinateScale,
		y * coordinateScale,
		octaves, frequency, amplitude
	) / octaves)

	return if (powScale % 2 == 0.0) (noiseValue.pow(powScale) + 1) / 2 else (noiseValue + 1) / 2
}
