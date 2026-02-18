import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

val ASCII_KITTENS = """
    /\_/\     /\_/\     /\_/\
   ( o.o )   ( ^.^ )   ( >.< )
    > ^ <     > ^ <     > ^ <
   /|   |\   /|   |\   /|   |\
  (_|   |_) (_|   |_) (_|   |_)
""".trimIndent()

val ASCII_RABBIT = """
   (\(\
   ( -.-)
   o_(")(")
""".trimIndent()

data class CountResult(val count: Int, val exceededLimit: Boolean)

class TextStats : CliktCommand(
    name = "text-stats",
    help = "A tool for analyzing text statistics",
    epilog = ASCII_KITTENS,
    invokeWithoutSubcommand = false
) {
    override fun run() {
        // Parent command just sets up the context
    }
}

class WordsCommand : CliktCommand(
    name = "words",
    help = "Count words in text (words are separated by whitespace)"
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
                throw com.github.ajalt.clikt.core.Abort()
            }
        }

        val result = countWordsWithLimit(text, limit)
        if (result.exceededLimit) {
            echo("Words: more than $limit")
        } else {
            echo("Words: ${result.count}")
        }
    }

    private fun countWordsWithLimit(text: String, limit: Int?): CountResult {
        val words = text.trim()
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }

        if (limit != null && words.size > limit) {
            return CountResult(limit, true)
        }

        return CountResult(words.size, false)
    }
}

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
                throw com.github.ajalt.clikt.core.Abort()
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

fun main(args: Array<String>) = TextStats()
    .subcommands(WordsCommand(), LinesCommand())
    .main(args)