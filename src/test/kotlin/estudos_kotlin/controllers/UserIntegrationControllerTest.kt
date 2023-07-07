package estudos_kotlin.controllers

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.models.UserModel
import estudos_kotlin.repositories.UserRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebClient

class UserIntegrationControllerTest @Autowired constructor(
    private val webTestClient: WebTestClient,
    private val userRepository: UserRepository
) {

    val BASE_URL = "/v1/users"

    @BeforeEach
    fun setup() {
        val users = listOf(
            UserModel(null, "Carolina Kunz", "carol.kunz@gbele.com.br"),
            UserModel(null, "Pedro Henrique", "pedro@henrique.com"),
            UserModel(null, "Gustavo Moreira", "gus.silva@moreira.com"),
            UserModel(null, "Gilmar Kunz", "gilma@gaucho.com"))

        userRepository.saveAll(users)
            .then()
            .block()
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
            .then()
            .block()
    }

    @Test
    fun createUser() {
        val userInfo = UserDto(null, "Andrezim", "andre@com.br")

        webTestClient.post()
            .uri(BASE_URL)
            .bodyValue(userInfo)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserDto::class.java)
            .consumeWith{userInfoExchangeResult ->
                run {
                    val userDto = userInfoExchangeResult.responseBody
                    assertNotNull(userDto)
                    assertNotNull(userDto!!._id)
                    assertEquals(userInfo.name, userDto.name)
                    assertEquals(userInfo.email, userDto.email)
                }
            }
    }

    @Test
    fun findAllUsers() {
        webTestClient.get()
            .uri(BASE_URL)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBodyList(UserDto::class.java)
            .hasSize(4)
    }

//    @Test
//    fun findUserById() {
//        val userById = UserModel(ObjectId("1"),"pedrim do batuque", "batuque@coisalinda.com")
//    }

    @Test
    fun deleteUser() {
    }

    @Test
    fun updateUser() {
    }
}