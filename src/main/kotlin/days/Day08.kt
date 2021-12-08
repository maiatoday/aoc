// Day object template
object Day08 {

    /*
  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg
     */
    val segments = mapOf(
        0 to 6, // 1, !4, 7, !8  -- all of 1 is in 0, all of 7 is in 0
        1 to 2, // unique
        2 to 5, // !1, !4, !7, !8
        3 to 5, // 1, !4, 7, !8
        4 to 4, // unique
        5 to 5, // !1, !4, !7, !8
        6 to 6, // !1, !4, !7, !8 **** 6digit doesn't contain 1 4 7
        7 to 3, // unique
        8 to 7, // unique
        9 to 6, // 1, 4, 7, !8 **** 6 digit contains 1 4 7
    )

    fun part1(input: List<String>): Int {
        val digits = input.map { it.split("|") }.map { item ->
            item[1]
        }
        val uniqueCounts = listOf(2, 4, 3, 7)
        return digits.flatMap { digit ->
            digit.trim().split(" ").map {
                it.length
            }
        }.count { uniqueCounts.contains(it) }
    }

    fun part2(input: List<String>): Int {
        val signalDigits = input.map { it.split("|") }
        val signals = signalDigits.map { it.first().trim().split(" ") }
        val digits = signalDigits.map { it[1].trim().split(" ") }
        val maps = signals.map { decodeMap(it) }
        val values = maps.zip(digits) { map, dig -> decode(dig, map) }
        return values.sum()
    }

    fun decode(digits: List<String>, map: Map<String, String>): Int =
        digits.map { map[it.alphabetized()] }.joinToString(separator = "").toInt()

    fun decodeMap(signals: List<String>): Map<String, String> {
        val one = (signals.find { it.length == 2 } ?: "-")
        val four = (signals.find { it.length == 4 } ?: "-")
        val seven = (signals.find { it.length == 3 } ?: "-")
        val eight = (signals.find { it.length == 7 } ?: "-")
        val nine = (signals.find {
            (it.length == 6) and
                    allMatch(it, one) and
                    allMatch(it, four) and
                    allMatch(it, seven)
        } ?: "-")
        val zero = (signals.find {
            (it.length == 6) and
                    allMatch(it, one) and
                    !allMatch(it, four) and
                    allMatch(it, seven)
        } ?: "-")
        val six = (signals.find {
            (it.length == 6) and
                    !allMatch(it, one) and
                    !allMatch(it, four) and
                    !allMatch(it, seven)
        } ?: "-")

        val three = (signals.find {
            (it.length == 5) and
                    allMatch(it, one) and
                    !allMatch(it, four) and
                    allMatch(it, seven)
        } ?: "-")

        val five = (signals.find {
            (it.length == 5) and
                    !allMatch(it, one) and
                    !allMatch(it, four) and
                    !allMatch(it, seven) and
                    allMatch(nine, it)
        } ?: "-")

        val two = (signals.find {
            (it.length == 5) and
                    (it != three) and
                    (it != five)
        } ?: "-")

        return mapOf(
            zero.alphabetized() to "0",
            one.alphabetized() to "1",
            two.alphabetized() to "2",
            three.alphabetized() to "3",
            four.alphabetized() to "4",
            five.alphabetized() to "5",
            six.alphabetized() to "6",
            seven.alphabetized() to "7",
            eight.alphabetized() to "8",
            nine.alphabetized() to "9"
        )
    }

    fun allMatch(first: String, charsToMatch: String): Boolean =
        charsToMatch == charsToMatch.filter { first.contains(it) }

    fun String.alphabetized() = String(toCharArray().apply { sort() })

}
