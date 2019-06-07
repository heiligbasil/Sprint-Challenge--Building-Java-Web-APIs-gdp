package com.lambdaschool.sprint12.handler

import com.lambdaschool.sprint12.exception.ResourceNotFoundException
import com.lambdaschool.sprint12.model.ErrorDetail
import org.slf4j.LoggerFactory
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler()
{
    companion object
    {
        private val loggerFactory = LoggerFactory.getLogger(GdpController::class.java)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(rnfe: ResourceNotFoundException, request: HttpServletRequest): ResponseEntity<*>
    {
        val messageLog: String = "$request on ${LocalDateTime.now()}"
        loggerFactory.info(messageLog)

        val errorDetail = ErrorDetail()
        errorDetail.setTimestamp(Date().time)
        errorDetail.status = HttpStatus.NOT_FOUND.value()
        errorDetail.title = "Resource Not Found"
        errorDetail.detail = rnfe.message
        errorDetail.developerMessage = rnfe.javaClass.name

        return ResponseEntity<Any>(errorDetail, null, HttpStatus.NOT_FOUND)
    }

    override fun handleTypeMismatch(ex: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any>
    {
        val messageLog: String = "$status on ${LocalDateTime.now()}"
        loggerFactory.info(messageLog)

        val errorDetail = ErrorDetail()
        errorDetail.setTimestamp(Date().time)
        errorDetail.status = HttpStatus.BAD_REQUEST.value()
        errorDetail.title = ex.propertyName
        errorDetail.detail = ex.message
        errorDetail.developerMessage = request.getDescription(true)

        return ResponseEntity(errorDetail, null, HttpStatus.NOT_FOUND)
    }

    override fun handleNoHandlerFoundException(ex: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any>
    {
        val messageLog: String = "$status on ${LocalDateTime.now()}"
        loggerFactory.info(messageLog)

        val errorDetail = ErrorDetail()
        errorDetail.setTimestamp(Date().time)
        errorDetail.status = HttpStatus.NOT_FOUND.value()
        errorDetail.title = ex.requestURL
        errorDetail.detail = request.getDescription(true)
        errorDetail.developerMessage = "Rest Handler Not Found (check for valid URI)"

        return ResponseEntity(errorDetail, null, HttpStatus.NOT_FOUND)
    }
}
