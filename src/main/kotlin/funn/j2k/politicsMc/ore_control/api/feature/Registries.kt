package funn.j2k.politicsMc.ore_control.api.feature

import funn.j2k.politicsMc.ore_control.api.util.ValueType

class Registries {
    val featureRegistry: Registry<Feature> = Registry()
    val featureGeneratorRegistry: Registry<FeatureGenerator<*>> = Registry()
    val placementModifierRegistry: Registry<FeaturePlacementModifier<*>> = Registry()
    private val valueTypeRegistry: MutableMap<Class<*>, Registry<*>> = LinkedHashMap<Class<*>, Registry<*>>()
    val ruleTestTypeRegistry: Registry<RuleTestType> = Registry()

    fun <O : ValueType<*, O, *>?> getValueTypeRegistry(clazz: Class<O>): Registry<O> {
        return valueTypeRegistry.computeIfAbsent(clazz) { Registry<O>() } as Registry<O>
    }
}
