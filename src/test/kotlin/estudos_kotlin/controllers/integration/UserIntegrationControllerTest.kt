package estudos_kotlin.controllers.integration

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.models.UserModel
import estudos_kotlin.repositories.UserRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
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
            UserModel(ObjectId("64ad8e2204a52f3dafb51838"), "Gilmar Kunz", "gilma@gaucho.com"))

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

    @Test
    fun findUserById(){
        val id = ObjectId("64ad8e2204a52f3dafb51838")

        webTestClient.get()
            .uri("/v1/users/{id}" , id)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(UserDto::class.java)
    }

    //verificar depois
/*    @Test
    fun deleteUser(){
        val id = ObjectId("64ad8e2204a52f3dafb51838")

        webTestClient.delete()
            .uri("/v1/users/{id}" , id)
            .exchange()
            .expectStatus()
            .isOk

    }*/

    @Test
    fun updateUser() {
        val id = ObjectId("64ad8e2204a52f3dafb51838")
        val userInfo = UserDto(ObjectId("64ad8e2204a52f3dafb51838"), "Andrezim", "andre@andre.com.br")

        webTestClient
            .put()
            .uri("/v1/users/{id}" , id)
            .bodyValue(userInfo)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(UserDto::class.java)
            .consumeWith{userInfoExchangeResult ->
                run {
                    var updatedUser = userInfoExchangeResult.responseBody
                    assert(updatedUser!=null);
                    assertEquals("andre@andre.com.br", updatedUser!!.email)
                }
            }
    }

}
