package funn.j2k.politicsMc

import funn.j2k.politicsMc.gui.maps.setupMap
import funn.j2k.politicsMc.gui.utilities.currentPlugin
import funn.j2k.politicsMc.gui.utilities.setupCoreUtils
import funn.j2k.politicsMc.gui.utilities.shutdownCoreUtils
import funn.j2k.politicsMc.portal_feature.setupPortals
import org.bukkit.plugin.java.JavaPlugin


class PoliticsMc : JavaPlugin() {
	override fun onEnable() {
		currentPlugin = this
		setupCoreUtils()
		setupMap()
		saveDefaultConfig()
		setupPortals()
	}

	override fun onDisable() {
		shutdownCoreUtils()
	}
}
