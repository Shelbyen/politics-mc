package funn.j2k.politicsMc.gui.maps

import funn.j2k.politicsMc.gui.utilities.data_structures.Grid
import funn.j2k.politicsMc.gui.utilities.maths.RIGHT_VECTOR
import funn.j2k.politicsMc.gui.utilities.rendering.RenderGroup
import funn.j2k.politicsMc.gui.utilities.rendering.interpolateTransform
import funn.j2k.politicsMc.gui.utilities.rendering.renderText
import org.bukkit.Color
import org.bukkit.World
import org.bukkit.entity.Display.Brightness
import org.bukkit.util.Vector
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f


val textBackgroundTransform: Matrix4f
	get() = Matrix4f()
		.translate(-0.1f + .5f, -0.5f + .5f, 0f)
		.scale(8.0f, 4.0f, 1f)

fun buildMap(
	world: World,
	position: Vector,
	quaternion: Quaternionf,
	bitmap: Grid<Color>,
	mapScale: Float = 1f
): RenderGroup {
	val group = RenderGroup()

	val scale = mapScale / bitmap.height
	val rotPerStride = 0f

	val currentOffset = Vector3f()
	val currentRotation = Quaternionf().rotateY(rotPerStride * -bitmap.width / 2)

	val strideRotation = Quaternionf().rotateY(rotPerStride)
	val stride = RIGHT_VECTOR.multiply(scale).toVector3f().rotate(currentRotation)

	// calculate width so we can offset it by half
	val widthStride = Vector3f(stride)
	for (x in 0 until bitmap.width) {
		widthStride.rotate(strideRotation)
		currentOffset.sub(widthStride)
	}
	currentOffset.mul(.5f)


	for (x in 0 until bitmap.width) {
		stride.rotate(strideRotation)
		currentOffset.add(stride)
		currentRotation.premul(strideRotation)

		val offset = Vector3f(currentOffset)
		val rotation = Quaternionf(currentRotation)

		for (y in 0 until bitmap.height) {
			val transform = Matrix4f()
				.rotate(quaternion)
				.translate(offset.x, offset.y + scale * y, offset.z)
				.rotate(rotation)
				.scale(scale)

			group[x to y] = renderText(
				world = world,
				position = Vector(position.x, position.y - 2.0, position.z),
				init = {
					it.text = " "
					it.brightness = Brightness(15, 15)
					it.interpolationDuration = 1
					it.teleportDuration = 1
				},
				update = {
					it.interpolateTransform(Matrix4f(transform).mul(textBackgroundTransform))
					it.backgroundColor = bitmap[x to y]
				}
			)
		}
	}

	return group
}
