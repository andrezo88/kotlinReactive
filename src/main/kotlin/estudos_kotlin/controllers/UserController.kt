package estudos_kotlin.controllers

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.models.UserModel
import estudos_kotlin.services.UserServiceImpl
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserServiceImpl
) {
    @PostMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    fun createUser(@RequestBody userDto: UserDto): Mono<UserModel> {
        return userService.createUser(userDto)
    }

    @GetMapping
    fun findAllUsers(): Flux<UserDto> {
        return userService.findAllUsers()
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: ObjectId): Mono<UserDto> {
        return userService.findUserById(id)
            .switchIfEmpty(Mono.error(Exception()))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: ObjectId): Mono<Unit> {
        return userService.deleteUser(id)
            .switchIfEmpty(Mono.error(Exception()))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: ObjectId, @RequestBody userDto:UserDto): Mono<UserModel> {
        return userService.updateUser(id, userDto)
            .switchIfEmpty(Mono.error(Exception()))
    }
}