package edu.dio.credit.app.system.Repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import edu.dio.credit.app.system.Model.Customer

@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {
}