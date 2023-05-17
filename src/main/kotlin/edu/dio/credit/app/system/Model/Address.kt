package edu.dio.credit.app.system.Model

import jakarta.persistence.*

@Embeddable
data class Address(
    @Column(nullable = false, name = "zip_code")
    var zipCode: String = "",

    @Column(nullable = false, name = "street")
    var street: String = ""
)
