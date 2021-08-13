/*
 * Created by Le Quang Hiep on 4/22/20 12:58 PM.
 * Copyright © 2020 Baka3k@gmail.com. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details
 */

package com.baka3k.architecture.device.barcode.decoder.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

class Display(context: Context) {
    private val context: Context by lazy { context.applicationContext }
    private val windowManager: WindowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    init {
        val displayMetrics = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
    }

    fun size(): IntArray {
        val displayMetrics = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return intArrayOf(width, height)
    }

    fun getWidth(): Int {
        val displayMetrics = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getHeight(): Int {
        val displayMetrics = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getScreenResolution(): Point? {
        val display = windowManager.defaultDisplay
        val screenResolution = Point()
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(screenResolution)
        } else {
            screenResolution[display.width] = display.height
        }
        return screenResolution
    }

    fun getScreenOrientation(): Int {
        val orientation: Int
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        orientation = if (width <= height) {
            Configuration.ORIENTATION_PORTRAIT
        } else {
            Configuration.ORIENTATION_LANDSCAPE
        }
        return orientation
    }
}