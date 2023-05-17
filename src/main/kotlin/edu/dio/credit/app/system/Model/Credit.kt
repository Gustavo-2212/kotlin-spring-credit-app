package edu.dio.credit.app.system.Model

import edu.dio.credit.app.system.Enum.Status
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tab_credit")
data class Credit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, name = "credit_code")
    var creditCode: UUID = UUID.randomUUID(),

    @Column(nullable = false, name = "credit_value")
    val creditValue: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, name = "day_first_installment")
    val dayFirstInstallment: LocalDate,

    @Column(nullable = false, name = "number_of_installments")
    val numberOfInstallments: Int = 0,

    @Enumerated
    val status: Status = Status.IN_PROGRESS,

    @ManyToOne
    var customer: Customer? = null
)
