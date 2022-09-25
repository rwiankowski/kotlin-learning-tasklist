package tasklist

import kotlinx.datetime.*

data class Task(var id : Int, var priority: Char, var date: String, var time: String, var items: MutableList<String>) {

    override fun toString(): String {

        val daysToGo = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.daysUntil(date.toLocalDate())

        val tag = when {
            daysToGo < 0 -> "\u001B[101m \u001B[0m"
            daysToGo == 0 -> "\u001B[103m \u001B[0m"
            else-> "\u001B[102m \u001B[0m"
        }

        val prio = when (priority) {
            'C' -> "\u001B[101m \u001B[0m"
            'H' -> "\u001B[103m \u001B[0m"
            'N' -> "\u001B[102m \u001B[0m"
            else -> "\u001B[104m \u001B[0m"
        }

        var stringValue = "| ${id.toString().padEnd(3, ' ')}| $date | $time | $prio | $tag |${items[0].padEnd(44, ' ')}|"
        for(index in 1 .. items.lastIndex) {
            stringValue += "\n|    |            |       |   |   |${items[index].padEnd(44, ' ')}|"
        }
        return stringValue
    }

}
