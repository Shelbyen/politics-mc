package funn.j2k.politicsMc.portal_feature

import funn.j2k.politicsMc.common_utilities.events.onPortalCreate
import funn.j2k.politicsMc.common_utilities.events.onPortalSpawn
import funn.j2k.politicsMc.gui.utilities.currentPlugin
import org.bukkit.entity.Player


fun setupPortals() {
	val regions = mutableListOf<Region>()
	val config = currentPlugin.config

	val debugMode = config.getBoolean("debug")

	regions.clear()

	val section = config.getConfigurationSection("ruins") ?: return
	for (key in section.getKeys(false)) {
		regions += Region.load(section.getConfigurationSection(key)!!)
	}

	fun addRegion(region: Region) {
		val path = "ruins.${regions.size}"
		config.createSection(path)
		region.save(config, path)
		currentPlugin.saveConfig()

		regions += region
	}

	onPortalSpawn { event ->
		val box = event.boundingBox
		val world = event.world.name

		val region = Region(
			world,
			box.minX.toInt(), box.maxX.toInt(),
			box.minY.toInt(), box.maxY.toInt(),
			box.minZ.toInt(), box.maxZ.toInt()
		)
		addRegion(region)

		if (debugMode) {
			currentPlugin.logger.info("Registered ruined portal: $region")
		}
	}

	onPortalCreate { event ->
		var minX = Int.MAX_VALUE
		var minY = Int.MAX_VALUE
		var minZ = Int.MAX_VALUE
		var maxX = Int.MIN_VALUE
		var maxY = Int.MIN_VALUE
		var maxZ = Int.MIN_VALUE

		for (state in event.blocks) {
			val loc = state.location
			minX = minOf(minX, loc.blockX)
			maxX = maxOf(maxX, loc.blockX)
			minY = minOf(minY, loc.blockY)
			maxY = maxOf(maxY, loc.blockY)
			minZ = minOf(minZ, loc.blockZ)
			maxZ = maxOf(maxZ, loc.blockZ)
		}
		val world = event.world.name
		val allowed = regions.any {
			it.isInside(world, minX, maxX, minY, maxY, minZ, maxZ)
		}

		if (!allowed) {
			event.isCancelled = true

			(event.entity as? Player)
				?.sendMessage("§cПортал можно строить только на месте разрушенных!")

			if (debugMode) {
				currentPlugin.logger.info("Portal blocked: $world ($minX $minY $minZ)")
			}
		}
	}
}
