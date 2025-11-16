package funn.j2k.politicsMc.common_utilities.events

import funn.j2k.politicsMc.gui.utilities.currentPlugin
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.world.AsyncStructureSpawnEvent
import org.bukkit.event.world.PortalCreateEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.io.Closeable

fun addEventListener(listener: Listener): Closeable {
    val plugin = currentPlugin
    plugin.server.pluginManager.registerEvents(listener, plugin)
    return Closeable {
        HandlerList.unregisterAll(listener)
    }
}

fun onInteractEntity(listener: (Player, Entity, EquipmentSlot) -> Unit): Closeable {
    return addEventListener(object : Listener {
        @EventHandler
        fun onInteract(event: PlayerInteractEntityEvent) {
            listener(event.player, event.rightClicked, event.hand)
        }
    })
}


fun onInteractEntity(listener: (event: PlayerInteractEntityEvent) -> Unit): Closeable {
    return addEventListener(object : Listener {
        @EventHandler
        fun onInteract(event: PlayerInteractEntityEvent) {
            listener(event)
        }
    })
}

fun onSpawnEntity(listener: (Entity) -> Unit): Closeable {
    return addEventListener(object : Listener {
        @EventHandler
        fun onSpawn(event: EntitySpawnEvent) {
            listener(event.entity)
        }
    })
}

fun onGestureUseItem(listener: (Player, ItemStack) -> Unit) = addEventListener(object : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.action == Action.RIGHT_CLICK_BLOCK && !(event.clickedBlock?.type?.isInteractable == false || event.player.isSneaking)) return
        listener(event.player, event.item ?: return)
    }
})

fun onPortalSpawn(listener: (event: AsyncStructureSpawnEvent) -> Unit): Closeable {
	return addEventListener(object : Listener  {
		@EventHandler
		fun onStructureSpawn(event: AsyncStructureSpawnEvent) {
			if (event.structure.structureType.key.key != "ruined_portal") return
			listener(event)
		}
	})
}

fun onPortalCreate(listener: (event: PortalCreateEvent) -> Unit): Closeable {
	return addEventListener(object : Listener  {
		@EventHandler
		fun onPortalCreate(event: PortalCreateEvent) {
			if (event.reason != PortalCreateEvent.CreateReason.FIRE) return
			listener(event)
		}
	})
}
