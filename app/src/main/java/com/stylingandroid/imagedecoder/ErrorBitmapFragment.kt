package com.stylingandroid.imagedecoder

import android.content.Context
import android.graphics.ImageDecoder
import kotlinx.android.synthetic.main.fragment_image_decoder.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class ErrorBitmapFragment : ImageDecoderFragment() {

    override val assetName: String = "corrupt.png"

    override fun updateAsset(context: Context) {
        launch(CommonPool) {
            try {
                ImageDecoder.createSource(cacheAsset.file(assetName)).also { source ->
                    ImageDecoder.decodeBitmap(source, headerDecodedListener).also { bitmap ->
                        launch(UI) {
                            image.setImageBitmap(bitmap)
                        }
                    }
                }
            } catch (e: Exception) {
                println("Exception loading image: $e")
            }
        }
    }

    private var headerDecodedListener = ImageDecoder.OnHeaderDecodedListener { imageDecoder, _, _ ->
        imageDecoder.setOnPartialImageListener(partialImageListener)
    }

    private var partialImageListener = ImageDecoder.OnPartialImageListener { errorCode, _ ->
        println("An error occurred: $errorCode")
        true
    }
}
