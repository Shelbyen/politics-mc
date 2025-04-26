package funn.j2k.politicsMc.ore_control.api.feature

/**
 * Represents a configuration which has a [FeatureGenerator] as owner.
 */
interface FeatureGeneratorConfiguration : Configuration {
    /**
     * Returns the owner of this configuration.
     *
     * @return the owner of this Configuration.
     */
    override val owner: FeatureGenerator<*>
}
