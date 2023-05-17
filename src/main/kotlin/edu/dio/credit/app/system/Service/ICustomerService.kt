package edu.dio.credit.app.system.Service

import edu.dio.credit.app.system.Model.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long)
}