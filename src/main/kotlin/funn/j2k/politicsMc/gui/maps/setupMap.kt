package funn.j2k.politicsMc.gui.maps

import funn.j2k.politicsMc.gui.utilities.commands.registerEditObjectCommand
import funn.j2k.politicsMc.gui.utilities.custom_entities.CustomEntityComponent
import funn.j2k.politicsMc.gui.utilities.events.onTick
import funn.j2k.politicsMc.gui.utilities.maths.getQuaternion
import funn.j2k.politicsMc.gui.utilities.requireCommand
import funn.j2k.politicsMc.gui.utilities.sendActionBarOrMessage
import org.joml.Matrix4f

fun setupMap() {
	val map = Map()
	onTick {
		map.update()
	}

	val mapComponent = CustomEntityComponent.fromString("map")
	mapComponent.onTick { entity ->
		// copy entity rotation
		map.transform = Matrix4f().rotate(entity.location.getQuaternion())

		// render
		map.render(
			world = entity.world,
			position = entity.location.toVector(),
		).submit(entity)

//		// begin shutdown
//		if (map.state.shutDownTime == 0) {
//			map.state.previousShader = EmptyShader()
//			map.state.shaderTransition = 1.0
//			map.state.shaderTransitionReversed = true
//		}

		// remove after shutdown
		if (map.state.shutDownTime >= 3 * 8 + 10 + 5) {
			entity.remove()
		}
	}

	onTick {
		// reset if there are no entities
		if (mapComponent.entities().isNotEmpty()) return@onTick
		map.state = MapState()
	}

	val mapCloseComponent = CustomEntityComponent.fromString("map_close")
	mapCloseComponent.onTick {
		map.state.shutDownTime += 1
	}



	registerEditObjectCommand(
		command = requireCommand("map_settings"),
		objectProvider = { map },
		defaultObject = { Map() },
//		onChange = {
//			it.state.shaderTransition = .0
//		},
		sendMessage = { sender, message ->
			sender.sendActionBarOrMessage(message)
		},
	)

//	requireCommand("map_presets").apply {
//
//		val presets = mapOf(
//			"reset" to { map = Map() },
//			"earth" to ::presetEarth,
//			"hologram" to ::presetHologram,
//			"basketball" to ::presetBasketballPlanet,
//		)
//
//		setExecutor { sender, _, _, args ->
//			if (args.isEmpty()) {
//				sender.sendMessage("Specify a preset /${name} <preset>")
//				return@setExecutor true
//			}
//
//			val preset = args[0]
//			val action = presets[preset]
//
//			if (action == null) {
//				sender.sendMessage("Invalid preset: \"$preset\"")
//				return@setExecutor true
//			}
//
//			action(map)
//
//			sender.sendActionBarOrMessage("Applied preset \"$preset\"")
//			true
//		}
//
//		setTabCompleter { _, _, _, args ->
//			if (args.size == 1) {
//				presets.keys.filter { it.startsWith(args[0], true) }
//			} else {
//				emptyList()
//			}
//		}
//	}
}