package com.baka3k.architecture.data.repository

import com.baka3k.architecture.data.service.user.UserService
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.model.User
import com.baka3k.architecture.domain.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val dataSource: UserService,
    private val dispatcher: CoroutineDispatcher
) : AccountRepository {
    override suspend fun login(username: String, password: String): Result<Boolean> =
        withContext(dispatcher) {
            dataSource.signIn(username, password)
        }

    override suspend fun getUserInfor(): Result<User> =
        withContext(dispatcher) {
            dataSource.getUserInfor()
        }
}