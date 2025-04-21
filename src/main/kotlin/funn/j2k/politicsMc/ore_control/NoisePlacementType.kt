package funn.j2k.politicsMc.ore_control

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.levelgen.placement.PlacementModifierType

object NoisePlacementType : PlacementModifierType<NoisePlacement> {

    private val CODEC: MapCodec<NoisePlacement> = RecordCodecBuilder.mapCodec { instance ->
        instance.group(
            Codec.STRING.fieldOf("seed").forGetter(NoisePlacement::seed),
            Codec.INT.fieldOf("octaves").forGetter(NoisePlacement::octaves),
            Codec.DOUBLE.fieldOf("frequency").forGetter(NoisePlacement::frequency),
            Codec.DOUBLE.fieldOf("amplitude").forGetter(NoisePlacement::amplitude)
        ).apply(instance, ::NoisePlacement)
    }

    override fun codec(): MapCodec<NoisePlacement> = CODEC
}
