package estudos_kotlin.services

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.mappers.UserMapperManual
import estudos_kotlin.models.UserModel
import estudos_kotlin.repositories.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Component
data class UserServiceImpl (
    private final val userRepository: UserRepository,

) {
    fun createUser(userDto: UserDto): Mono<UserModel> {
        return userRepository.save(UserMapperManual().toModel(userDto))
    }

    fun findAllUsers(): Flux<UserDto> {
        return userRepository.findAll()
            .map { UserMapperManual().toDto(it) }
    }

    fun findUserById(userId: ObjectId): Mono<UserDto> {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(NotFoundException()))
            .map { UserMapperManual().toDto(it) }
    }

    fun deleteUser(userId: ObjectId): Mono<Void> {
    return userRepository.deleteById(userId)
        .switchIfEmpty(Mono.error(NotFoundException()))
    }

    fun updateUser(userId: ObjectId, @RequestBody userDto:UserDto): Mono<UserModel> {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(NotFoundException()))
            .map { UserMapperManual().toDto(it) }
            .map { UserMapperManual().toModel(it) }
            .flatMap { userRepository.save(it) }
    }

}


