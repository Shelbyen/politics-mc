package funn.j2k.oreControl.api.util

interface SaveAble {
    /**
     * Returns true if this object has unsaved changes applied.
     * Otherwise, it will return false.
     *
     * @return true if dirty otherwise false.
     */
    val isDirty: Boolean

    /**
     * Sets the state of the object to not dirty.
     */
    fun saved()
}
