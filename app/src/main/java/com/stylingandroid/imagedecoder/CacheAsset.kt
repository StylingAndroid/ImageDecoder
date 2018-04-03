package com.stylingandroid.imagedecoder

import android.content.Context
import android.content.res.AssetManager
import java.io.File
import java.io.FileOutputStream

class CacheAsset(
        context: Context,
        private val cacheDir: File = context.cacheDir,
        private val assetManager: AssetManager = context.assets
) {

    fun file(assetName: String): File =
            File(cacheDir, assetName).apply {
                if (!exists()) {
                    createCache(assetName, this)
                }
            }

    private fun createCache(assetName: String, output: File) {
        val buffer = ByteArray(1024)
        assetManager.open(assetName).use { inputStream ->
            FileOutputStream(output).use { outputStream ->
                var length = inputStream.read(buffer)
                while (length > 0) {
                    outputStream.write(buffer, 0, length)
                    length = inputStream.read(buffer)
                }
            }
        }
    }
}
