package funn.j2k.politicsMc.ore_control

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.placement.PlacementContext
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.PlacementModifierType
import org.bukkit.util.noise.SimplexNoiseGenerator
import java.util.stream.Stream

class NoisePlacement(val seed: String, val octaves: Int, val frequency: Double, val amplitude: Double) :
    PlacementModifier() {
    private val noise = SimplexNoiseGenerator.getInstance()
    override fun getPositions(ctx: PlacementContext, random: RandomSource, pos: BlockPos): Stream<BlockPos> =
        Stream.generate { pos }.limit(noise.noise(pos.center.x, pos.center.y, frequency, amplitude).toLong() * 10)

    override fun type(): PlacementModifierType<*> = NoisePlacementType

    companion object {

        @JvmStatic
        fun of(seed: String, octaves: Int, frequency: Double, amplitude: Double) =
            NoisePlacement(seed, octaves, frequency, amplitude)

    }
}
