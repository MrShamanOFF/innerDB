package dataBase

import objectClasses.DepartmentObjectClass

class DbDepartments {
    private val departmentList = mutableMapOf<Int, DepartmentObjectClass>()
    private var increment = 0;

    fun create(name: String, description: String): Int {
        increment++
        val department = DepartmentObjectClass(increment, name, description)
        departmentList[increment] = department
        return increment
    }

    fun read(id: Int): DepartmentObjectClass? {
        return departmentList.getOrDefault(id, null)
    }

    fun update(id: Int, name: String, description: String) {
        val department = DepartmentObjectClass(id, name, description)
        departmentList.replace(id, department)
    }

    fun delete(id: Int) {
        departmentList.remove(id)
    }

    fun deleteAll(){
        departmentList.clear()
    }

    fun getAll(): MutableCollection<DepartmentObjectClass> {
        return departmentList.values
    }
}