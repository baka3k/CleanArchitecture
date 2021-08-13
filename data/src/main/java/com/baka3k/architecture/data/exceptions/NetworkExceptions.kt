package com.baka3k.architecture.data.exceptions

import java.io.IOException

class NetworkExceptions(private val errCode: Int) : IOException() {
}