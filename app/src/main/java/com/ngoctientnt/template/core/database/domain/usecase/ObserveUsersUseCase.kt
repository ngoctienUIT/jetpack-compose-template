package com.ngoctientnt.template.core.database.domain.usecase

import com.ngoctientnt.template.core.database.domain.model.User
import com.ngoctientnt.template.core.database.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<List<User>> = userRepository.observeAll()
}
