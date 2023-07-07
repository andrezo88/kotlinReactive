package estudos_kotlin.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class UserModel (

    @Id
    var _id: ObjectId? = null,
    var name: String? = null,
    var email: String? = null
)