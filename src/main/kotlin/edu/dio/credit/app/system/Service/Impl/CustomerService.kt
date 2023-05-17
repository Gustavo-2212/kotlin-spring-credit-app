package edu.dio.credit.app.system.Service.Impl

import edu.dio.credit.app.system.Exception.BusinessException
import edu.dio.credit.app.system.Model.Customer
import edu.dio.credit.app.system.Repository.CustomerRepository
import edu.dio.credit.app.system.Service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
): ICustomerService {

    override fun delete(id: Long) {
        // ou: this.customerRepository.deleteById(id)
        val customer = this.findById(id)
        return this.customerRepository.delete(customer)
    }

    override fun findById(id: Long): Customer {
        val customer = this.customerRepository.findById(id).orElseThrow{            // Optional
            throw BusinessException("Id ${id} is not found!")
        }

        return customer
    }

    // override fun save(customer: Customer): Customer = this.customerRepository.save(customer)
    override fun save(customer: Customer): Customer {
        return this.customerRepository.save(customer)
    }
}