package com.baka3k.architecture.domain


public interface SingleUseCase<T> {
    suspend fun execute():T
}