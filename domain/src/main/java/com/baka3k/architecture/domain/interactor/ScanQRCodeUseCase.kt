package com.baka3k.architecture.domain.interactor

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.SingleUseCase
import com.baka3k.architecture.domain.device.CameraService
import kotlinx.coroutines.flow.Flow

class ScanQRCodeUseCase(private val cameraService: CameraService) :
    SingleUseCase<Flow<Result<String>>> {
    override suspend fun execute(): Flow<Result<String>> {
        return cameraService.scanQRCode()
    }
}