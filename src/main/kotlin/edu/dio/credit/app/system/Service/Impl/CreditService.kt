package edu.dio.credit.app.system.Service.Impl

import edu.dio.credit.app.system.Exception.BusinessException
import edu.dio.credit.app.system.Model.Credit
import edu.dio.credit.app.system.Repository.CreditRepository
import edu.dio.credit.app.system.Service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {

        // Antes de salvarmos um Crédito, precisamos ter certeza que ela esteja
        // atrelada a um Customer
        credit.apply {
            this.customer = customerService.findById(credit.customer?.id!!)
        }

        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> = this.creditRepository.findAllByCustomer(customerId)


    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = this.creditRepository.findByCreditCode(creditCode) ?: throw BusinessException("Credit code ${creditCode} not found")

        return if (credit.customer?.id == customerId) credit else throw BusinessException("Contact admin")
    }
}