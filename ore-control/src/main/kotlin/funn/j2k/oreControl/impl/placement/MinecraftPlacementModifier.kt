package funn.j2k.oreControl.impl.placement

import funn.j2k.oreControl.api.feature.FeaturePlacementModifier
import funn.j2k.oreControl.api.feature.PlacementModifierConfiguration
import funn.j2k.oreControl.api.feature.Registries
import funn.j2k.oreControl.api.util.Parser
import net.minecraft.core.BlockPos
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementContext
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.generator.CraftLimitedRegion
import org.bukkit.craftbukkit.util.RandomSourceWrapper
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import org.bukkit.util.BlockVector
import java.util.*
import java.util.stream.Stream

abstract class MinecraftPlacementModifier<M : PlacementModifier?, C : PlacementModifierConfiguration?>(
    registries: Registries,
    name: String
) : FeaturePlacementModifier<C> {
    final override val parser: Parser<PlacementModifierConfiguration?> = createParser(registries) as Parser<PlacementModifierConfiguration?>
    val key: NamespacedKey = NamespacedKey.minecraft(name)

    abstract fun mergeConfig(first: C, second: C): C

    abstract fun createParser(registries: Registries?): Parser<C>?

    abstract fun createPlacementModifier(
        worldInfo: WorldInfo,
        random: Random,
        position: BlockVector,
        limitedRegion: LimitedRegion,
        configuration: C
    ): M

    override fun merge(first: PlacementModifierConfiguration, second: PlacementModifierConfiguration): C {
        return mergeConfig(first as C, second as C)
    }

    override fun getPositions(
        worldInfo: WorldInfo,
        random: Random,
        position: BlockVector,
        limitedRegion: LimitedRegion,
        configuration: C
    ): Stream<BlockVector?> {
        val level: WorldGenLevel = (limitedRegion as CraftLimitedRegion).handle
        val placementModifier = createPlacementModifier(worldInfo, random, position, limitedRegion, configuration)
        return placementModifier!!.getPositions(
            PlacementContext(
                level,
                level.minecraftWorld.getChunkSource().generator,
                Optional.empty<PlacedFeature>()
            ), RandomSourceWrapper(random), BlockPos(position.blockX, position.blockY, position.blockZ)
        ).map<BlockVector>
        { pos: BlockPos -> BlockVector(pos.x, pos.y, pos.z) }
    }
}
