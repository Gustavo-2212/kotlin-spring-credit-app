package edu.dio.credit.app.system.Exception

import java.time.LocalDateTime

data class ExceptionDetails(
    val title: String,
    val timestamp: LocalDateTime,
    val exception: String,
    val status: Int,
    val details: MutableMap<String, String?>
) {

}