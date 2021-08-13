package com.baka3k.architecture.device.camera

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.device.CameraService
import kotlinx.coroutines.flow.Flow

class CameraServiceImpl : CameraService {
    override suspend fun scanQRCode(): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

}