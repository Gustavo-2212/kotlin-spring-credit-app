package edu.dio.credit.app.system.dto.Credit

import edu.dio.credit.app.system.Model.Credit
import edu.dio.credit.app.system.Model.Customer
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    @field: NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field: FutureOrPresent val dayFirstInstallment: LocalDate,
    @field: Max(value = 48) val numberOfInstallments: Int,
    @field: NotNull(message = "Invalid input") val customerId: Long
) {
    fun toEntity(): Credit {
        return Credit(
            creditValue = this.creditValue,
            dayFirstInstallment = this.dayFirstInstallment,
            numberOfInstallments = this.numberOfInstallments,
            customer = Customer(id = this.customerId)
        )
    }
}