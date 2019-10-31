import dataBase.*
import objectClasses.UserObjectClass
import java.text.SimpleDateFormat
import java.util.*

class ApplicationController {
    inner class Command{
        var id: String = "EMPTY"
        var sCommand = "EMPTY"
        var sObject = "EMPTY"
        val commandSelector = arrayOf("SELECT","UPDATE","CREATE","DELETE","HELP","QUIT")
        val objectSelector = arrayOf("USER", "DEPARTMENT")
    }
    private var departments = DbDepartments()
    private var users = DbUsers()
    private val defaultErrorMessage = arrayOf("Unknown command. Use \"help\" for Help", "Unknown Object", "Value must be \"*\" or digit")

    private fun commandControl(): Command{
        val structedCommand = Command()
        val command = readLine()
        if (command!!.isNotEmpty()){
            try {
                if (structedCommand.commandSelector.contains(command.split(" ")[0].toUpperCase())){
                    structedCommand.sCommand = command.split(" ")[0].toUpperCase()
                }
            } catch (e: Exception){}

            try {
                if (structedCommand.objectSelector.contains(command.split(" ")[1].toUpperCase())){
                    structedCommand.sObject = command.split(" ")[1].toUpperCase()
                }
            }catch (e: Exception){}

            try {
                if (command.split(" ")[2] == "*" || command.split(" ")[2].matches(Regex("\\d+"))){
                    structedCommand.id = command.split(" ")[2]
                }
            } catch (e: Exception){}

        }
        return structedCommand
    }

    fun run(){
        while (true){
            val cmdCommand = commandControl()
            val commandSelector = cmdCommand.sCommand
            val objectSelector = cmdCommand.sObject
            val id = cmdCommand.id
            when(commandSelector){
                "SELECT"    -> {select(objectSelector, id)}
                "UPDATE"    -> {update(objectSelector, id)}
                "CREATE"    -> {create(objectSelector)}
                "DELETE"    -> {delete(objectSelector, id)}
                "HELP", "?" -> {help()}
                "QUIT"      -> {kotlin.system.exitProcess(0)}
                else -> {println(defaultErrorMessage[0])}
            }
        }
    }

    private fun select(objectClass: String, id: String){
        val result: Any?
        when (objectClass) {
            "USER" -> {
                result = when{
                    id == "*"                         -> { users.getAll() }
                    id.matches(Regex("\\d+")) -> { users.read(id.toInt()) }
                    else                              -> { defaultErrorMessage[2] }
                }
            }
            "DEPARTMENT" -> {
                result = when{
                    id == "*"                         -> { departments.getAll() }
                    id.matches(Regex("\\d+")) -> { departments.read(id.toInt()) }
                    else                              -> { defaultErrorMessage[2] }
                }
            }
            else -> {result = defaultErrorMessage[1]}
        }
        println(result)
    }

    private fun update(objectClass: String, id: String){
        when(objectClass){
            "USER" -> {
                when{
                    id.matches(Regex("\\d+")) -> {
                        println("Trying to edit UserObjectClass with id $id")
                        val user = users.read(id.toInt())
                        if(user == null){
                            println("Not found user with id=$id")
                        } else {
                            println("Founded object: $user")
                            print("User Name: \t")
                            val name = readLine()!!

                            print("User Birth Date (formate dd.mm.yyyy): \t")
                            var dateInput = readLine()
                            while (!dateInput!!.matches(Regex("^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*\$"))){
                                println("Input error. Value must be check with pattern \"dd.mm.yyyy\". Try again\r\nBirth Date \t")
                                dateInput = readLine()
                            }
                            val date = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateInput)

                            print("Pay in month \t")
                            var payInput = readLine()
                            while (!payInput!!.matches(Regex("\\d+"))){
                                println("Input error. Value must be a digit. Try again\r\nPay in month \t")
                                payInput = readLine()
                            }
                            val pay = payInput.toDouble()

                            var departmentInput: String = ""
                            var department = -1
                            var departmentObj = departments.read(department)
                            while (departmentObj == null){
                                print("ID of Department \t")
                                departmentInput = readLine()!!
                                while (!departmentInput.matches(Regex("\\d+"))){
                                    println("Input error. Value must be a digit. Try again\r\nID of Department \t")
                                    departmentInput = readLine()!!
                                }
                                department = departmentInput.toInt()
                                departmentObj = departments.read(department)
                                if (departmentObj == null) println("Department with id $department not found. Try again")
                            }
                            users.update(id.toInt(),name,date,pay,department)
                            println("User with id=$id is updated")
                            println(users.read(id.toInt()))
                        }
                    }
                    else                              -> {println("id must be digit")}
                }
            }
            "DEPARTMENT" -> {
                when{
                    id.matches(Regex("\\d+")) -> {
                        println("Trying to edit DepartmentObjectClass with id $id")
                        val department = departments.read(id.toInt())
                        if (department == null){
                            println("Not found department with id=$id")
                        } else {
                            print("Department Name")
                            val name = readLine()!!
                            print("Department Description")
                            val description = readLine()!!
                            departments.update(id.toInt(), name, description)
                            println("Department with id=$id is updated")
                            println(departments.read(id.toInt()))
                        }
                    }
                    else -> {println("id must be digit")}
                }
            }
            else -> {println(defaultErrorMessage[1])}
        }
    }

    private fun create(objectClass: String){
        when(objectClass){
            "USER" -> {
                if(departments.getAll().isEmpty()){
                    println("No Departments in DB. Please create Department at first")
                    print("Department Name \t")
                    val name = readLine()!!
                    print("Department Description \t")
                    val description = readLine()!!
                    println("Created Department with id = ${departments.create(name, description)}\r\n Creating User Now...")
                }
                print("User Name: \t")
                val name = readLine()!!

                print("User Birth Date (formate dd.mm.yyyy): \t")
                var dateInput = readLine()
                while (!dateInput!!.matches(Regex("^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*\$"))){
                    println("Input error. Value must be check with pattern \"dd.mm.yyyy\". Try again\r\nBirth Date \t")
                    dateInput = readLine()
                }
                val date = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateInput)

                print("Pay in month \t")
                var payInput = readLine()
                while (!payInput!!.matches(Regex("\\d+"))){
                    println("Input error. Value must be a digit. Try again\r\nPay in month \t")
                    payInput = readLine()
                }
                val pay = payInput.toDouble()

                var departmentInput: String = ""
                var department = -1
                var departmentObj = departments.read(department)
                while (departmentObj == null){
                    print("ID of Department \t")
                    departmentInput = readLine()!!
                    while (!departmentInput.matches(Regex("\\d+"))){
                        println("Input error. Value must be a digit. Try again\r\nID of Department \t")
                        departmentInput = readLine()!!
                    }
                    department = departmentInput.toInt()
                    departmentObj = departments.read(department)
                    if (departmentObj == null) println("Department with id $department not found. Try again")
                }
                println("Created User with id = ${users.create(name, date, pay, department)}")
            }
            "DEPARTMENT" -> {
                print("Department Name")
                val name = readLine()!!
                print("Department Description")
                val description = readLine()!!
                println("Created Department with id = ${departments.create(name, description)}")
            }
            else -> {println(defaultErrorMessage[1])}
        }
    }

    private fun delete(objectClass: String, id: String){
        when(objectClass){
            "USER" -> {
                when{
                    id == "*"                         -> {
                        println("Are you sure to continue? yes/no")
                        when(readLine()){
                            "yes" -> {
                                users.deleteAll()
                                println("All data was delete")
                            }
                            else -> {
                                println("Canceled")
                            }
                        }
                    }
                    id.matches(Regex("\\d+")) -> {
                        if(users.read(id.toInt()) == null){
                            println("Not found User with id=$id")
                        } else {
                            users.delete(id.toInt())
                            println("User with id=$id was deleted successful")
                            println(users.getAll())
                        }
                    }
                    else                              -> {println(defaultErrorMessage[2])}
                }
            }
            "DEPARTMENT" -> {
                when{
                    id == "*" -> {
                        println("Deleting all department will delete all users\r\nAre you sure to continue? yes/no")
                        when(readLine()){
                            "yes" -> {
                                users.deleteAll()
                                departments.deleteAll()
                                println("All data deleted successful")
                            }
                            else -> {
                                println("Canceled")
                            }
                        }
                    }
                    id.matches(Regex("\\d+")) -> {
                        if (departments.read(id.toInt()) == null){
                            println("Not found Department with id=$id")
                        } else {
                            val allUserList = users.getAll()
                            val count:MutableList<UserObjectClass>? = mutableListOf()
                            allUserList.forEach {
                                val currentUserDepartmentId = it.department
                                if (currentUserDepartmentId == id.toInt()){
                                    count?.add(it)
                                }
                            }
                            when (count) {
                                null -> {
                                    departments.delete(id.toInt())
                                    println("Department with id=$id was deletes successful")
                                }
                                else -> {
                                    println("Department with id=$id is used in a table of Users.\r\n At first delete all users with ID from this list:")
                                    count.forEach { println("User id:\t${it.id}") }
                                }
                            }
                        }
                    }
                    else                              -> {println(defaultErrorMessage[2])}
                }
            }
            else -> {println(defaultErrorMessage[1])}
        }
    }

    private fun help(){
        println("Supported Commands:")
        println("SELECT: \r\n\t \"select user/department id/*\" - show User/Department with some id (* - select all data)")
        println("UPDATE: \r\n\t \"update user/department id\" - update User/Department with some id")
        println("CREATE: \r\n\t \"create user/department\" - create User/Department. After creation will return id")
        println("DELETE: \r\n\t \"delete user/department id/*\" - delete User/Department with some id (* - delete all data)")
        println("Use 'quit' for terminate")
    }
}