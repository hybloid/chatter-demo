import commands.LinesCommand
import commands.WordsCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains

class LinesCommandTest {

    @TempDir
    lateinit var tempDir: Path

    private fun createTempFile(content: String): File {
        val file = tempDir.resolve("input.txt").toFile()
        file.writeText(content)
        return file
    }

    private fun runLines(vararg args: String): com.github.ajalt.clikt.testing.CliktCommandTestResult {
        return TextStats().subcommands(WordsCommand(), LinesCommand()).test(listOf("lines") + args.toList())
    }

    @Test
    fun `single line`() {
        val file = createTempFile("hello world")
        val result = runLines(file.absolutePath)
        assertEquals("Lines: 1\n", result.stdout)
        assertEquals(0, result.statusCode)
    }

    @Test
    fun `multiple lines`() {
        val file = createTempFile("line1\nline2\nline3")
        val result = runLines(file.absolutePath)
        assertEquals("Lines: 3\n", result.stdout)
    }

    @Test
    fun `empty string counts as one line`() {
        val file = createTempFile("")
        val result = runLines(file.absolutePath)
        assertEquals("Lines: 1\n", result.stdout)
    }

    @Test
    fun `trailing newline adds a line`() {
        val file = createTempFile("line1\nline2\n")
        val result = runLines(file.absolutePath)
        assertEquals("Lines: 3\n", result.stdout)
    }

    @Test
    fun `limit less than line count shows exceeded`() {
        val file = createTempFile("a\nb\nc\nd\ne")
        val result = runLines("--limit", "3", file.absolutePath)
        assertEquals("Lines: more than 3\n", result.stdout)
    }

    @Test
    fun `limit equal to line count shows exact count`() {
        val file = createTempFile("a\nb\nc")
        val result = runLines("--limit", "3", file.absolutePath)
        assertEquals("Lines: 3\n", result.stdout)
    }

    @Test
    fun `limit greater than line count shows exact count`() {
        val file = createTempFile("a\nb")
        val result = runLines("--limit", "10", file.absolutePath)
        assertEquals("Lines: 2\n", result.stdout)
    }

    @Test
    fun `limit of zero produces error`() {
        val file = createTempFile("hello")
        val result = runLines("--limit", "0", file.absolutePath)
        assertContains(result.stderr, "Error: Limit must be a positive number")
        assertEquals(1, result.statusCode)
    }

    @Test
    fun `negative limit produces error`() {
        val file = createTempFile("hello")
        val result = runLines("--limit", "-1", file.absolutePath)
        assertContains(result.stderr, "Error: Limit must be a positive number")
        assertEquals(1, result.statusCode)
    }

    @Test
    fun `short limit option works`() {
        val file = createTempFile("a\nb\nc\nd")
        val result = runLines("-l", "2", file.absolutePath)
        assertEquals("Lines: more than 2\n", result.stdout)
    }

    @Test
    fun `only newlines`() {
        val file = createTempFile("\n\n\n")
        val result = runLines(file.absolutePath)
        assertEquals("Lines: 4\n", result.stdout)
    }
}
