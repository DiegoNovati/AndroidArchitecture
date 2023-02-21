package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase

class UseCaseHomeInit(
    repositoryLogger: IRepositoryDevelopmentLogger,
    repositoryAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositoryRuntime: IRepositoryRuntime,
) : UseCaseBase<NoParams, UseCaseHomeInit.Result, FailureHome>(
    repositoryLogger, repositoryAnalytics,
) {
    data class Result(
        val customerList: List<Customer>,
        val bookingList: List<Booking>,
    )

    override suspend fun run(params: NoParams): Either<FailureHome, Result> =
        Result(
            customerList = repositoryRuntime.getCustomerList(),
            bookingList = repositoryRuntime.getBookingList(),
        ).right()
}