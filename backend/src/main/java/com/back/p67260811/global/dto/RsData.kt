package com.back.p67260811.global.dto

import com.fasterxml.jackson.annotation.JsonIgnore

class RsData<T>(
    val resultCode: String,
    val msg: String,
    val data: T? = null
) {

    constructor(resultCode: String, msg: String) : this(resultCode, msg, null)

    @get:JsonIgnore
    val statusCode: Int
        get() = resultCode.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt() // "201-1"

}
