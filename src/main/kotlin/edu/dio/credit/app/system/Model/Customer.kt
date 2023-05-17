package edu.dio.credit.app.system.Model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "tab_customer")
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, name = "first_name")
    var firstName: String = "",

    @Column(nullable = false, name = "last_name")
    var lastName: String = "",

    @Column(nullable = false, unique = true, name = "cpf")
    var cpf: String = "",

    @Column(nullable = false, name = "income")
    var income: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, unique = true, name = "email")
    var email: String = "",

    @Column(nullable = false, name = "password")
    var password: String = "",

    @Column(nullable = false)
    @Embedded
    var address: Address = Address(),

    @Column(nullable = false)
    @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.REMOVE), mappedBy = "customer")
    var credits: List<Credit> = mutableListOf()
)
