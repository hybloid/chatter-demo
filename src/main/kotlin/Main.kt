import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import commands.LinesCommand
import commands.WordsCommand

val ASCII_KITTENS = """
    /\_/\     /\_/\     /\_/\
   ( o.o )   ( ^.^ )   ( >.< )
    > ^ <     > ^ <     > ^ <
   /|   |\   /|   |\   /|   |\
  (_|   |_) (_|   |_) (_|   |_)
""".trimIndent()

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

fun main(args: Array<String>) = TextStats()
    .subcommands(WordsCommand(), LinesCommand())
    .main(args)
