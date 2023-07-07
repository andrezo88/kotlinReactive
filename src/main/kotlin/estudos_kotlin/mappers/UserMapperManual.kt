package estudos_kotlin.mappers

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.models.UserModel

class UserMapperManual() {

    fun toModel(dto: UserDto): UserModel {
        val model = UserModel()
        model._id = dto._id
        model.name = dto.name
        model.email = dto.email
        return model
    }

    fun toDto(model: UserModel): UserDto {
        val dto = UserDto()
        dto._id = model._id
        dto.name = model.name
        dto.email = model.email
        return dto
    }
}