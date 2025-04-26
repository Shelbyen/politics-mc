package funn.j2k.politicsMc.ore_control.api.util

interface LocatedAble {
    /**
     * The value location were the value is from / is being saved.
     * The value is only present during runtime and is not saved to disk.
     * Therefor it should be set once the value is loaded / set.
     * When the value is saved to a different value location, then the value
     * should be updated accordingly.
     *
     * @return from were the value is, if not location is set [ValueLocation.UNKNOWN] is returned
     * @see .setValueLocation
     */
    /**
     * The value location were the value is from / is being saved.
     * The value is only present during runtime and is not saved to disk.
     * Therefor it should be set once the value is loaded / set.
     * When the value is saved to a different value location, then the value
     * should be updated accordingly.
     * <br></br>
     * When setting a value location it gets passed to all child values.
     *
     * @param valueLocation to set
     * @see .getValueLocation
     */
    var valueLocation: ValueLocation
}
