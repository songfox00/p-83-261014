package com.back.p67260811.global.exception

import com.back.p67260811.global.dto.RsData

class ServiceException(
    val resultCode: String,
    val msg: String
) : RuntimeException(msg) {
    val rsData: RsData<*>

    init {
        rsData = RsData(resultCode, msg, null)
    }
}
