package com.baka3k.architecture.clean.base.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

open abstract class BaseViewModel<S : Any> : ViewModel() {
    val store by lazy {
        ViewStateStore(this.initState())
    }
    val currentState: S
        get() = store.state

    private val _errorLiveEvent: MutableLiveData<Throwable> = MutableLiveData<Throwable>()

    val errorLiveEvent: LiveData<Throwable>
        get() = _errorLiveEvent

    protected val handlerException = CoroutineExceptionHandler { _, exception ->
        Log.e("BaseViewModel", "CoroutineExceptionHandler got $exception")
        dispatchError(exception)
    }

    protected fun dispatchError(error: Throwable) {
        _errorLiveEvent.value = error
    }

    protected fun dispatchState(state: S) {
        store.dispatchState(state = state)
    }
    abstract fun initState(): S
    override fun onCleared() {
        Log.d("test","onCleared")
        super.onCleared()
    }
}
