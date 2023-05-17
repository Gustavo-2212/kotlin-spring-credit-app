package edu.dio.credit.app.system.dto.Customer

import edu.dio.credit.app.system.Model.Address
import edu.dio.credit.app.system.Model.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
    @field: NotEmpty(message = "Invalid input") val firstName: String,
    @field: NotEmpty(message = "Invalid input") val lastName: String,
    @field: NotNull(message = "Invalid input") val income: BigDecimal,

    @field: NotEmpty(message = "Invalid input")
    @field: CPF(message = "Invalid cpf")
    val cpf: String,

    @field: NotEmpty(message = "Invalid zip code") val zipCode: String,

    @field: NotEmpty(message = "Invalid input")
    @field: Email(message = "Invalid email")
    val email: String,

    @field: NotEmpty(message = "Invalid input") val password: String,
    @field: NotEmpty(message = "Invalid input") val street: String
) {
    fun toEntity(): Customer {
        return Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            income = this.income,
            email = this.email,
            password = this.password,
            address = Address(
                zipCode = this.zipCode,
                street = this.street
            )
        )
    }
}