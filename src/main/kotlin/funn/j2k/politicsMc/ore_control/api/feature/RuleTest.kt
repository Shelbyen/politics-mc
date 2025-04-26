package funn.j2k.politicsMc.ore_control.api.feature

import funn.j2k.politicsMc.ore_control.api.util.LocatedAble
import funn.j2k.politicsMc.ore_control.api.util.SaveAble

interface RuleTest : LocatedAble, SaveAble, Cloneable {
    val type: RuleTestType?

    public override fun clone(): RuleTest
}
