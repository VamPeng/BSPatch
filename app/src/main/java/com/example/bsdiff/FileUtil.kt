package com.example.bsdiff

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

object FileUtil {

    fun getPatchFile(context: Context, func: (File) -> Unit) {
        var ins = context.assets.open("patch.diff")
        val bytes = ByteArray(1024)

        var file = File(context.cacheDir, "gif")
        if (!file.exists()) {
            file.mkdir()
        }

        val patchFile = File(file, "patch.diff")
        if (patchFile.exists()) {
            patchFile.delete()
        }
        patchFile.createNewFile()
        val ops = FileOutputStream(patchFile)
        var length = 0
        Executors.newSingleThreadExecutor().execute {
            ins.use {
                ops.use {
                    while (ins.read(bytes).also { length = it } != -1) {
                        ops.write(bytes, 0, length)
                    }
                }
                func.invoke(patchFile)
            }
        }
    }

}