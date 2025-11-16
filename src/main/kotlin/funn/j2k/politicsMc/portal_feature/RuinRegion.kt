package funn.j2k.politicsMc.portal_feature


import org.bukkit.configuration.ConfigurationSection

data class Region(
	val world: String,
	val minX: Int,
	val maxX: Int,
	val minY: Int,
	val maxY: Int,
	val minZ: Int,
	val maxZ: Int
) {
	fun isInside(
		w: String,
		pMinX: Int, pMaxX: Int,
		pMinY: Int, pMaxY: Int,
		pMinZ: Int, pMaxZ: Int
	): Boolean {
		if (w != world) return false

		return !(pMaxX < minX || pMinX > maxX ||
				pMaxY < minY || pMinY > maxY ||
				pMaxZ < minZ || pMinZ > maxZ)
	}

	fun save(cfg: ConfigurationSection, path: String) {
		cfg.set("$path.world", world)
		cfg.set("$path.minX", minX)
		cfg.set("$path.maxX", maxX)
		cfg.set("$path.minY", minY)
		cfg.set("$path.maxY", maxY)
		cfg.set("$path.minZ", minZ)
		cfg.set("$path.maxZ", maxZ)
	}

	companion object {
		fun load(cfg: ConfigurationSection): Region =
			Region(
				cfg.getString("world")!!,
				cfg.getInt("minX"),
				cfg.getInt("maxX"),
				cfg.getInt("minY"),
				cfg.getInt("maxY"),
				cfg.getInt("minZ"),
				cfg.getInt("maxZ")
			)
	}
}
