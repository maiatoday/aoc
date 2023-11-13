# Advent of code

Welcome to the Advent of Code[^aoc] Kotlin project created by [maiatoday][github] using the [Advent of Code Kotlin Template][template] delivered by JetBrains.

This repository, has been heavily modified to my workflow. It is still used for solutions using [Kotlin][kotlin] language, however it has some scripts and testing built in and a collections of libraries I built up over the time I have been doing these challenges.

## Getting started for a fresh year

1. Pick the latest starter branch or head of main and make a fresh branch for the year
2. Setup the data download script
   * Add an env variable for `$ADVENT_SESSION` and `$ADVENT_USER_AGENT`
   * Test the script with this command `aoc 1` It should download day 1 data
   * Change the year in the script to point to the correct url
3. Update any Kotlin versions or gradle versions
4. Run some tests to see if everything still works
5. There are two options to work, either call the day code in the Main.kt or setup tests in the DayXX test file.

## Using benchmarks

* The build should be setup to use benchmarks, there is a test that shows how to run these.
* Run the benchmarks with `./gradlew benchmark`

## Utils

* There are some generalised utils in utils package. [InputUtils](src/main/kotlin/util/InputUtils.kt) for various input handling, [GridUtils](src/main/kotlin/util/GridUtils.kt) for some point and 3d point utilities.

## Quickstart live code templates
The steps each day:
1. Setup the day code with new file Dayxx and live template `aoc`
2. Generate a test file and use `taoc` live template, populate imports
2. Download test data and read question and get cracking to submit for ⭐️⭐️

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
