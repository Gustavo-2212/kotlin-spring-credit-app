package edu.dio.credit.app.system.Service

import edu.dio.credit.app.system.Enum.Status
import edu.dio.credit.app.system.Exception.BusinessException
import edu.dio.credit.app.system.Model.Credit
import edu.dio.credit.app.system.Model.Customer
import edu.dio.credit.app.system.Repository.CreditRepository
import edu.dio.credit.app.system.Service.Impl.CreditService
import edu.dio.credit.app.system.Service.Impl.CustomerService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test") // usado em testes de integração (a aplicação "sobe")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var customerService: CustomerService
    @MockK lateinit var creditRepository: CreditRepository
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun `should create a credit`() {
        // given
        val mockCustomer: Customer = CustomerServiceTest().buildCustomer(1L)
        val mockCredit: Credit = buildCredit(null, null, mockCustomer)
        every { creditRepository.save(mockCredit) } returns mockCredit
        every { customerService.findById(1L) } returns mockCustomer

        // when
        val actualCredit: Credit = creditService.save(mockCredit)

        // then
        Assertions.assertThat(actualCredit).isNotNull
        Assertions.assertThat(actualCredit).isSameAs(mockCredit)
        verify(exactly = 1) { creditRepository.save(mockCredit) }
    }

    @Test
    fun `should find a list of credit`() {
        // given
        val mockCustomerId: Long = Random().nextLong()
        val mockCustomer: Customer = CustomerServiceTest().buildCustomer(mockCustomerId)
        val mockCreditList: List<Credit> = listOf(
            buildCredit(1L, null, mockCustomer),
            buildCredit(2L, null, mockCustomer),
            buildCredit(2L, null, mockCustomer)
        )
        every { creditRepository.findAllByCustomer(mockCustomerId) } returns mockCreditList

        // when
        val actualCreditList: List<Credit> = creditService.findAllByCustomer(mockCustomerId)

        // then
        Assertions.assertThat(actualCreditList).isNotNull
        Assertions.assertThat(actualCreditList).isSameAs(mockCreditList)
        verify(exactly = 1) { creditRepository.findAllByCustomer(mockCustomerId) }
    }

    // findByCreditCode
    /* Três Cenários
    * Achou o CREDIT CODE: Pode retornar um credit (Achou o ID) ou Business Exception
    * Não achou o CREDIT CODE: Business Exception
    * */
    @Test
    fun `should return a credit`() {
        // given
        val mockCreditCode: UUID = UUID.randomUUID()
        val mockCustomerId: Long = Random().nextLong()
        val mockCredit: Credit = buildCredit(1L, mockCreditCode, CustomerServiceTest().buildCustomer(mockCustomerId))
        every { creditRepository.findByCreditCode(mockCreditCode) } returns mockCredit

        // when
        val actualCredit: Credit = creditService.findByCreditCode(mockCustomerId, mockCreditCode)

        // then
        Assertions.assertThat(actualCredit).isNotNull
        Assertions.assertThat(actualCredit).isSameAs(mockCredit)
        Assertions.assertThat(actualCredit).isExactlyInstanceOf(Credit::class.java)
        verify(exactly = 1) { creditRepository.findByCreditCode(mockCreditCode) }
    }

    @Test
    fun `should not return a credit by an invalid customer id and throw BusinessException`() {
        // given
        val mockCreditCode: UUID = UUID.randomUUID()
        val mockCustomerId: Long = Random().nextLong()
        val mockCredit: Credit = buildCredit(1L, mockCreditCode, CustomerServiceTest().buildCustomer(1L))
        every { creditRepository.findByCreditCode(mockCreditCode) } returns mockCredit

        // when and then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(mockCustomerId, mockCreditCode) }
            .withMessage("Contact admin")
        verify(exactly = 1) { creditRepository.findByCreditCode(mockCreditCode) }
    }

    @Test
    fun `should not return a credit by an invalid credit code and throw BusinessException`() {
        // given
        val mockCreditCode: UUID = UUID.randomUUID()
        val mockCustomerId: Long = Random().nextLong()
        val mockCredit: Credit = buildCredit(1L, null, CustomerServiceTest().buildCustomer(mockCustomerId))
        every { creditRepository.findByCreditCode(mockCreditCode) } returns null

        // when and then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(mockCustomerId, mockCreditCode) }
            .withMessage("Credit code ${mockCreditCode} not found")
        verify(exactly = 1) { creditRepository.findByCreditCode(mockCreditCode) }
    }

    fun buildCredit(id: Long?, creditCode: UUID?, customer: Customer): Credit {
        return Credit(
            id = id ?: 1L,
            creditCode = creditCode ?: UUID.randomUUID(),
            creditValue = BigDecimal.valueOf(100.5),
            dayFirstInstallment = LocalDate.of(2026,7,15),
            numberOfInstallments = 5,
            status = Status.IN_PROGRESS,
            customer = customer
        )
    }
}