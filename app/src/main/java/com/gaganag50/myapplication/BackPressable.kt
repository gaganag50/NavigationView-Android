package com.gaganag50.myapplication

/**
 * Indicates that the current fragment can handle back presses
 */

interface BackPressable {
    /**
     * A back press was delegated to this fragment
     *
     * @return if the back press was handled
     */
    abstract fun onBackPressed(): Boolean

}
