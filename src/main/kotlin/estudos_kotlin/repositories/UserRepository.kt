package estudos_kotlin.repositories

import estudos_kotlin.models.UserModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository: ReactiveMongoRepository<UserModel, Long> {
    fun findById(userId: ObjectId): Mono<UserModel>
    fun deleteById(userId: ObjectId): Mono<Void>
}