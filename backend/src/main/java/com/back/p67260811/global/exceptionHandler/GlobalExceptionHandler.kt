package com.back.p67260811.global.exceptionHandler

import com.back.p67260811.global.dto.RsData
import com.back.p67260811.global.exception.ServiceException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseBody
    fun handleNoSuchElementException() = RsData<Void?>(
        "404-1",
        "존재하지 않는 데이터입니다.",
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
    ): RsData<Void?> {
        val message = e.bindingResult.allErrors
            .filterIsInstance<FieldError>()
            .map { "${it.field}-${it.code}-${it.defaultMessage}" }
            .sorted()
            .joinToString("\n")

        return RsData(
            "400-1",
            message,
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseBody
    fun handleHttpMessageNotReadableException() = RsData<Void?>(
        "400-2",
        "잘못된 형식의 요청 데이터입니다.",
    )

    @ExceptionHandler(ServiceException::class)
    @ResponseBody
    fun handleServiceException(e: ServiceException) = RsData<Void?>(
        e.resultCode,
        e.msg,
    )
}