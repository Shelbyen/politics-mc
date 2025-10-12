package funn.j2k.politicsMc.custom_map

import funn.j2k.politicsMc.custom_map.maps.Map
import funn.j2k.politicsMc.custom_map.maps.PerlinNoiseMap
import funn.j2k.politicsMc.custom_map.utilities.*
import funn.j2k.politicsMc.custom_map.utilities.rendering.RenderEntityGroup
import funn.j2k.politicsMc.custom_map.utilities.rendering.interpolateTransform
import funn.j2k.politicsMc.custom_map.utilities.rendering.textRenderEntity
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Display.Brightness
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f


val textBackgroundTransform: Matrix4f; get() = Matrix4f()
    .translate(-0.1f + .5f, -0.5f + .5f, 0f)
    .scale(8.0f, 4.0f, 1f) //  + 0.003f  + 0.001f

fun isSimilar(location1: Location, location2: Location): Boolean {
    val x1 = location1.x
    val y1 = location1.y
    val z1 = location1.z
    val x2 = location2.x
    val y2 = location2.y
    val z2 = location2.z
    return (x1 == x2 && z1 == z2 && y2 <= y1)
}


internal val maps = mutableMapOf<String, Map>()

@Suppress("UnstableApiUsage")
fun setupCustomMap() {
    fun Player.lockMovement() {
        if (player?.scoreboardTags?.contains("pplayer_lock") == true) {
            player?.scoreboardTags?.add("pplayer_lock_active")
            return
        }

        player?.scoreboardTags?.add("pplayer_lock_active")
        player?.scoreboardTags?.add("pplayer_lock")
    }

    onPlayerMove { event ->
        if (event.player.scoreboardTags.contains("pplayer_lock")) {
            if (!event.player.scoreboardTags.contains("pplayer_lock_active")) {
                event.player.scoreboardTags.remove("pplayer_lock")
                event.player.scoreboardTags.remove("pplayer_lock_active")
                return@onPlayerMove
            }

            val fromLocation: Location = event.from
            val toLocation = event.to
            if (isSimilar(fromLocation, toLocation)) {
                return@onPlayerMove
            }

            event.to = fromLocation

            event.player.scoreboardTags.remove("pplayer_lock_active")
        }
    }

    val randomMap = CustomItemComponent("random_map")
    customItemRegistry += createNamedItem(org.bukkit.Material.BREEZE_ROD, "Random Map").attach(randomMap)
    randomMap.onGestureUse { player, _ ->
        if (maps[player.name] != null) {
            maps.remove(player.name)
            return@onGestureUse
        }
        maps[player.name] = PerlinNoiseMap(player)
    }

    val moveCamera = CustomItemComponent("move_map")
    customItemRegistry += createNamedItem(org.bukkit.Material.CLOCK, "Move Map").attach(moveCamera)
    moveCamera.onHeldTick { player, _ ->
        val map = maps[player.name] ?: return@onHeldTick
        player.lockMovement()

        sendDebugMessage(map.localPosition.toString())

        val input = player.currentInput
        if (input.isLeft) map.localPosition.x += .1
        if (input.isRight) map.localPosition.x -= .1
        if (input.isForward) map.localPosition.y += .1
        if (input.isBackward) map.localPosition.y -= .1
    }

    val setValue = CustomItemComponent("set_value_item")
    customItemRegistry += createNamedItem(org.bukkit.Material.CLOCK, "Set Value").attach(setValue)
    setValue.onHeldTick { player, _ ->
        val map = maps[player.name] ?: return@onHeldTick
        if (map !is PerlinNoiseMap) {
            return@onHeldTick
        }

        player.lockMovement()

        sendDebugMessage(map.frequency.toString() + " " + map.scale.toString())


        val input = player.currentInput
        if (input.isLeft) map.scale -= .001
        if (input.isRight) map.scale += .001
        if (input.isForward) map.frequency += .1
        if (input.isBackward) map.frequency -= .1
        map.updateMap()
    }

    val zoomMap = CustomItemComponent("zoom_map_item")
    customItemRegistry += createNamedItem(org.bukkit.Material.SPECTRAL_ARROW, "Zoom Map").attach(zoomMap)
    zoomMap.onHeldTick { player, _ ->
        val map = maps[player.name] ?: return@onHeldTick
        if (map !is PerlinNoiseMap) {
            return@onHeldTick
        }

        player.lockMovement()

        sendDebugMessage(map.octaves.toString() + " " + map.size.toString())

        val input = player.currentInput
        if (input.isLeft) map.octaves -= 1
        if (input.isRight) map.octaves += 1
        if (input.isForward) map.zoom(-1)
        if (input.isBackward) map.zoom(1)
        map.updateMap()
    }

    onTick {
        maps.toList().forEach { it.second.update() }
    }
}


fun bitmapToRenderEntities(
    world: World,
    position: Vector,
    quaternion: Quaternionf,
    bitmap: Grid<Color>,
): RenderEntityGroup {
    val group = RenderEntityGroup()

    val scale = 1.0f / bitmap.height
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

            group.add(x to y, textRenderEntity(
                world = world,
                position = position,
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
            ))
        }
    }

    return group
}
