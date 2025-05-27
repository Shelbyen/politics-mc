package funn.j2k.oreControl.api.feature

import funn.j2k.oreControl.api.util.Parser
import org.bukkit.Keyed

interface RuleTestType : Keyed {
    val parser: Parser<RuleTest?>
}
