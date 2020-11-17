package toyrobot

abstract class Command {
    abstract fun run()

    companion object Parser {
        operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

        fun parseAll(vararg inputs: String) = inputs.map(::parse)

        fun parse(input: String): Command = when (input) {
            "REPORT" -> ReportCommand
            "MOVE" -> MoveCommand
            "LEFT" -> LeftCommand
            "RIGHT" -> RightCommand
            in PlaceCommand.regex -> PlaceCommand.parse(input)!!
            else -> InvalidCommand(input)
        }
    }
}

object ReportCommand : Command() {
    override fun run() {
        println("REPORT")
    }
}

object LeftCommand : Command() {
    override fun run() {
        println("LEFT")
    }
}

object RightCommand : Command() {
    override fun run() {
        println("RIGHT")
    }
}

object MoveCommand : Command() {
    override fun run() {
        println("MOVE")
    }
}

class PlaceCommand(val x: Int, val y: Int, val facing: Direction) : Command() {
    companion object {
        val regex = """\APLACE (\d+),(\d+),(NORTH|EAST|SOUTH|WEST)\z""".toRegex()

        fun parse(input: String): PlaceCommand? {
            val match = regex.find(input)

            if (match != null) {
                val (x, y, dir) = match.destructured

                return PlaceCommand(x.toInt(), y.toInt(), Direction.valueOf(dir.capitalize()))
            }

            return null
        }
    }

    override fun run() {
        println("PLACE $x,$y,$facing")
    }
}

class InvalidCommand(val input: String) : Command() {
    override fun run() {
        println("Invalid command: $input")
    }
}