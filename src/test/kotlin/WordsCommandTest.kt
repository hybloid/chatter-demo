import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains

class WordsCommandTest {

    @TempDir
    lateinit var tempDir: Path

    private fun createTempFile(content: String): File {
        val file = tempDir.resolve("input.txt").toFile()
        file.writeText(content)
        return file
    }

    private fun runWords(vararg args: String): com.github.ajalt.clikt.testing.CliktCommandTestResult {
        return TextStats().subcommands(WordsCommand(), LinesCommand()).test(listOf("words") + args.toList())
    }

    @Test
    fun `counts words in simple text`() {
        val file = createTempFile("hello world")
        val result = runWords(file.absolutePath)
        assertEquals("Words: 2\n", result.stdout)
        assertEquals(0, result.statusCode)
    }

    @Test
    fun `counts single word`() {
        val file = createTempFile("hello")
        val result = runWords(file.absolutePath)
        assertEquals("Words: 1\n", result.stdout)
    }

    @Test
    fun `counts words separated by various whitespace`() {
        val file = createTempFile("hello\tworld\nfoo  bar")
        val result = runWords(file.absolutePath)
        assertEquals("Words: 4\n", result.stdout)
    }

    @Test
    fun `empty file has zero words`() {
        val file = createTempFile("")
        val result = runWords(file.absolutePath)
        assertEquals("Words: 0\n", result.stdout)
    }

    @Test
    fun `whitespace only file has zero words`() {
        val file = createTempFile("   \t\n  ")
        val result = runWords(file.absolutePath)
        assertEquals("Words: 0\n", result.stdout)
    }

    @Test
    fun `limit less than word count shows exceeded`() {
        val file = createTempFile("one two three four five")
        val result = runWords("--limit", "3", file.absolutePath)
        assertEquals("Words: more than 3\n", result.stdout)
    }

    @Test
    fun `limit equal to word count shows exact count`() {
        val file = createTempFile("one two three")
        val result = runWords("--limit", "3", file.absolutePath)
        assertEquals("Words: 3\n", result.stdout)
    }

    @Test
    fun `limit greater than word count shows exact count`() {
        val file = createTempFile("one two")
        val result = runWords("--limit", "10", file.absolutePath)
        assertEquals("Words: 2\n", result.stdout)
    }

    @Test
    fun `limit of zero produces error`() {
        val file = createTempFile("hello")
        val result = runWords("--limit", "0", file.absolutePath)
        assertContains(result.stderr, "Error: Limit must be a positive number")
        assertEquals(1, result.statusCode)
    }

    @Test
    fun `negative limit produces error`() {
        val file = createTempFile("hello")
        val result = runWords("--limit", "-1", file.absolutePath)
        assertContains(result.stderr, "Error: Limit must be a positive number")
        assertEquals(1, result.statusCode)
    }

    @Test
    fun `short limit option works`() {
        val file = createTempFile("one two three four five")
        val result = runWords("-l", "2", file.absolutePath)
        assertEquals("Words: more than 2\n", result.stdout)
    }

    @Test
    fun `multiple lines counted correctly`() {
        val file = createTempFile("line one\nline two\nline three")
        val result = runWords(file.absolutePath)
        assertEquals("Words: 6\n", result.stdout)
    }
}
