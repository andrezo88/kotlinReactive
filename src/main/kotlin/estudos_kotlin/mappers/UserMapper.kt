package estudos_kotlin.mappers

import estudos_kotlin.dtos.UserDto
import estudos_kotlin.models.UserModel
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy


@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = []
)
interface UserMapper {
    fun toModel(dto: UserDto): UserModel
    fun toDto(model: UserModel): UserDto
}