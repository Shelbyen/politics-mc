package funn.j2k.politicsMc

import funn.j2k.politicsMc.custom_map.maps
import funn.j2k.politicsMc.custom_map.setupCustomMap
import funn.j2k.politicsMc.utilities.closeCurrentPlugin
import funn.j2k.politicsMc.utilities.currentPlugin
import funn.j2k.politicsMc.utilities.openCustomItemInventory
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class PoliticsMc : JavaPlugin() {

    override fun onEnable() {
        currentPlugin = this
        setupCustomMap()

        getCommand("maps")?.setExecutor { sender, _, _, _ ->
            openCustomItemInventory(sender as? Player ?: run {
                sender.sendMessage("Only players can use this command")
                return@setExecutor true
            })
            true
        }
    }

    override fun onDisable() {
        maps.clear()
        closeCurrentPlugin()
    }
}
