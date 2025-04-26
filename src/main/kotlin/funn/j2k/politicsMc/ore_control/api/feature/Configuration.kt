package funn.j2k.politicsMc.ore_control.api.feature

import funn.j2k.politicsMc.ore_control.api.util.ConfigurationAble
import funn.j2k.politicsMc.ore_control.api.util.SaveAble


/**
 * Holds values for features.
 */
interface Configuration : SaveAble {
    /**
     * Returns the owner of this configuration.
     *
     * @return the owner of this Configuration.
     */
    val owner: ConfigurationAble

    /**
     * Returns an unmodifiable set containing the allowed
     * [settings][Setting] for this configuration.
     *
     * @return the allowed settings.
     */
    val settings: Set<Any?>

    /**
     * Returns the value associated with the given setting.
     * If this configuration allows but does not have a value set,
     * it will return null.
     * <br></br>
     * If this configuration does not allow the given setting,
     * an exception will be thrown. Use [.getSettings]
     * for a set of allowed settings.
     *
     * @param setting The setting to get the value from.
     * @return the value associated with the setting.
     * @throws IllegalArgumentException if the given setting is not allowed in this configuration.
     */
    fun getValue(setting: Setting): Value<*, *, *>?

    /**
     * Sets the value for the given setting to the given value.
     * An exception is thrown when the given setting is not allowed by
     * this configuration or when the given value is not of the right type.
     * Use [.getSettings] for a set of allowed settings.
     *
     * @param setting The setting which should get the new value.
     * @param value   The new value for the given setting.
     * @throws IllegalArgumentException if the given setting is not allowed in this configuration.
     * @throws IllegalArgumentException if the given value is not the right type for the setting.
     */
    fun setValue(setting: Setting, value: Value<*, *, *>?)
}
