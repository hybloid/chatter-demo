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
    if (args.isEmpty() || args[0] != "words") {
        println(ASCII_KITTENS)
        println()
        println("Usage: app words [--limit <number>] [filePath]")
        println("Commands:")
        println("  words - count words in text")
        println("Options:")
        println("  --limit <number> - stop counting after specified number of words")
        exitProcess(1)
    }
    
    val command = args[0]
    
    when (command) {
        "words" -> {
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

val ASCII_RABBIT = """
   (\(\
   ( -.-)
   o_(")(")
""".trimIndent()