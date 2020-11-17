package toyrobot

import java.io.PrintStream

abstract class Command {
    abstract fun run(simulation: Simulation)

    companion object Parser {
        operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

        fun parseAll(vararg inputs: String) = inputs.map(::parse)

        fun parse(input: String): Command = when (input) {
            "REPORT" -> ReportCommand()
            "MOVE" -> MoveCommand
            "LEFT" -> LeftCommand
            "RIGHT" -> RightCommand
            in PlaceCommand.regex -> PlaceCommand.parse(input)!!
            else -> InvalidCommand(input)
        }
    }
}

class ReportCommand(val out: PrintStream = System.out) : Command() {
    override fun run(simulation: Simulation) {
        val message = simulation.robot?.stringify()

        if (message != null) {
            out.println(message)
        }
    }
}

object LeftCommand : Command() {
    override fun run(simulation: Simulation) = when (simulation.robot?.facing) {
        Direction.NORTH -> simulation.robot?.facing = Direction.WEST
        Direction.EAST -> simulation.robot?.facing = Direction.NORTH
        Direction.SOUTH -> simulation.robot?.facing = Direction.EAST
        Direction.WEST -> simulation.robot?.facing = Direction.SOUTH
        null -> Unit
    }
}

object RightCommand : Command() {
    override fun run(simulation: Simulation) = when (simulation.robot?.facing) {
        Direction.NORTH -> simulation.robot?.facing = Direction.EAST
        Direction.EAST -> simulation.robot?.facing = Direction.SOUTH
        Direction.SOUTH -> simulation.robot?.facing = Direction.WEST
        Direction.WEST -> simulation.robot?.facing = Direction.NORTH
        null -> Unit
    }
}

object MoveCommand : Command() {
    override fun run(simulation: Simulation) {
        val robot = simulation.robot

        if (robot != null) {
            PlaceCommand(robot.position + robot.facing, robot.facing).run(simulation)
        }
    }
}

class PlaceCommand(val x: Int, val y: Int, val direction: Direction) : Command() {
    constructor(position: Pair<Int, Int>, direction: Direction) : this(position.first, position.second, direction) {}

    companion object {
        val regex = """\APLACE (\d+),(\d+),(NORTH|EAST|SOUTH|WEST)\z""".toRegex()

        fun parse(input: String): PlaceCommand? {
            val match = regex.find(input)

            if (match != null) {
                val (x, y, dir) = match.destructured

                return PlaceCommand(x.toInt(), y.toInt(), Direction.valueOf(dir))
            }

            return null
        }
    }

    override fun run(simulation: Simulation) {
        val position = x to y

        if (position in simulation.board) {
            simulation.robot = Robot(position, direction)
        }
    }
}

class InvalidCommand(val input: String) : Command() {
    override fun run(simulation: Simulation) {
        println("Invalid command: $input")
    }
}