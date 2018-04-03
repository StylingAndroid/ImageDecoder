package com.stylingandroid.imagedecoder

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ImageDecoderPagerAdapter(
        private val context: Context,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    private val items = arrayOf(
            Item("Logo", SimpleBitmapFragment::class.java),
            Item("Animated", AnimatedDrawableFragment::class.java)
    )

    override fun getItem(position: Int): ImageDecoderFragment =
            items[position].let { item ->
                createFragment(item.fragmentClass)
            }

    private fun createFragment(cls: Class<out ImageDecoderFragment>) : ImageDecoderFragment =
            Fragment.instantiate(context, cls.name) as ImageDecoderFragment

    override fun getCount(): Int = items.size

    override fun getPageTitle(position: Int): CharSequence? {
        return items[position].title
    }

    private data class Item(
            val title: String,
            val fragmentClass: Class<out ImageDecoderFragment>
    )
}
