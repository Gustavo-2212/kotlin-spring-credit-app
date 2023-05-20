package edu.dio.credit.app.system.Controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.dio.credit.app.system.Model.Customer
import edu.dio.credit.app.system.Repository.CustomerRepository
import edu.dio.credit.app.system.dto.Customer.CustomerDTO
import edu.dio.credit.app.system.dto.Customer.CustomerUpdateDTO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc       // mock das nossas requisições
@ContextConfiguration
class CustomerControllerTest {

    @Autowired private lateinit var customerRepository: CustomerRepository      // para persistir alguns dados para testar as nossas requisições
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper                  // mockar um json para passar como parâmetro no body da nossa requisição

    companion object {
        const val URL: String = "/api/customers"
    }

    // Antes e depois da cada teste, o Banco de Dados será limpado
    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    // método de save (POST)
    @Test
    fun `should create a customer and return 201 status`() {
        // given
        val customerDto: CustomerDTO = buildCustomerDto("Joana", "Morais")
        val customerDtoAsString: String = objectMapper.writeValueAsString(customerDto)

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post("$URL/save").content(customerDtoAsString).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }
    
    @Test
    fun `should not create a customer with same cpf of a saved customer and return status 409`() {
        // given
        customerRepository.save(buildCustomerDto("Joana", "Moura").toEntity())
        val customerDto: CustomerDTO = buildCustomerDto("Joana", "Moura")
        val customerDtoAsString: String = objectMapper.writeValueAsString(customerDto)

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post("$URL/save").contentType(MediaType.APPLICATION_JSON).content(customerDtoAsString))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Data Conflict! Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.dao.DataIntegrityViolationException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save customer with firstName empty and return status 400`() {
        // given
        val customerDto: CustomerDTO = buildCustomerDto("", "Morais")
        val customerDtoAsString: String = objectMapper.writeValueAsString(customerDto)

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post("${URL}/save").contentType(MediaType.APPLICATION_JSON).content(customerDtoAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.web.bind.MethodArgumentNotValidException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    // método get customer by id
    @Test
    fun `should find customer by id and return status 200`() {
        // given
        val customer: Customer = customerRepository.save(buildCustomerDto("Joana", "Silva").toEntity())

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Joana"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("13151549623"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jiszy2@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("400.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("43590129"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua dos Bobos"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer with an invalid id and return status 400`() {
        // given
        val invalidId: Long = 2L

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${invalidId}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class edu.dio.credit.app.system.Exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    // método delete
    // Obs.: O método delete usa o findById (SERVICE)
    @Test
    fun `should delete customer by id and return status 204 no content`() {
        // given
        val customerToDelete: Customer = customerRepository.save(buildCustomerDto("Lúcia", "Camilla").toEntity())

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customerToDelete.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete customer by invalid id and return status 400`() {
        // given
        val invalidId: Long = 2L

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${invalidId}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class edu.dio.credit.app.system.Exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    // método update (PATCH)
    // Ele recebe um id (REQUEST PARAM -> URL?id={id}) e um customerUpdateDto
    @Test
    fun `should update customer and return status 200`() {
        // given
        val customerToUpdate: Customer = customerRepository.save(buildCustomerDto("Diana", "Soares").toEntity())
        val customerUpdated: CustomerUpdateDTO = buildUpdateCustomerDto("Diana", "S. Vieira")
        val customerUpdatedAsString: String = objectMapper.writeValueAsString(customerUpdated)

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customerToUpdate.id}").contentType(MediaType.APPLICATION_JSON).content(customerUpdatedAsString))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Diana"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("S. Vieira"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("13151549623"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jiszy2@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("800.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("45908893"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua dos Bobos"))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update customer with invalid id and return status 400`() {
        // given
        val invalidId: Long = 2L
        val customerUpdated: CustomerUpdateDTO = buildUpdateCustomerDto("Diana", "S. Vieira")
        val customerUpdatedAsString: String = objectMapper.writeValueAsString(customerUpdated)

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${invalidId}").contentType(MediaType.APPLICATION_JSON).content(customerUpdatedAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest).andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class edu.dio.credit.app.system.Exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDto(firstName: String, lastName: String): CustomerDTO {
        return CustomerDTO(
            firstName = firstName,
            lastName = lastName,
            income = BigDecimal.valueOf(400.0),
            cpf = "13151549623",
            zipCode = "43590129",
            email = "jiszy2@gmail.com",
            password = "root",
            street = "Rua dos Bobos"
        )
    }

    private fun buildUpdateCustomerDto(firstName: String, lastName: String): CustomerUpdateDTO {
        return CustomerUpdateDTO(
            firstName = firstName,
            lastName = lastName,
            income = BigDecimal.valueOf(800.0),
            street = "Rua dos Bobos",
            zipCode = "45908893"
        )
    }
}