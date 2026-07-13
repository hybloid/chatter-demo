// pilot rollout header 1
// pilot rollout header 2
// pilot rollout header 3
// pilot rollout header 4
// pilot rollout header 5
import java.io.File
import kotlin.system.exitProcess

val ASCII_KITTENS = """
    /\_/\     /\_/\     /\_/\
   ( o.o )   ( ^.^ )   ( >.< )
    > ^ <     > ^ <     > ^ <
   /|   |\   /|   |\   /|   |\
  (_|   |_) (_|   |_) (_|   |_)
""".trimIndent()

fun main(args: Array<String>) {
    if (args.isEmpty() || (args[0] != "words" && args[0] != "lines")) {
        println(ASCII_KITTENS)
        println()
        println("Usage: app <command> [options] [filePath]")
        println("Commands:")
        println("  words - count words in text")
        println("  lines - count lines in text")
        println("Options:")
        println("  --limit <number> - stop counting after specified number")
        println("Note: Words are separated by whitespace (spaces, tabs, newlines)")
        exitProcess(1)
    }

    val command = args[0]

    when (command) {
        "lines" -> {
            val (limit, filePathIndex) = parseLimitOption(args)

            val text = if (args.size > filePathIndex) {
                // Read from file
                val filePath = args[filePathIndex]
                try {
                    File(filePath).readText()
                } catch (e: Exception) {
                    println("Error reading file: ${e.message}")
                    exitProcess(1)
                }
            } else {
                // Read from stdin
                generateSequence(::readlnOrNull).joinToString("\n")
            }

            val result = countLinesWithLimit(text, limit)
            if (result.exceededLimit) {
                println("Lines: more than $limit")
            } else {
                println("Lines: ${result.count}")
            }
        }
        "words" -> {
            val (limit, filePathIndex) = parseLimitOption(args)

            val text = if (args.size > filePathIndex) {
                // Read from file
                val filePath = args[filePathIndex]
                try {
                    File(filePath).readText()
                } catch (e: Exception) {
                    println("Error reading file: ${e.message}")
                    exitProcess(1)
                }
            } else {
                // Read from stdin
                generateSequence(::readlnOrNull).joinToString("\n")
            }
            
            val result = countWordsWithLimit(text, limit)
            if (result.exceededLimit) {
                println("Words: more than $limit")
            } else {
                println("Words: ${result.count}")
            }
        }
    }
}

data class WordCountResult(val count: Int, val exceededLimit: Boolean)

fun countWordsWithLimit(text: String, limit: Int?): WordCountResult {
    val words = text.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }

    if (limit != null && words.size > limit) {
        return WordCountResult(limit, true)
    }

    return WordCountResult(words.size, false)
}

fun countLinesWithLimit(text: String, limit: Int?): WordCountResult {
    val lines = text.split("\n")

    if (limit != null && lines.size > limit) {
        return WordCountResult(limit, true)
    }

    return WordCountResult(lines.size, false)
}

val ASCII_RABBIT = """
   (\(\
   ( -.-)
   o_(")(")
""".trimIndent()

data class LimitOption(val limit: Int?, val filePathIndex: Int)

fun parseLimitOption(args: Array<String>): LimitOption {
    var limit: Int? = null
    var filePathIndex = 1

    // Parse --limit parameter
    if (args.size > 1 && args[1] == "--limit") {
        if (args.size > 2) {
            try {
                limit = args[2].toInt()
                if (limit <= 0) {
                    println("Error: Limit must be a positive number")
                    exitProcess(1)
                }
                filePathIndex = 3
            } catch (e: NumberFormatException) {
                println("Error: Invalid limit value. Must be a number.")
                exitProcess(1)
            }
        } else {
            println("Error: --limit requires a value")
            exitProcess(1)
        }
    }

    return LimitOption(limit, filePathIndex)
}