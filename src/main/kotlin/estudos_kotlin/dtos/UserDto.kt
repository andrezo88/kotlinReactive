package estudos_kotlin.dtos

import org.bson.types.ObjectId

data class UserDto (
    var _id: ObjectId? = null,
    var name: String? = null,
    var email: String? = null
)