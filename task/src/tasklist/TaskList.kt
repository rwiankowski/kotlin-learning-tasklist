package tasklist

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.*
import java.io.File

class TaskList {

    private var tasks = mutableListOf<Task>()

    fun printAllTasks(): Boolean {
        if (tasks.isEmpty()) {
            println("No tasks have been input")
            return false
        }
        println("+----+------------+-------+---+---+--------------------------------------------+\n" +
                "| N  |    Date    | Time  | P | D |                   Task                     |\n" +
                "+----+------------+-------+---+---+--------------------------------------------+")
        tasks.forEach { task ->
            println(task.toString())
            println("+----+------------+-------+---+---+--------------------------------------------+")
        }

        return true
    }

    fun addTask() {

        val priority : String = setPriority()
        val date : String = setDate()
        val time : String = setTime()
        val items : MutableList<String> = setItems()

        if (items.isEmpty()) println("The task is blank")
        else {
            val nextId = tasks.size + 1
            tasks.add(Task(nextId, priority.first(), date, time, items))
        }
    }

    fun editTask() {
        if(!printAllTasks()) return
        val taskToEdit = selectTask(tasks.size)

        if (taskToEdit == 0) return
        else {
            val indexToEdit = taskToEdit - 1
            do {
                println("Input a field to edit (priority, date, time, task): ")

                when(readln().lowercase()) {
                    "priority" -> {
                        tasks[indexToEdit].priority = setPriority().first()
                        break
                    }
                    "date" -> {
                        tasks[indexToEdit].date = setDate()
                        break
                    }
                    "time" -> {
                        tasks[indexToEdit].time = setTime()
                        break
                    }
                    "task" -> {
                        tasks[indexToEdit].items = setItems()
                        break
                    }
                    else -> println("Invalid field")
                }
            } while (true)
        }
        println("The task is changed")
    }

    fun deleteTask() {
        if(!printAllTasks()) return
        val taskToDelete = selectTask(tasks.size)

        if (taskToDelete == 0) return
        else {
            val indexToDelete = taskToDelete - 1
            tasks.removeAt(indexToDelete)
        }
        var counter = 1
        tasks.forEach { task -> task.id = counter++ }
        println("The task is deleted")

    }

    fun diskData(action: String) {

        val jsonFile = File("tasklist.json")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(MutableList::class.java, Task::class.java)
        val taskListAdapter = moshi.adapter<MutableList<Task>>(type)

        if (action.lowercase() == "save") {
            val jsonData = taskListAdapter.toJson(tasks)
            jsonFile.writeText(jsonData)
        }

        if (action.lowercase() == "load") {
            if (!jsonFile.exists()) return

            val jsonData = jsonFile.readText()
            tasks = taskListAdapter.fromJson(jsonData)!!
        }


    }


    private fun selectTask(maxSize: Int): Int {

        if (tasks.isEmpty()) {
            println("No tasks have been input")
            return 0
        }

        do {
            println("Input the task number (1-$maxSize):")

            try {
                val index = readln().toInt()
                if (index in 1..maxSize) return index
                else println("Invalid task number")
            } catch (e: Exception) {
                println("Invalid task number")
            }


        } while (true)

    }

    private fun setItems(): MutableList<String> {
        val items = mutableListOf<String>()
        println("Input a new task (enter a blank line to end):")
        do {
            var newItem: String = readln().trim { it.isWhitespace() }
            if (newItem.isNotBlank()) {
                do {
                    if(newItem.length > 44) {
                        items.add(newItem.substring(0, 44))
                        newItem = newItem.substring(44)
                    }
                } while(newItem.length > 44)
                items.add(newItem)

            }
        } while (newItem.isNotBlank())
        return items
    }

    private fun setTime(): String {
        do {
            println("Input the time (hh:mm):")
            try {
                val timeInput = readln().split(':').map { it.toInt() }.toMutableList()
                return LocalTime(timeInput[0], timeInput[1]).toString()

            } catch (e: RuntimeException) {
                println("The input time is invalid")
            }
        } while (true)
     }

    private fun setDate(): String {
        do {
            println("Input the date (yyyy-mm-dd):")

            try {
                val dateInput = readln().split('-').map { it.toInt() }.toMutableList()
                return LocalDate(dateInput[0], dateInput[1], dateInput[2]).toString()

            } catch (e: RuntimeException) {
                println("The input date is invalid")
            }
        } while (true)
    }

    private fun setPriority(): String {
        var priority: String

        do {

            println("Input the task priority (C, H, N, L):")
            readln().uppercase().also { priority = it }

        } while (priority !in listOf("C", "H", "N", "L"))
        return priority
    }

}