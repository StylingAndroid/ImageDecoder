package com.stylingandroid.imagedecoder

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ImageDecoderFragment : Fragment() {

    protected abstract val assetName: String

    protected val cacheAsset: CacheAsset by lazy {
        CacheAsset(context as Context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_image_decoder, container, false)

    protected abstract fun updateAsset(context: Context)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateAsset(context)
    }
}
