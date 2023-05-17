package edu.dio.credit.app.system.dto.Credit

import edu.dio.credit.app.system.Enum.Status
import edu.dio.credit.app.system.Model.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int,
    val status: Status,
    val emailCustomer: String?,
    val incomeCustomer: BigDecimal?
) {
    constructor(credit: Credit): this (
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income,
        numberOfInstallments = credit.numberOfInstallments
    )
}