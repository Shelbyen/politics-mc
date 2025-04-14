package funn.j2k.politicsMc.custom_map

import funn.j2k.politicsMc.custom_map.maps.Map
import funn.j2k.politicsMc.custom_map.maps.PerlinNoiseMap
import funn.j2k.politicsMc.utilities.*
import funn.j2k.politicsMc.utilities.rendering.RenderEntityGroup
import funn.j2k.politicsMc.utilities.rendering.interpolateTransform
import funn.j2k.politicsMc.utilities.rendering.textRenderEntity
import org.bukkit.Color
import org.bukkit.World
import org.bukkit.entity.Display.Brightness
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.joml.*


val textBackgroundTransform: Matrix4f; get() = Matrix4f()
    .translate(-0.1f + .5f, -0.5f + .5f, 0f)
    .scale(8.0f, 4.0f, 1f) //  + 0.003f  + 0.001f


internal val maps = mutableMapOf<String, Map>()

@Suppress("UnstableApiUsage")
fun setupCustomMap() {
    fun Player.lockMovement() {
        if (vehicle != null) {
            if (vehicle?.scoreboardTags?.contains("player_lock") == true) {
                vehicle?.scoreboardTags?.add("player_lock_active")
            }
            return
        }

        world.spawn(eyeLocation.add(.0, -1.02, .0), org.bukkit.entity.ArmorStand::class.java) {
            it.setGravity(false)
            it.isInvisible = true
            it.isInvulnerable = true
            it.isSilent = true
            it.isCollidable = false
            it.isMarker = true
            it.scoreboardTags.add("player_lock")
            it.scoreboardTags.add("player_lock_active")
            it.addPassenger(this)
        }
    }

    onTick {
        for (entity in EntityTag("player_lock").getEntities()) {
            if (!entity.scoreboardTags.contains("player_lock_active") || entity.passengers.isEmpty()) {
                entity.remove()
                continue
            }

            entity.scoreboardTags.remove("player_lock_active")
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

//        val input = player.currentInput
//        if (input.isLeft) map.localPosition.x -= .1
//        if (input.isRight) map.localPosition.x += .1
//        if (input.isForward) map.localPosition.y += .1
//        if (input.isBackward) map.localPosition.y -= .1
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
