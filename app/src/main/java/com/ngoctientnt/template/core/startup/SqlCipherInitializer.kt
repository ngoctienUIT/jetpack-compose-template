package com.ngoctientnt.template.core.startup

object SqlCipherInitializer {

    @Volatile
    private var loaded = false

    fun loadIfNeeded(isEncryptedBuild: Boolean) {
        if (!isEncryptedBuild || loaded) return
        synchronized(this) {
            if (!loaded) {
                System.loadLibrary("sqlcipher")
                loaded = true
            }
        }
    }
}
