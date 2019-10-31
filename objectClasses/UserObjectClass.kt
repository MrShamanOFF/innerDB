/**
Класс объектов Пользователь
 */
package objectClasses

import java.util.*

data class UserObjectClass(
    var id: Int,
    var name: String,
    var dateOfBirth: Date,
    var pay: Double,
    var department: Int
)