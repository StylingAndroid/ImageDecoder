package com.stylingandroid.imagedecoder

import android.content.Context
import android.graphics.ImageDecoder
import kotlinx.android.synthetic.main.fragment_image_decoder.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class ScaleDrawableFragment : ImageDecoderFragment() {

    override val assetName: String = "StylingAndroid.png"

    override fun updateAsset(context: Context) {
        launch(CommonPool) {
            ImageDecoder.createSource(cacheAsset.file(assetName)).also { source ->
                ImageDecoder.decodeDrawable(source, listener).also { drawable ->
                    launch(UI) {
                        image.setImageDrawable(drawable)
                    }
                }
            }
        }
    }

    private val listener = ImageDecoder.OnHeaderDecodedListener { imageDecoder, imageInfo, _ ->
        imageDecoder.setResize(imageInfo.size.width / 2, imageInfo.size.height / 2)
    }
}
