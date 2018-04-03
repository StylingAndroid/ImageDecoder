package com.stylingandroid.imagedecoder

import android.content.Context
import android.graphics.ImageDecoder
import kotlinx.android.synthetic.main.fragment_image_decoder.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class SimpleBitmapFragment : ImageDecoderFragment() {

    override val assetName: String = "StylingAndroid.png"

    override fun updateAsset(context: Context) {
        launch(CommonPool) {
            ImageDecoder.createSource(cacheAsset.file(assetName)).also { source ->
                ImageDecoder.decodeBitmap(source).also { bitmap ->
                    launch(UI) {
                        image.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}
