package edu.dio.credit.app.system.Repository

import edu.dio.credit.app.system.Model.Address
import edu.dio.credit.app.system.Model.Credit
import edu.dio.credit.app.system.Model.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository          // instância da classe que sera testada
    @Autowired lateinit var testEntityManager: TestEntityManager        // persistência de dados no DB para testes

    private lateinit var mockDbCustomer: Customer
    private lateinit var mockFirstDbCredit: Credit
    private lateinit var mockSecondDbCredit: Credit

    @BeforeEach     // antes de cada caso de teste, a função 'setup' será chamada
    fun setup() {
        mockDbCustomer = testEntityManager.persist(buildCustomer("Luana", "Gonçalves"))
        mockFirstDbCredit = testEntityManager.persist(buildCredit(mockDbCustomer))
        mockSecondDbCredit = testEntityManager.persist(buildCredit(mockDbCustomer))
    }

    @Test
    fun `should find credit by credit code`() {
        // given
        val mockFirstCreditCode: UUID = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val mockSecondCreditCode: UUID = UUID.fromString("49f740ba-46a7-449b-84e7-ff5b798d7ef")

        mockFirstDbCredit.creditCode = mockFirstCreditCode
        mockSecondDbCredit.creditCode = mockSecondCreditCode

        // when
        val fakeFirstCredit: Credit = creditRepository.findByCreditCode(mockFirstCreditCode)!!
        val fakeSecondCredit: Credit = creditRepository.findByCreditCode(mockSecondCreditCode)!!

        // then
        Assertions.assertThat(fakeFirstCredit).isNotNull
        Assertions.assertThat(fakeSecondCredit).isNotNull

        Assertions.assertThat(fakeFirstCredit).isSameAs(mockFirstDbCredit)
        Assertions.assertThat(fakeSecondCredit).isSameAs(mockSecondDbCredit)
    }

    @Test
    fun `should find all credits by customer id`() {
        // given
        val mockCustomerId: Long = 1L

        // when
        val mockCreditList: List<Credit> = creditRepository.findAllByCustomer(mockCustomerId)

        // then
        Assertions.assertThat(mockCreditList).isNotEmpty
        Assertions.assertThat(mockCreditList.size).isEqualTo(2)
        Assertions.assertThat(mockCreditList).contains(mockFirstDbCredit, mockSecondDbCredit)
    }

    private fun buildCustomer(firstName: String, lastName: String): Customer {
        return Customer(
            firstName = firstName,
            lastName = lastName,
            income = BigDecimal.valueOf(500),
            email = "jiszy2@gmail.com",
            password = "1234",
            cpf = "12390887601",
            address = Address("23908987", "Rua dos Bobos")
        )
    }

    private fun buildCredit(customer: Customer): Credit {
        return Credit(
            creditValue = BigDecimal.valueOf(2000),
            dayFirstInstallment = LocalDate.of(2024,5,15),
            numberOfInstallments = 5,
            customer = customer
        )
    }
}