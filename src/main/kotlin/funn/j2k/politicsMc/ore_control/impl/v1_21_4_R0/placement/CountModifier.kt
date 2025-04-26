package funn.j2k.politicsMc.ore_control.impl.v1_21_4_R0.placement

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import funn.j2k.politicsMc.ore_control.api.feature.Registries
import net.minecraft.world.level.levelgen.placement.CountPlacement
import org.bukkit.NamespacedKey
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import org.bukkit.util.BlockVector
import java.util.*


class CountModifier(registries: Registries) :
    MinecraftPlacementModifier<CountPlacement?, CountModifierConfiguration?>(registries, "count") {
    override fun mergeConfig(
        first: CountModifierConfiguration,
        second: CountModifierConfiguration
    ): CountModifierConfiguration {
        return CountModifierConfiguration(
            this,
            if (first.getCount() != null) first.getCount() else second.getCount()
        )
    }

    override fun createParser(registries: Registries): Parser<CountModifierConfiguration> {
        return object : Parser() {
            override fun toJson(value: CountModifierConfiguration): JsonElement {
                val jsonObject = JsonObject()
                if (value.getCount() != null) {
                    val entry: JsonObject =
                        value.getCount().getValueType().getParser().toJson(value.getCount()).getAsJsonObject()
                    entry.addProperty("count_type", value.getCount().getValueType().getKey().toString())
                    jsonObject.add("count", entry)
                }
                return jsonObject
            }

            override fun fromJson(jsonElement: JsonElement): CountModifierConfiguration {
                val jsonObject = jsonElement.asJsonObject

                var count: IntegerValue? = null
                if (jsonObject.has("count")) {
                    val entry = jsonObject.getAsJsonObject("count")
                    count = registries.getValueTypeRegistry(IntegerType::class.java)
                        .get(NamespacedKey.fromString(entry.getAsJsonPrimitive("count_type").asString)).get()
                        .getParser().fromJson(entry)
                }

                return CountModifierConfiguration(this@CountModifier, count)
            }
        }
    }

    override fun createPlacementModifier(
        worldInfo: WorldInfo,
        random: Random,
        position: BlockVector,
        limitedRegion: LimitedRegion,
        configuration: CountModifierConfiguration
    ): CountPlacement {
        val count = if (configuration.getCount() != null) {
            configuration.getCount().getValue(worldInfo, random, position, limitedRegion)
        } else {
            0
        }

        return CountPlacement.of(count)
    }

    override val settings: Set<Any>
        get() = CountModifierConfiguration.SETTINGS

    override fun createEmptyConfiguration(): CountModifierConfiguration {
        return CountModifierConfiguration(this, null)
    }
}
