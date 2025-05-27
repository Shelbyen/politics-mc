package funn.j2k.politicsMcAddon

import xyz.xenondevs.nova.addon.Addon
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitFun
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.world.generation.ExperimentalWorldGen

@Init(stage = InitStage.PRE_PACK)
object PoliticsMcAddon : Addon() {
    @InitFun
    private fun init() {
        logger.info("Started!")
    }
}
