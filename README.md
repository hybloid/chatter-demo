# sa/chatter-demo

A command-line text statistics tool written in Kotlin using the [Clikt](https://ajalt.github.io/clikt/) library. This tool analyzes text files and provides word and line counts with optional limits.

## Features

- **Word Count**: Count words in text files (words are separated by whitespace)
- **Line Count**: Count lines in text files
- **Limit Support**: Stop counting after a specified number with `--limit` or `-l` option
- **Stdin Support**: Read from stdin if no file is provided
- **ASCII Art**: Displays cute ASCII kittens in the help output

## Getting Started

### Clone the Repository

```bash
# SSH
git clone ssh://git@git.jetbrains.team/sa/chatter-demo.git

# HTTPS
git clone https://git.jetbrains.team/sa/chatter-demo.git
```

### Prerequisites

- Java JDK 11 or higher
- Gradle (included via wrapper)

## Building the Project

Build the project using Gradle:

```bash
./gradlew build
```

## Running the Application

### Show Help

```bash
./gradlew run --args="--help"
```

### Count Words

```bash
# From file
./gradlew run --args="words path/to/file.txt"

# With limit
./gradlew run --args="words --limit 100 path/to/file.txt"

# From stdin
echo "hello world" | ./gradlew run --args="words"
```

### Count Lines

```bash
# From file
./gradlew run --args="lines path/to/file.txt"

# With limit (short option)
./gradlew run --args="lines -l 50 path/to/file.txt"

# From stdin
cat file.txt | ./gradlew run --args="lines"
```

## Running Tests

Execute the test suite:

```bash
./gradlew test
```

## Project Structure

```
src/
├── main/kotlin/
│   ├── Main.kt              # Entry point with TextStats CLI
│   └── commands/
│       ├── CountResult.kt   # Result data class
│       ├── LinesCommand.kt  # Lines counting command
│       └── WordsCommand.kt  # Words counting command
└── test/kotlin/
    ├── LinesCommandTest.kt  # Tests for lines command
    └── WordsCommandTest.kt  # Tests for words command
```

## Technology Stack

- **Language**: Kotlin
- **CLI Framework**: [Clikt](https://ajalt.github.io/clikt/) - Command-line interface toolkit
- **Build Tool**: Gradle with Kotlin DSL
- **Testing**: JUnit 5 with Kotlin Test

## Development

The project uses Clikt's subcommand architecture, where each command (`words`, `lines`) is implemented as a separate class extending `CliktCommand`. This makes it easy to add new text analysis commands in the future.

```
    (\(\
    ( -.-)
    o_(")(")
```
That's all folks!
Have fun!
Happy coding!
Happy Kotlining!
Yes!