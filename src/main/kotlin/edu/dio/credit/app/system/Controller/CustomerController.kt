package edu.dio.credit.app.system.Controller

import edu.dio.credit.app.system.Model.Customer
import edu.dio.credit.app.system.Service.Impl.CustomerService
import edu.dio.credit.app.system.dto.Customer.CustomerDTO
import edu.dio.credit.app.system.dto.Customer.CustomerUpdateDTO
import edu.dio.credit.app.system.dto.Customer.CustomerView
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping("/save")
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDTO): ResponseEntity<CustomerView> {
        val savedCustomer = this.customerService.save(customerDto.toEntity())

        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(savedCustomer))
    }

    @GetMapping("/{customerId}")
    fun findById(@PathVariable customerId: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(customerId)

        return ResponseEntity.status(HttpStatus.OK).body( CustomerView(customer) )
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable customerId: Long) {
        this.customerService.delete(customerId)
    }

    // http://localhost:8080/api/customers?customerId={customerId}
    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") customerId: Long,
                       @RequestBody @Valid customerUpdateDTO: CustomerUpdateDTO
    ): ResponseEntity<CustomerView> {

        val customer = this.customerService.findById(customerId)
        val customerToUpdate = customerUpdateDTO.toEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)

        return ResponseEntity.status(HttpStatus.OK).body( CustomerView(customerUpdated) )
    }
}