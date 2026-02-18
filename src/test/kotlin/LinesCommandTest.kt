import commands.LinesCommand
import commands.WordsCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.testing.CliktCommandTestResult
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains

/**
 * Refactored test class for LinesCommand with the following improvements:
 * - Added helper methods to reduce code duplication (assertLinesOutput, assertError)
 * - Extracted error message to constant for consistency
 * - Organized tests into logical sections with comments
 * - Improved type readability by using imported CliktCommandTestResult instead of fully qualified name
 */
class LinesCommandTest {

    @TempDir
    lateinit var tempDir: Path

    private fun createTempFile(content: String): File {
        val file = tempDir.resolve("input.txt").toFile()
        file.writeText(content)
        return file
    }

    private fun runLines(vararg args: String): CliktCommandTestResult {
        return TextStats().subcommands(WordsCommand(), LinesCommand()).test(listOf("lines") + args.toList())
    }

    /**
     * Helper method to assert successful lines output.
     * Reduces duplication of assertEquals calls for stdout and status code.
     */
    private fun assertLinesOutput(expected: String, result: CliktCommandTestResult) {
        assertEquals(expected, result.stdout)
        assertEquals(0, result.statusCode)
    }

    /**
     * Helper method to assert error conditions.
     * Reduces duplication of assertContains and assertEquals calls for error cases.
     */
    private fun assertError(expectedMessage: String, result: CliktCommandTestResult) {
        assertContains(result.stderr, expectedMessage)
        assertEquals(1, result.statusCode)
    }

    companion object {
        // Extracted constant to eliminate duplication of error message across multiple tests
        private const val LIMIT_ERROR_MESSAGE = "Error: Limit must be a positive number"
    }

    // Basic line counting tests
    @Test
    fun `single line`() {
        val file = createTempFile("hello world")
        val result = runLines(file.absolutePath)
        assertLinesOutput("Lines: 1\n", result)
    }

    @Test
    fun `multiple lines`() {
        val file = createTempFile("line1\nline2\nline3")
        val result = runLines(file.absolutePath)
        assertLinesOutput("Lines: 3\n", result)
    }

    @Test
    fun `empty string counts as one line`() {
        val file = createTempFile("")
        val result = runLines(file.absolutePath)
        assertLinesOutput("Lines: 1\n", result)
    }

    @Test
    fun `trailing newline adds a line`() {
        val file = createTempFile("line1\nline2\n")
        val result = runLines(file.absolutePath)
        assertLinesOutput("Lines: 3\n", result)
    }

    @Test
    fun `only newlines`() {
        val file = createTempFile("\n\n\n")
        val result = runLines(file.absolutePath)
        assertLinesOutput("Lines: 4\n", result)
    }

    // Limit functionality tests
    @Test
    fun `limit less than line count shows exceeded`() {
        val file = createTempFile("a\nb\nc\nd\ne")
        val result = runLines("--limit", "3", file.absolutePath)
        assertLinesOutput("Lines: more than 3\n", result)
    }

    @Test
    fun `limit equal to line count shows exact count`() {
        val file = createTempFile("a\nb\nc")
        val result = runLines("--limit", "3", file.absolutePath)
        assertLinesOutput("Lines: 3\n", result)
    }

    @Test
    fun `limit greater than line count shows exact count`() {
        val file = createTempFile("a\nb")
        val result = runLines("--limit", "10", file.absolutePath)
        assertLinesOutput("Lines: 2\n", result)
    }

    @Test
    fun `short limit option works`() {
        val file = createTempFile("a\nb\nc\nd")
        val result = runLines("-l", "2", file.absolutePath)
        assertLinesOutput("Lines: more than 2\n", result)
    }

    // Error handling tests
    @Test
    fun `limit of zero produces error`() {
        val file = createTempFile("hello")
        val result = runLines("--limit", "0", file.absolutePath)
        assertError(LIMIT_ERROR_MESSAGE, result)
    }

    @Test
    fun `negative limit produces error`() {
        val file = createTempFile("hello")
        val result = runLines("--limit", "-1", file.absolutePath)
        assertError(LIMIT_ERROR_MESSAGE, result)
    }
}
