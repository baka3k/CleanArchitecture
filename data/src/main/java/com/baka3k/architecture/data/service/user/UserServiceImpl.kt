package com.baka3k.architecture.data.service.user

import com.baka3k.architecture.data.service.http.NoConnectivityException
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.model.User

class UserServiceImpl(private val mNetworkConnection: NetworkConnection) : UserService {
    override fun getUserInfor(): Result<User> {
        if (mNetworkConnection.netWorkStatus().hasInternetConnection()) {
            Thread.sleep(1000)
            // dummy data return data from server
            return Result.Success(User("test","test","19","0923423842"))
        } else {
            throw NoConnectivityException()
        }
    }

    override fun signIn(username: String, pass: String): Result<Boolean> {
        if (mNetworkConnection.netWorkStatus().hasInternetConnection()) {
            Thread.sleep(1000)
            return Result.Success(true)
        } else {
            throw NoConnectivityException()
        }
    }
}