package edu.dio.credit.app.system.Exception

data class BusinessException(
    override val message: String?
): RuntimeException(message) {

}