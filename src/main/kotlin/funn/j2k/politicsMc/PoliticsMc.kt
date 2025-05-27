package funn.j2k.politicsMc

import funn.j2k.politicsMc.custom_map.maps
import funn.j2k.politicsMc.custom_map.setupCustomMap
import funn.j2k.politicsMc.custom_map.utilities.closeCurrentPlugin
import funn.j2k.politicsMc.custom_map.utilities.currentPlugin
import funn.j2k.politicsMc.custom_map.utilities.openCustomItemInventory
import org.bukkit.Bukkit
import org.bukkit.World
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
        val world: World? = Bukkit.getWorld("world")

    }

    override fun onDisable() {
        maps.clear()
        closeCurrentPlugin()
    }
}
