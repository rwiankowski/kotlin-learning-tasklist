package tasklist

fun main() {

    val taskList = TaskList()
    taskList.diskData("load")

    do {
        println("Input an action (add, print, edit, delete, end):")

        when(readln().lowercase()) {
            "add" -> taskList.addTask()
            "print" -> taskList.printAllTasks()
            "edit" -> taskList.editTask()
            "delete" -> taskList.deleteTask()
            "end" -> {
                println("Tasklist exiting!")
                taskList.diskData("save")
                return
            }
            else -> println("The input action is invalid")
        }
    } while (true)
}


