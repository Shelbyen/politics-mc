package funn.j2k.politicsMc

import funn.j2k.politicsMc.gui.maps.setupMap
import funn.j2k.politicsMc.gui.utilities.currentPlugin
import funn.j2k.politicsMc.gui.utilities.setupCoreUtils
import funn.j2k.politicsMc.gui.utilities.shutdownCoreUtils
import org.bukkit.plugin.java.JavaPlugin


class PoliticsMc : JavaPlugin() {

    override fun onEnable() {
        currentPlugin = this
	    setupCoreUtils()
	    setupMap()
    }

    override fun onDisable() {
	    shutdownCoreUtils()
    }
}
