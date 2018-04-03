package com.stylingandroid.imagedecoder

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.Animatable2
import kotlinx.android.synthetic.main.fragment_image_decoder.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AnimatedDrawableFragment : ImageDecoderFragment() {

    override val assetName: String = "animated.gif"

    override fun updateAsset(context: Context) {
        launch(CommonPool) {
            ImageDecoder.createSource(cacheAsset.file(assetName)).also { source ->
                ImageDecoder.decodeDrawable(source, listener).also { drawable ->
                    launch(UI) {
                        image.setImageDrawable(drawable)
                        if (drawable is Animatable2) {
                            drawable.start()
                        }
                    }
                }
            }
        }
    }

    private var listener = ImageDecoder.OnHeaderDecodedListener { _, info, _ ->
        info?.apply {
            println("Decoded Image: ${description()}")
        }
    }

    private fun ImageDecoder.ImageInfo.description(): String =
            "MIME type: $mimeType; size: $size; isAnimated: $isAnimated"
}
