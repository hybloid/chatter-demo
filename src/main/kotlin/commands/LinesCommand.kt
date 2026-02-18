package commands

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int

class LinesCommand : CliktCommand(
    name = "lines",
    help = "Count lines in text"
) {
    private val limit by option("--limit", "-l", help = "Stop counting after specified number")
        .int()

    private val file by argument(help = "File path to analyze (reads from stdin if not provided)")
        .file(mustExist = true, canBeDir = false)
        .optional()

    override fun run() {
        val text = file?.readText() ?: generateSequence(::readlnOrNull).joinToString("\n")

        limit?.let {
            if (it <= 0) {
                echo("Error: Limit must be a positive number", err = true)
                throw Abort()
            }
        }

        val result = countLinesWithLimit(text, limit)
        if (result.exceededLimit) {
            echo("Lines: more than $limit")
        } else {
            echo("Lines: ${result.count}")
        }
    }

    private fun countLinesWithLimit(text: String, limit: Int?): CountResult {
        val lines = text.split("\n")

        if (limit != null && lines.size > limit) {
            return CountResult(limit, true)
        }

        return CountResult(lines.size, false)
    }
}
