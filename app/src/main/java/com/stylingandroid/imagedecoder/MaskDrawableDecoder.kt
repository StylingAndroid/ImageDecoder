package com.stylingandroid.imagedecoder

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.PostProcessor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_decoder.*
import kotlinx.android.synthetic.main.fragment_image_decoder_virginia.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MaskDrawableDecoder : ImageDecoderFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_image_decoder_virginia, container, false).apply {
        website?.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.virginiapoltrack.com")).also {
                activity?.startActivity(it)
            }
        }
    }

    override val assetName: String = "artoo.png"

    override fun updateAsset(context: Context) {
        launch(CommonPool) {
            ImageDecoder.createSource(cacheAsset.file(assetName)).also { source ->
                ImageDecoder.decodeBitmap(source, headerDecodedListener).also { bitmap ->
                    launch(UI) {
                        image.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private val headerDecodedListener = ImageDecoder.OnHeaderDecodedListener { imageDecoder, imageInfo, _ ->
        imageDecoder.setPostProcessor(MaskProcessor(imageInfo.size.width, imageInfo.size.height))
    }

    private inner class MaskProcessor(val width: Int, val height: Int) : PostProcessor {

        private val maskName: String = "feather.png"

        override fun onPostProcess(canvas: Canvas): Int {
            loadMask().also { mask ->
                canvas.drawBitmap(
                        mask,
                        Rect(0, 0, mask.width, mask.height),
                        Rect(0, 0, width, height),
                        maskPaint)
            }

            return PixelFormat.TRANSLUCENT
        }

        private fun loadMask(): Bitmap =
                ImageDecoder.createSource(cacheAsset.file(maskName)).let { maskSource ->
                    ImageDecoder.decodeBitmap(maskSource) { maskDecoder, _, _ ->
                        maskDecoder.setAsAlphaMask(true)
                    }
                }

        private val maskPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }
    }
}
