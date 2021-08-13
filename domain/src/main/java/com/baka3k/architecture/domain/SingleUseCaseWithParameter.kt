package com.baka3k.architecture.domain

public interface SingleUseCaseWithParameter<P, R> {
    suspend fun execute(parameter: P): R
}