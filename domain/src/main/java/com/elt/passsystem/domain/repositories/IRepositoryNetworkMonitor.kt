package com.elt.passsystem.domain.repositories

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

sealed class RepositoryNoFailure

interface IRepositoryNetworkMonitor {
    suspend fun monitor(): Either<RepositoryNoFailure, Flow<Boolean>>
}