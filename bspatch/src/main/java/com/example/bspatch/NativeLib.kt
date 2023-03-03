package com.example.bspatch

class NativeLib {

    /**
     * A native method that is implemented by the 'bspatch' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'bspatch' library on application startup.
        init {
            System.loadLibrary("bspatch")
        }
    }
}