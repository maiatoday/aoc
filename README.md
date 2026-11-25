# Advent of code

Welcome to the Advent of Code[^aoc] Kotlin project created by [maiatoday][github] using the [Advent of Code Kotlin Template][template] delivered by JetBrains.

This repository, has been heavily modified to my workflow. It is still used for solutions using [Kotlin][kotlin] language, however it has some scripts and testing built in and a collections of libraries I built up over the time I have been doing these challenges.

## Getting started for a fresh year

1. Pick the latest starter branch or head of main and make a fresh branch for the year
2. Setup the data download script
   * Add an env variable for `$ADVENT_SESSION` and `$ADVENT_USER_AGENT`
   * Test the script with this command `aoc 1` It should download day 1 data
   * Change the year in the script to point to the correct url
3. Update the Kotlin Toolchain with `./kotlin update`
4. Run `./kotlin test` to see if everything still works
5. There are two options to work, either call the day code in the Main.kt or setup tests in the DayXX test file.

## Building and testing

Run `./kotlin build` or `./kotlin test` (on Windows, use `kotlin.bat`). The wrapper downloads the Kotlin Toolchain and required JDK automatically. In IntelliJ IDEA, install the Kotlin Toolchain plugin instead of the Amper plugin.

## Utils

* There are some generalised utils in utils package. [InputUtils](src/main/kotlin/util/InputUtils.kt) for various input handling, [GridUtils](src/main/kotlin/util/GridUtils.kt) for some point and 3d point utilities.

## Quickstart live code templates
To fill in the two templates you created in IntelliJ IDEA:

1. Open **Settings → Editor → Live Templates**, expand the group where you created them, and select the first template. Set its abbreviation to `aoc` and paste the following into **Template text** (not the whole XML file):

   ```kotlin
   object Day$DAY$ : Day<Long, List<String>> {
       override val number: Int = $NUMBER$
       override val expectedPart1Test: Long = -1L
       override val expectedPart2Test: Long = -1L
       override var useTestData = true
       override val debug = false

       override fun part1(input: List<String>): Long {
           $END$return -1L
       }

       override fun part2(input: List<String>): Long {
           return -1L
       }
   }
   ```

2. Select the second template, set its abbreviation to `taoc`, and paste this into **Template text**:

   ```kotlin
   import util.readInput
   import kotlin.test.Test
   import kotlin.test.assertEquals

   class Day$DAY$Test {
       private val day = Day$DAY$
       private val testInput = readInput(day.number, "Day_test")

       @Test
       fun part1() {
           day.useTestData = true
           assertEquals(day.expectedPart1Test, day.part1(testInput))
       }

       @Test
       fun part2() {
           day.useTestData = true
           assertEquals(day.expectedPart2Test, day.part2(testInput))
       }
       $END$
   }
   ```

3. For each template, click **Define** (or **Change**) under the template text and select **Kotlin → Top-level**. Set **Expand with** to Tab, then click **Apply** or **OK**. In **Edit variables**, leave expressions and defaults blank so you can enter the values when expanding the template.

Alternatively, the shared definitions are in `ide/live-templates/Advent of Code.xml`. To install that file instead, close IntelliJ IDEA, copy it into the `templates` directory inside your [IDE configuration directory](https://www.jetbrains.com/help/idea/directories-used-by-the-ide.html#config-directory), and restart the IDE. Avoid creating duplicate `aoc` and `taoc` templates if you use this method.

Each day:
1. Create an empty Kotlin file such as `src/days/Day01.kt` in package `days`. After the package declaration, type `aoc` and press Tab. Enter the zero-padded class suffix (for example `01`) and the numeric day (`1`).
2. Create an empty Kotlin file such as `test/days/Day01Test.kt` in package `days`. After the package declaration, type `taoc` and press Tab, then enter the same class suffix. The template supplies the imports and both tests.
3. Download the input and test data, set the expected test answers, and solve the puzzle.

More info
- [Kotlin docs][docs]
- [Kotlin Slack][slack]
- Template [issue tracker][issues]


[^aoc]:
    [Advent of Code][aoc] – an annual event in December since 2015.
    Every year since then, with the first day of December, a programming puzzles contest is published every day for twenty-four days.
    A set of Christmas-oriented challenges provide any input you have to use to answer using the language of your choice.

[aoc]: https://adventofcode.com
[docs]: https://kotlinlang.org/docs/home.html
[github]: https://github.com/maiatoday
[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues
[kotlin]: https://kotlinlang.org
[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up
[template]: https://github.com/kotlinhandson/advent-of-code-kotlin-template
