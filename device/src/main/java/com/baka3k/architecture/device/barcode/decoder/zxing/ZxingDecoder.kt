/*
 * Created by Le Quang Hiep on 4/22/20 1:10 PM.
 * Copyright © 2020 Baka3k@gmail.com. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details
 */

package com.baka3k.architecture.device.barcode.decoder.zxing

import android.content.res.Configuration
import com.baka3k.architecture.device.barcode.decoder.utils.Display
import com.baka3k.architecture.device.barcode.decoder.Decoder
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.util.*

class ZxingDecoder(private val display: Display) : Decoder {
    companion object {
        private val ALL_FORMATS = arrayListOf(
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.EAN_13,
            BarcodeFormat.EAN_8,
            BarcodeFormat.RSS_14,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.ITF,
            BarcodeFormat.CODABAR,
            BarcodeFormat.QR_CODE,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.PDF_417
        )
    }

    private var mMultiFormatReader: MultiFormatReader? = null

    init {
        initMultiFormatReader()
    }

    private fun initMultiFormatReader() {
        val hints: MutableMap<DecodeHintType, Any> =
            EnumMap(
                DecodeHintType::class.java
            )
        hints[DecodeHintType.POSSIBLE_FORMATS] = ALL_FORMATS
        mMultiFormatReader = MultiFormatReader()
        mMultiFormatReader?.setHints(hints)
    }

    override fun decode(
        widthImage: Int,
        heightImage: Int,
        inputData: ByteArray
    ): String {
        var width = widthImage
        var height = heightImage
        var data = inputData
        val datalength = data.size
        if (width > 10 && height > 10) {
            if (display.getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                val rotatedData = ByteArray(datalength)
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val rotatedDataIndex = x * height + height - y - 1
                        val dataIndext = x + y * width
                        if (rotatedDataIndex < datalength && dataIndext < datalength) {
                            rotatedData[rotatedDataIndex] = data[dataIndext]
                        }
                    }
                }
                val tmp = width
                width = height
                height = tmp
                data = rotatedData
            }
            var rawResult: Result? = null
            val source =
                PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false)
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                rawResult = mMultiFormatReader?.decodeWithState(bitmap)
            } catch (re: ReaderException) {
                // continue
            } catch (npe: NullPointerException) {
                // This is terrible
            } catch (aoe: ArrayIndexOutOfBoundsException) {
            } finally {
                mMultiFormatReader?.reset()
            }
            if (rawResult != null) {
                return rawResult.text
            }
        }
        return ""
    }
}