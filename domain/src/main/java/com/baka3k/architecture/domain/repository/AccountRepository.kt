package com.baka3k.architecture.domain.repository

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.model.User

interface AccountRepository {
    suspend fun login(username: String, password: String): Result<Boolean>
    suspend fun getUserInfor(): Result<User>
}