package dataBase

import objectClasses.DepartmentObjectClass
import objectClasses.UserObjectClass
import java.util.*

class DbUsers { //todo я так понял это класс управления данными, по факту он одинаковый с департаментами, подучай как можно это обыграть
    private val userList = mutableMapOf<Int, UserObjectClass>()
    private var increment: Int = 0;

    fun create(name: String, dateOfBirth: Date, pay: Double, department: Int): Int{
        increment++
        val user = UserObjectClass(increment, name, dateOfBirth, pay, department)
        userList[increment] = user
        return increment
    }

    fun read(id: Int): UserObjectClass?{
        return userList.getOrDefault(id,null)
    }

    fun update(id: Int, name: String, dateOfBirth: Date, pay: Double, department: Int){
        val user = UserObjectClass(id, name, dateOfBirth, pay, department)
        userList.replace(id, user)
    }

    fun delete(id: Int){
        userList.remove(id)
    }

    fun deleteAll(){
        userList.clear()
    }

    fun getAll(): MutableCollection<UserObjectClass> {
        return userList.values
    }
}