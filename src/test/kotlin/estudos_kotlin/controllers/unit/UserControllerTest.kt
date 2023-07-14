package estudos_kotlin.controllers.unit

import estudos_kotlin.controllers.UserController
import estudos_kotlin.dtos.UserDto
import estudos_kotlin.models.UserModel
import estudos_kotlin.services.UserServiceImpl
import io.github.benas.randombeans.api.EnhancedRandom
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@WebFluxTest(controllers = [UserController::class])
class UserControllerTest {

    val BASE_URL = "/v1/users"

    @MockBean
    private var userServiceImpl: UserServiceImpl? = null

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun shouldPostNewUser() {
        val userDto = EnhancedRandom.random(UserDto::class.java)
        val userEntity = EnhancedRandom.random(UserModel::class.java)

        `when`(userServiceImpl!!.createUser(userDto)).thenReturn(Mono.just(userEntity))

        webClient.post()
            .uri(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(userDto)
            .exchange()
            .expectStatus()
            .isCreated()
    }

    @Test
    fun shouldFindAllUsers() {
        val user = EnhancedRandom.random(UserDto::class.java)

        `when`(userServiceImpl!!.findAllUsers()).thenReturn(Flux.just(user))

        webClient.get()
            .uri(BASE_URL)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
    }

    @Test
    fun shouldReturnStatus200WhenFindUserById() {
        val user1 = UserDto(ObjectId("64ad8e2204a52f3dafb51838"), "Gilmar Kunz", "gilma@gaucho.com")
        val id = ObjectId("64ad8e2204a52f3dafb51838")

        `when`(userServiceImpl!!.findUserById(id)).thenReturn(Mono.just(user1))

        webClient.get()
            .uri("$BASE_URL/${id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .is2xxSuccessful


    }

    @Test
    fun shouldReturnStatus200WhenUpdateUserIsSuccessed() {

        val id = ObjectId("64ad8e2204a52f3dafb51838")
        val userInfo = UserDto(ObjectId("64ad8e2204a52f3dafb51838"), "Andrezim", "andre@andre.com")
        val userEntity = UserModel(ObjectId("64ad8e2204a52f3dafb51838"), "Andrezim", "andre@andre.com")

        `when`(userServiceImpl!!.updateUser(id, userInfo)).thenReturn(Mono.just(userEntity))

        webClient
            .put()
            .uri("/v1/users/{id}", id)
            .bodyValue(userInfo)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(UserDto::class.java)
            .consumeWith { userInfoExchangeResult ->
                run {
                    val updatedUser = userInfoExchangeResult.responseBody
                    assert(updatedUser != null)
                    assertEquals("andre@andre.com", updatedUser!!.email)
                }
            }
    }
}