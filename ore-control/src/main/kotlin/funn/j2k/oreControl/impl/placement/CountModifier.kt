package funn.j2k.oreControl.impl.placement

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import funn.j2k.oreControl.api.feature.Registries
import net.minecraft.world.level.levelgen.placement.CountPlacement
import org.bukkit.NamespacedKey
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import org.bukkit.util.BlockVector
import java.util.*


class CountModifier(registries: Registries) :
    MinecraftPlacementModifier<CountPlacement?, CountModifierConfiguration?>(registries, "count") {
    public override fun mergeConfig(
        first: CountModifierConfiguration,
        second: CountModifierConfiguration
    ): CountModifierConfiguration? {
        return CountModifierConfiguration(
            this,
            if (first.getCount() != null) first.getCount() else second.getCount()
        )
    }

    public override fun createParser(registries: Registries): Parser<CountModifierConfiguration?>? {
        return object : Parser() {
            public override fun toJson(value: CountModifierConfiguration): JsonElement {
                val jsonObject = JsonObject()
                if (value.getCount() != null) {
                    val entry: JsonObject =
                        value.getCount().getValueType().getParser().toJson(value.getCount()).getAsJsonObject()
                    entry.addProperty("count_type", value.getCount().getValueType().getKey().toString())
                    jsonObject.add("count", entry)
                }
                return jsonObject
            }

            public override fun fromJson(jsonElement: JsonElement): CountModifierConfiguration? {
                val jsonObject = jsonElement.getAsJsonObject()

                var count: IntegerValue? = null
                if (jsonObject.has("count")) {
                    val entry = jsonObject.getAsJsonObject("count")
                    count = registries.getValueTypeRegistry(IntegerType::class.java)
                        .get(NamespacedKey.fromString(entry.getAsJsonPrimitive("count_type").getAsString())!!).get()
                        .getParser().fromJson(entry)
                }

                return CountModifierConfiguration(this@CountModifier, count)
            }
        }
    }

    public override fun createPlacementModifier(
        worldInfo: WorldInfo,
        random: Random,
        position: BlockVector,
        limitedRegion: LimitedRegion,
        configuration: CountModifierConfiguration
    ): CountPlacement {
        val count: Int
        if (configuration.getCount() != null) {
            count = configuration.getCount().getValue(worldInfo, random, position, limitedRegion)
        } else {
            count = 0
        }

        return CountPlacement.of(count)
    }

    val settings: MutableSet<Setting>
        get() = CountModifierConfiguration.SETTINGS

    public override fun createEmptyConfiguration(): CountModifierConfiguration {
        return CountModifierConfiguration(this, null)
    }
}
