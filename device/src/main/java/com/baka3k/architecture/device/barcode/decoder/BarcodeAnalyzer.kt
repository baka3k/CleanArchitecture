/*
 * Created by Le Quang Hiep on 4/22/20 11:33 AM.
 * Copyright © 2020 Baka3k@gmail.com. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details
 */

package com.baka3k.architecture.device.barcode.decoder

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

typealias BarcodeListener = (content: String) -> Unit

class BarcodeAnalyzer(private val decoder: Decoder, private val listener: BarcodeListener) :
    ImageAnalysis.Analyzer {
    private val frameRateWindow = 8
    private val frameTimestamps = ArrayDeque<Long>(5)
    private val listeners = ArrayList<BarcodeListener>().apply { listener?.let { add(it) } }
    private var lastAnalyzedTimestamp = 0L
    var framesPerSecond: Double = -1.0
        private set

    fun onFrameAnalyzed(listener: BarcodeListener) = listeners.add(listener)
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {
        if (listeners.isEmpty()) {
            image.close()
            return
        }
        val currentTime = System.currentTimeMillis()
        frameTimestamps.push(currentTime)
        while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
        val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
        val timestampLast = frameTimestamps.peekLast() ?: currentTime
        framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0
        lastAnalyzedTimestamp = frameTimestamps.first
        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val content = decoder.decode(image.width, image.height, data)
        if (!content.isNullOrEmpty()) {
            listeners.forEach { it(content) }
        }
        image.close()
    }
}