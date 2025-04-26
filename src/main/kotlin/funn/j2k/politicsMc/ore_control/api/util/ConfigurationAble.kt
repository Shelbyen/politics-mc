package funn.j2k.politicsMc.ore_control.api.util

import funn.j2k.politicsMc.ore_control.api.feature.Configuration
import org.bukkit.Keyed

/**
 * Represents an object which can have a [Configuration].
 */
interface ConfigurationAble : Keyed {
    /**
     * Returns an unmodifiable set containing all
     * [settings][Setting] this object will use.
     *
     * @return the settings which will be used.
     */
    val settings: Set<Any?>

    /**
     * Creates and returns a new empty configuration,
     * for this object.
     *
     * @return a new empty configuration
     */
    fun createEmptyConfiguration(): Configuration
}
