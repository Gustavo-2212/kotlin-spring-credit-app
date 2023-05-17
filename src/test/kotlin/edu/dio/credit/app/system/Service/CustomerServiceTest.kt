package edu.dio.credit.app.system.Service

import edu.dio.credit.app.system.Exception.BusinessException
import edu.dio.credit.app.system.Model.Address
import edu.dio.credit.app.system.Model.Customer
import edu.dio.credit.app.system.Repository.CustomerRepository
import edu.dio.credit.app.system.Service.Impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*


@ActiveProfiles("test")     // usado em testes de integração (a aplicação "sobe")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK lateinit var customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun `should create customer`() {
        // given
        val fakeCustomer: Customer = buildCustomer(1L)

        every { customerRepository.save(any()) } returns fakeCustomer

        // when
        val actualCustomer: Customer = customerService.save(fakeCustomer)

        // then
        Assertions.assertThat(actualCustomer).isNotNull
        Assertions.assertThat(actualCustomer).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should find a customer by id` () {
        // given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)

        // when
        val actualCustomer: Customer = customerService.findById(fakeId)

        // then
        Assertions.assertThat(actualCustomer).isNotNull
        Assertions.assertThat(actualCustomer).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actualCustomer).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should not found customer by an invalid id and throw BusinessException` () {
        // given
        val fakeId: Long = Random().nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()

        // when and then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findById(fakeId) }
            .withMessage("Id $fakeId is not found!")
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should delete customer by id`() {
        // given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs

        // when
        customerService.delete(fakeId)
        
        // then
        verify(exactly = 1) { customerRepository.findById(fakeId) }
        verify(exactly = 1) { customerRepository.delete(fakeCustomer) }
    }

    fun buildCustomer(id: Long?): Customer {
        return Customer(
            id = id ?: Random().nextLong(),
            firstName = "Gustavo",
            lastName = "Alves de Oliveira",
            cpf = "13151549608",
            income = BigDecimal.valueOf(130.3),
            email = "jiszy22@gmail.com",
            password = "1234",
            Address(zipCode = "38307072", "Rua dos Bobos")
        )
    }
}