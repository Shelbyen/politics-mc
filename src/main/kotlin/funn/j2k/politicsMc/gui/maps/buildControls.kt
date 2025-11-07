package funn.j2k.politicsMc.gui.maps

import funn.j2k.politicsMc.gui.ui.SliderState
import funn.j2k.politicsMc.gui.ui.radioButton
import funn.j2k.politicsMc.gui.ui.snapTo
import funn.j2k.politicsMc.gui.utilities.maths.denormalize
import funn.j2k.politicsMc.gui.utilities.maths.normalize
import funn.j2k.politicsMc.gui.utilities.maths.toRadians
import funn.j2k.politicsMc.gui.utilities.rendering.EmptyRenderItem
import funn.j2k.politicsMc.gui.utilities.rendering.RenderGroup
import funn.j2k.politicsMc.gui.utilities.rendering.RenderItem
import funn.j2k.politicsMc.gui.utilities.rendering.interpolateTransform
import funn.j2k.politicsMc.gui.utilities.rendering.renderItem
import funn.j2k.politicsMc.gui.utilities.customSendActionBar
import funn.j2k.politicsMc.gui.utilities.data_structures.Grid
import org.bukkit.entity.Player
import org.bukkit.*
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.joml.Matrix4f
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.text.set

class MapUIState {
	val scaleSliderState = SliderState()
	val frequencySliderState = SliderState()
	val octavesSliderState = SliderState()
	val resolutionSliderState = SliderState()
}

fun buildMapControls(
	world: World,
	position: Vector,
	map: Map,
): RenderItem {
	val group = RenderGroup()

	val controlsTransform = Matrix4f(map.transform)
		.translate(0f, -.5f - map.mapScale, .75f)

	fun opacity(order: Int): Float {
		val fadeDuration = 3
		val baseDelay = 10
		val delay = fadeDuration

		// fade out in reverse order
		if (map.state.shutDownTime >= 0) {
			val shutdownOrder = 8 - order
			val fade = (map.state.shutDownTime - (shutdownOrder * delay + baseDelay)).toFloat() / fadeDuration
			return (1f - fade).coerceIn(0f,1f)
		}

		val fade = (map.state.ticksLived - (order * delay + baseDelay)).toFloat() / fadeDuration
		return fade.coerceIn(0f,1f)
	}

	fun radioOption(
		order: Int,
		name: String,
		rotation: Double = .0,
		offset: Vector,
		apply: (Map) -> Unit,
		isSelected: Boolean,
	): RenderItem {
		return radioButton(
			world = world,
			position = position,
			matrix = Matrix4f(controlsTransform)
				.translate(offset.toVector3f())
				.rotateY(rotation.toFloat())
				.rotateX(15f.toRadians())
				.scale(.9f),
			text = name,
			isSelected = isSelected,
			opacity = opacity(order),
			onClick = { apply(map) }
		)
	}

	val rot = 35.0.toRadians()

	group["iron"] = radioOption(
		order = 2,
		name = "Iron",
		apply = {},
		rotation = rot,
		offset = Vector(-1, 0, 0).add(Vector(-1.3, .0, .0).rotateAroundY(rot)),
		isSelected = map.selectedOre == OreMode.IRON
	)
	group["coal"] = radioOption(
		order = 1,
		name = "Coal",
		rotation = .0,
		offset = Vector(-.4,.0,.0),
		apply = {},
		isSelected = map.selectedOre == OreMode.COAL
	)

	fun slider(
		order: Int,
		state: SliderState,
		progress: Float,
		onChange: (Float, Player) -> Unit,
		transformer: (Float) -> Float = { it },
		offset: Float,
		icon: ItemStack,
	): RenderItem {
		val opacity = opacity(order)
		if (opacity == 0f) return EmptyRenderItem

		val out = RenderGroup()

		val sliderHeight = 1.3f
		val translation = Vector(1.0 * offset.sign, .5, .0).add(
			Vector(offset, 0f, 0f).rotateAroundY(-rot * offset.sign)
		)

		val transform = Matrix4f(controlsTransform)
			.translate(translation.toVector3f())
			.rotateY(-rot.toFloat() * offset.sign)

		out["slider"] = funn.j2k.politicsMc.gui.ui.slider(
			world = world,
			position = position,
			matrix = Matrix4f(transform).scale(.06f, sliderHeight, 1f),
			state = state,
			transformer = transformer,
			progress = progress,
			onChange = onChange,
			opacity = opacity,
		)
		out["icon"] = renderItem(
			world = world,
			position = position,
			init = {
				it.teleportDuration = 1
				it.interpolationDuration = 1
				it.itemDisplayTransform = ItemDisplay.ItemDisplayTransform.GUI
				it.setItemStack(icon)
				it.brightness = Display.Brightness(15, 15)
			},
			update = {
				it.interpolateTransform(Matrix4f(transform)
					.translate(0f, sliderHeight + .2f, 0f)
					.scale(.2f))

				it.isVisibleByDefault = opacity > .5f
			}
		)


		return out
	}


	group["scale"] = slider(
		order = 3,
		offset = .5f,
		state = map.state.ui.scaleSliderState,
		progress = (map.scale / 0.1).toFloat(),
		icon = ItemStack(Material.SPYGLASS),
		onChange = { newValue, player ->
			map.scale = newValue * 0.1
			player.customSendActionBar("Map scale: ${map.scale}")
		},
	)
	group["frequency"] = slider(
		order = 4,
		offset = .9f,
		state = map.state.ui.frequencySliderState,
		progress = map.frequency.normalize(0f, 8f),
		icon = ItemStack(Material.CLOCK),
//		transformer = { it.snapTo(.5f, .05f) },
		onChange = { newValue, player ->
			map.frequency = newValue.denormalize(0f, 8f)
			player.customSendActionBar("Frequency: ${"%.2f".format(map.frequency)} rps")
		},
	)
	group["octaves"] = slider(
		order = 5,
		offset = 1.5f,
		state = map.state.ui.octavesSliderState,
		progress = map.octaves.toFloat().normalize(1f, 8f),
		icon = ItemStack(Material.POWDER_SNOW_BUCKET),
//		transformer = { it.snapTo(.6f, .05f) },
		onChange = { newValue, player ->
			map.octaves = newValue.denormalize(1f, 8f).roundToInt()
			player.customSendActionBar("Octaves: ${map.octaves}")
		},
	)
	group["resolution"] = slider(
		order = 6,
		offset = -2.4f,
		state = map.state.ui.resolutionSliderState,
		progress = map.gridSize.toFloat().normalize(1f, 100f),
		icon = ItemStack(Material.SPYGLASS),
		transformer = { it.snapTo(.6f, .05f) },
		onChange = { newValue, player ->
			val resolution = newValue.denormalize(1f, 100f).roundToInt()

			map.changeResolution(resolution)
			player.sendActionBar("Resolution: $resolution")
		},
	)

	return group
}
