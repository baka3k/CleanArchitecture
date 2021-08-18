package com.baka3k.architecture.device.camera

import com.baka3k.architecture.device.barcode.decoder.BarcodeAnalyzer
import com.baka3k.architecture.device.barcode.decoder.Decoder
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.device.CameraService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CameraServiceImpl(
    private val bakaCamera: BakaCamera,
    private val decoder: Decoder
) : CameraService {

    override suspend fun scanQRCode(): Flow<Result<String>> = callbackFlow<Result<String>> {
        try {
            val barcodeAnalyzer = BarcodeAnalyzer(decoder) {
                trySend(Result.Success(it))
                cancel()
            }
            bakaCamera.startPreview(barcodeAnalyzer)
        } catch (e: Exception) {
            channel.close()
            cancel()
        }
    }
}