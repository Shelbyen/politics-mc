package funn.j2k.politicsMcAddon

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import xyz.xenondevs.nova.addon.Addon
import xyz.xenondevs.nova.context.Context
import xyz.xenondevs.nova.context.intention.DefaultContextIntentions
import xyz.xenondevs.nova.context.param.DefaultContextParamTypes
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitFun
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.util.BlockUtils
import xyz.xenondevs.nova.world.BlockPos

@Init(stage = InitStage.PRE_PACK)
object PoliticsMcAddon : Addon() {
    @InitFun
    private fun init() {
        logger.info("Started!")

    }
}
