package com.baka3k.architecture.domain.device

import com.baka3k.architecture.domain.Result
import kotlinx.coroutines.flow.Flow

interface CameraService {
    suspend fun scanQRCode(): Flow<Result<String>>
}