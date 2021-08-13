/*
 * Created by Le Quang Hiep on 4/22/20 1:27 PM.
 * Copyright © 2020 Baka3k@gmail.com. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details
 */

package com.baka3k.architecture.device.barcode.decoder

interface Decoder {
    fun decode(
        width: Int,
        height: Int,
        inputData: ByteArray
    ): String
}