package edu.dio.credit.app.system.dto.Customer

import edu.dio.credit.app.system.Model.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDTO(
    @field: NotEmpty(message = "Invalid input") val firstName: String,
    @field: NotEmpty(message = "Invalid input") val lastName: String,
    @field: NotNull(message = "Invalid input") val income: BigDecimal,
    @field: NotEmpty(message = "Invalid zip code") val zipCode: String,
    @field: NotEmpty(message = "Invalid input") val street: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street

        return customer
    }
}