package toyrobot

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.TermUi.echo
import com.github.ajalt.clikt.parameters.groups.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import java.io.File
import java.util.*

class Cli : CliktCommand() {
    val mode by mutuallyExclusiveOptions<CliMode>(
            option("-i", "--interactive").flag().convert { InteractiveCliMode() },
            option("-f", "--file").file(mustExist = true, canBeFile = true).convert { FileCliMode(it.readLines()) }
    )

    override fun run() = mode!!.run()
}

abstract class CliMode : Runnable {
    override fun run() {
        val commands = getCommandList()

        commands.fold(Simulation()) { simulation, command ->
            simulation.also { command.run(it) }
        }

        readLine()
    }

    abstract fun getCommandList(): List<Command>
}

class InteractiveCliMode : CliMode() {
    override fun getCommandList(): List<Command> {
        echo("Interactive Mode: Input each command on a separate line, empty line to execute the commands")

        return collectCommandInputs(readCommandInput(), listOf())
                .map(Command.Parser::parse)
    }

    tailrec fun collectCommandInputs(input: String, acc: List<String>): List<String> = when (input) {
        "" -> acc
        else -> collectCommandInputs(readCommandInput(), acc + input)
    }

    fun readCommandInput(): String = (readLine() ?: "").trim().toUpperCase()
}

class FileCliMode(val lines: List<String>) : CliMode() {
    override fun getCommandList() = lines.map(Command.Parser::parse)
}

fun main(args: Array<String>) = Cli().main(args)