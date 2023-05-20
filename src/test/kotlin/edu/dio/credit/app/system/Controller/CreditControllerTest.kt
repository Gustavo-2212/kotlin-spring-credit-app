package edu.dio.credit.app.system.Controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.dio.credit.app.system.Enum.Status
import edu.dio.credit.app.system.Model.Address
import edu.dio.credit.app.system.Model.Credit
import edu.dio.credit.app.system.Model.Customer
import org.springframework.http.MediaType
import edu.dio.credit.app.system.Repository.CreditRepository
import edu.dio.credit.app.system.Repository.CustomerRepository
import edu.dio.credit.app.system.dto.Credit.CreditDTO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControllerTest {

    @Autowired
    private lateinit var creditRepository: CreditRepository
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var mockMvc: MockMvc

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() = creditRepository.deleteAll()
    @AfterEach
    fun tearDown() = creditRepository.deleteAll()

    // método save

    @Test
    fun `should save a credit and return 201 status`() {
        // given
        val customer: Customer = customerRepository.save( Customer(1L, "Hyalla", "Freitas", "14590889212", BigDecimal.valueOf(200), "ssh@gmail.com", "password", Address("23908821", "Rua dos Bobos")) )
        val creditToSave: CreditDTO = buildCreditDto(150.0)
        val creditToSaveAsString: String = objectMapper.writeValueAsString(creditToSave)

        // when and then
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("$URL/save").content(creditToSaveAsString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        val response: String = mvcResult.response.contentAsString
        Assertions.assertEquals("Credit ${response.subSequence(7,43)} - ${customer.firstName} saved!", response)
    }

    @Test
    fun `should not create a credit with invalid customerId and return 400 status`() {
        // given
        val creditDto: CreditDTO = buildCreditDto(150.0, 7L)
        val creditDtoAsString: String = objectMapper.writeValueAsString(creditDto)

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post("$URL/save").contentType(MediaType.APPLICATION_JSON).content(creditDtoAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class edu.dio.credit.app.system.Exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    // método findAllByCustomer (GET)
    @Test
    fun `should find credit list of a customer by its id and return 200 status`() {
        // given
        val customerId: Long = 1L
        customerRepository.save( Customer(customerId, "Hyalla", "Freitas", "14590889212", BigDecimal.valueOf(200), "ssh@gmail.com", "password", Address("23908821", "Rua dos Bobos")) )
        val firstCredit: Credit = creditRepository.save( buildCreditDto(230.0).toEntity() )
        val secondCredit: Credit = creditRepository.save( buildCreditDto(580.0).toEntity() )

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL?customerId=$customerId").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").value(firstCredit.creditCode.toString()))   // content igual, mas nao funciona
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(firstCredit.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(firstCredit.numberOfInstallments))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditCode").value(secondCredit.creditCode.toString()))   // content igual, mas nao funciona
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditValue").value(secondCredit.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberOfInstallments").value(secondCredit.numberOfInstallments))
            .andDo(MockMvcResultHandlers.print())
    }

    // método find by credit code (GET) with customerId
    @Test
    fun `should find a credit by its credit code and customer id and return 200 status`() {
        // given
        val customer: Customer = customerRepository.save( Customer(1L, "Hyalla", "Freitas", "14590889212", BigDecimal.valueOf(200.0), "ssh@gmail.com", "password", Address("23908821", "Rua dos Bobos")) )
        val credit: Credit = creditRepository.save( buildCreditDto(250.0).toEntity() )

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(credit.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(credit.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(credit.numberOfInstallments))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(credit.status.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income))
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCreditDto(creditValue: Double, customerId: Long = 1L): CreditDTO {
        return CreditDTO(
            creditValue = BigDecimal.valueOf(creditValue),
            dayFirstInstallment = LocalDate.of(2023,5,30),
            numberOfInstallments = 5,
            customerId = customerId
        )
    }
}