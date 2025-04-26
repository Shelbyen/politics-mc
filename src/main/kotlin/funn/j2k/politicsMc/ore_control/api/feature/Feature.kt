package funn.j2k.politicsMc.ore_control.api.feature

import org.bukkit.Keyed
import org.bukkit.NamespacedKey

/**
 * Represents a Feature which can generate in a world.
 * Each Feature contains one generator and can have multiple placement modifiers.
 * The order of the placement modifiers is important. A list which preserves its order should be used.
 *
 * @param key                The unique key of this feature.
 * @param generator          The generator which generates the blocks.
 * @param placementModifiers The placement modifiers which determine the positions to generate the feature.
 */
class Feature(
    val key: NamespacedKey, val generator: FeatureGenerator<*>,
    val placementModifiers: List<FeaturePlacementModifier<*>>
) : Keyed {
    override fun getKey(): NamespacedKey {
        return key
    }

}
