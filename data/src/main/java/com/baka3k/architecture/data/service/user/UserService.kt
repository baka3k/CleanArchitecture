package com.baka3k.architecture.data.service.user

import androidx.annotation.WorkerThread
import com.baka3k.architecture.data.exceptions.ResponseExceptions
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.model.User

interface UserService {
    @WorkerThread
    @Throws(
        ResponseExceptions::class
    )
    fun signIn(username: String, pass: String): Result<Boolean>

    fun getUserInfor(): Result<User>
}