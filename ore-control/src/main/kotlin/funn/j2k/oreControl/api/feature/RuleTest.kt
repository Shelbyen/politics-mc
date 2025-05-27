package funn.j2k.oreControl.api.feature

import funn.j2k.oreControl.api.util.LocatedAble
import funn.j2k.oreControl.api.util.SaveAble

interface RuleTest : LocatedAble, SaveAble, Cloneable {
    val type: RuleTestType?

    public override fun clone(): RuleTest
}
