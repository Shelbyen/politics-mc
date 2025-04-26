package funn.j2k.politicsMc.ore_control.api.feature

import funn.j2k.politicsMc.ore_control.api.util.ConfigurationAble
import funn.j2k.politicsMc.ore_control.api.util.Parser
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import org.bukkit.util.BlockVector
import java.util.*

/**
 * Represents a generator which generates blocks in a region.
 *
 * @param <C> The type if configuration this generator while use.
</C> */
interface FeatureGenerator<C : FeatureGeneratorConfiguration?> : ConfigurationAble {
    /**
     * Returns the parser which can be used to serialize
     * the configuration used for this feature generator.
     *
     * @return the parser to save the configuration
     */
    val parser: Parser<FeatureGeneratorConfiguration?>

    /**
     * Merges the second configuration into the first one.
     * This means the first configuration will be used primary and
     * the second one will only be used if the first one those not
     * have a setting.
     * <br></br>
     * While a new configuration is returned the values are not cloned.
     *
     * @param first  The main configuration to merge.
     * @param second The second configuration to merge.
     * @return a new configuration with the merged values.
     */
    fun merge(first: FeatureGeneratorConfiguration, second: FeatureGeneratorConfiguration): C

    /**
     * Generates the feature at the given position and region.
     * During generation the given random and configuration should be used.
     *
     * @param worldInfo     The information about the world.
     * @param random        The random which should be used.
     * @param position      The position where the feature should get generated.
     * @param limitedRegion The LimitedRegion to use to generate the feature.
     * @param configuration The configuration to use to generate the feature.
     */
    fun place(
        worldInfo: WorldInfo,
        random: Random,
        position: BlockVector,
        limitedRegion: LimitedRegion,
        configuration: C
    )
}
