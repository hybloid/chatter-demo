import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty() || args[0] != "words") {
        println("Usage: app words [filePath]")
        println("Commands:")
        println("  words - count words in text")
        exitProcess(1)
    }
    
    val command = args[0]
    
    when (command) {
        "words" -> {
            val text = if (args.size > 1) {
                // Read from file
                val filePath = args[1]
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
            
            val wordCount = countWords(text)
            println("Words: $wordCount")
        }
    }
}

fun countWords(text: String): Int {
    return text.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .size
}