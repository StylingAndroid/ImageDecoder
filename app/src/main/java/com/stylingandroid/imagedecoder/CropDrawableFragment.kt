package com.stylingandroid.imagedecoder

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.Rect
import kotlinx.android.synthetic.main.fragment_image_decoder.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

private const val HALF_SIZE = 200

class CropDrawableFragment : ImageDecoderFragment() {

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
        imageDecoder.setCrop(
                createCropRect(imageInfo.size.width / 2, imageInfo.size.height / 2)
        )
    }

    private fun createCropRect(centreX: Int, centreY: Int) : Rect =
            Rect(
                    centreX - HALF_SIZE,
                    centreY - HALF_SIZE,
                    centreX + HALF_SIZE,
                    centreY + HALF_SIZE
            )
}
