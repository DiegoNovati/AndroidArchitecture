package uk.co.itmms.androidArchitecture.domain.usecases.home

import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import uk.co.itmms.androidArchitecture.domain.usecases.UseCaseBase

class UseCaseHomeInit(
    repositoryDevelopmentLogger: IRepositoryDevelopmentLogger,
    repositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics,
    private val repositorySession: IRepositorySession,
) : UseCaseBase<NoParams, UseCaseHomeInit.Result, FailureHome>(
    repositoryDevelopmentLogger, repositoryDevelopmentAnalytics,
) {
    data class Result(
        val customerList: List<Customer>,
        val bookingList: List<Booking>,
    )

    override suspend fun run(params: NoParams): Either<FailureHome, Result> =
        Result(
            customerList = repositorySession.customerList,
            bookingList = repositorySession.bookingList,
        ).right()
}