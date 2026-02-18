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

fun summonCodeGoblin(): String = "The code goblin reviewed your PR and demanded exactly 17 snacks."

fun askRubberDuckForHelp(): String = "The rubber duck said: 'Try turning your bugs off and on again.'"

fun consultSemicolonOracle(): String = "The semicolon oracle whispered: 'Kotlin forgives you... this time.'"

fun threatenTheBuildCache(): String = "I threatened the build cache and suddenly all tests passed out of fear."

fun bribeTheCompiler(): String = "I offered the compiler a coffee, and it only returned warnings this time."

fun debugByInterpretiveDance(): String = "I fixed the bug with interpretive dance; now the stack trace applauds."

fun refactorAt2AM(): String = "I refactored at 2 AM and invented three new bugs with premium features."

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
