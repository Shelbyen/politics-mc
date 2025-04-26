package funn.j2k.politicsMc.ore_control.api.feature

import funn.j2k.politicsMc.ore_control.api.util.Parser
import org.bukkit.Keyed

interface RuleTestType : Keyed {
    val parser: Parser<RuleTest?>
}
