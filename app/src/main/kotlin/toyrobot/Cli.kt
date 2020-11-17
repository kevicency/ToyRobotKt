package toyrobot

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.TermUi.echo
import com.github.ajalt.clikt.parameters.groups.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import java.io.File

class Cli : CliktCommand() {
    val mode by mutuallyExclusiveOptions<Runnable>(
            option("-i", "--interactive").flag().convert { InteractiveMode() },
            option("-f", "--file").file(mustExist = true, canBeFile = true).convert { FileMode(it) }
    )

    override fun run() = mode!!.run()
}

class InteractiveMode : Runnable {
    override fun run() {
        echo("Interactive Mode: Input each command on a separate line, empty line to execute the commands")

        val commands = collectCommandInputs(readCommandInput(), listOf()).map(Command.Parser::parse)

        // TODO: implement
        commands.forEach { it.run() }
    }

    tailrec fun collectCommandInputs(input: String, acc: List<String>): List<String> = when (input) {
        "" -> acc
        else -> collectCommandInputs(readCommandInput(), acc + input)
    }

    fun readCommandInput() = (readLine() ?: "").trim().toUpperCase()
}

class FileMode(val file: File) : Runnable {
    override fun run() {
        echo("File mode: $file")
    }
}

fun main(args: Array<String>) = Cli().main(args)