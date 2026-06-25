package com.ngoctientnt.template.core.database.domain.usecase

import com.ngoctientnt.template.core.database.domain.model.User
import com.ngoctientnt.template.core.database.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User): Result<Unit> = userRepository.save(user)
}
