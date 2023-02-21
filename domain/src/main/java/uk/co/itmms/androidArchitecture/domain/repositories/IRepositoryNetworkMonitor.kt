package uk.co.itmms.androidArchitecture.domain.repositories

import kotlinx.coroutines.flow.Flow

sealed class RepositoryNoFailure

interface IRepositoryNetworkMonitor {
    fun monitor(): Flow<Boolean>
}