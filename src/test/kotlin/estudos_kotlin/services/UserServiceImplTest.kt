package estudos_kotlin.services

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.mappers.UserMapperManual
import estudos_kotlin.models.UserModel
import estudos_kotlin.repositories.UserRepository
import io.github.benas.randombeans.api.EnhancedRandom
import io.github.benas.randombeans.api.EnhancedRandom.random
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    private var userRepository: UserRepository? = null
    @Spy
    private var userMapperManual: UserMapperManual? = null
    @InjectMocks
    private var userService: UserServiceImpl? = null


    @Test
    fun createUser() {
        val userDto = random(UserDto::class.java)
        val userEntity = random(UserModel::class.java)
        userDto._id = userEntity._id
        userDto.name = userEntity.name
        userDto.email = userEntity.email

        given(userRepository!!.save(userEntity)).willReturn(Mono.just(userEntity))

        StepVerifier.create(userService!!.createUser(userDto))
            .then{
                verify (userRepository!!, times(1)).save(userEntity)
            }
            .assertNext{
                assertEquals(userDto.name, userEntity.name)
            }
            .verifyComplete()
    }

    @Test
    fun findAllUsers() {

        val usersModel = Flux.just(random(UserModel::class.java))

        given(userRepository!!.findAll()).willReturn(usersModel)

        StepVerifier.create(userService!!.findAllUsers())
            .then{
                verify (userRepository!!, times(1)).findAll()
        }
            .assertNext{
                assertNotNull(usersModel)
            }
            .verifyComplete()
    }

    @Test
    fun findUserById() {

        val userDto = UserDto(ObjectId("64b038547ff5a70cc3c2fcfc"), "André Abreu", "andre@andre.com.br")
        val userEntity = UserModel(ObjectId("64b038547ff5a70cc3c2fcfc"), "André Abreu", "andre@andre.com.br")

        given(userRepository!!.findById(userDto._id!!)).willReturn(Mono.just(userEntity))

        StepVerifier.create(userService!!.findUserById(userDto._id!!))
            .then{
                verify (userRepository!!, times(1)).findById(userDto._id!!)
            }
            .assertNext{
                assertEquals(userDto.name, userEntity.name)
            }
            .verifyComplete()

    }

    @Test
    fun deleteUser() {
        val userDto = UserDto(ObjectId("64b038547ff5a70cc3c2fcfc"), "André Abreu", "andre@andre.com.br")
        val userEntity = UserModel(ObjectId("64b038547ff5a70cc3c2fcfc"), "André Abreu", "andre@andre.com.br")

        given(userRepository!!.deleteById(userEntity._id!!)).willReturn(Mono.just(Unit))

        StepVerifier.create(userService!!.deleteUser(userDto._id!!))
            .then{
                verify (userRepository!!, times(1)).deleteById(userDto._id!!)
            }
            .assertNext{
                assertEquals(userDto.name, userEntity.name)
            }
            .verifyComplete()

    }

    @Test
    fun updateUser() {

        val userDto = UserDto(ObjectId("64b038547ff5a70cc3c2fcfc"), "André Abreu", "andre@andre.com.br")
        val userEntity = UserModel(ObjectId("64b038547ff5a70cc3c2fcfc"), "André Abreu", "andre@andre.com")

        given(userRepository!!.findById(userDto._id!!)).willReturn(Mono.just(userEntity))
        given(userMapperManual!!.toModel(userDto)).willReturn(userEntity)
        given(userRepository!!.save(userEntity)).willReturn(Mono.just(userEntity))

        StepVerifier.create(userService!!.updateUser(userDto._id!!, userDto))
            .then{
                verify(userRepository!!, times(1)).findById(userDto._id!!)
                verify(userRepository!!, times(1)).save(userEntity)
            }
            .assertNext{
                assertEquals("andre@andre.com", userEntity.email)
            }
            .verifyComplete()
    }

}