package uk.co.itmms.androidArchitecture.domain.failures

sealed class FailureRepositoryBackend {
    object ConnectionError : FailureRepositoryBackend()
    object BackendError : FailureRepositoryBackend()
}

sealed class FailureRepositoryBackendAuthentication {
    object ConnectionError : FailureRepositoryBackendAuthentication()
    object BackendError : FailureRepositoryBackendAuthentication()
    object LoginError: FailureRepositoryBackendAuthentication()
}