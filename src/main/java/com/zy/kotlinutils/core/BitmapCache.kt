package com.zy.kotlinutils.core

import android.graphics.Bitmap
import com.zy.kotlinutils.core.cache.SoftCache

/**
 * Created by zy on 17-12-17.
 */
class BitmapCache : SoftCache<String, Bitmap> {
    constructor() : super()
    constructor(initialSize: Int) : super(initialSize)

    override fun onClear(value: Bitmap) {
        if (!value.isRecycled) {
            value.recycle()
        }
    }
}