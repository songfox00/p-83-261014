package com.back.p67260811.global.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.AllArgsConstructor
import lombok.Getter

class RsData<T>(
    private val resultCode: String,
    private val msg: String,
    private val data: T? = null
) {

    constructor(resultCode: String, msg: String) : this(resultCode, msg, null)

    @get:JsonIgnore
    val statusCode: Int
        get() = resultCode.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt() // "201-1"

    fun getResultCode(): String = resultCode
    fun getMsg(): String = msg

}
