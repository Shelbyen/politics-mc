package funn.j2k.oreControl.api.feature

interface PlacementModifierConfiguration : Configuration {
    /**
     * Returns the owner of this configuration.
     *
     * @return the owner of this Configuration.
     */
    override val owner: FeaturePlacementModifier<*>
}
