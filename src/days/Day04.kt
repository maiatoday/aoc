package days

object Day04 : Day<Long, List<String>> {
    override val number: Int = 4
    override val expectedPart1Test: Long = 18L
    override val expectedPart2Test: Long = 9L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        var count = 0
        for (i in input[0].indices) for (j in input.indices) {
            count += input.countSmallGrid(i, j)
        }
        return count.toLong()
    }

    override fun part2(input: List<String>): Long {
        var count = 0
        for (i in input[0].indices) for (j in input.indices) {
            count += input.countXGrid(i, j)
        }
        return count.toLong()
    }

    private fun List<String>.countSmallGrid(i: Int, j: Int): Int {
        var count = 0
        // check row
        if (i < this[0].length - 3) {
            val r = this[j].substring(i, i + 4) // substring doesn't include endIndex
            if (r.isXmas()) count++
        }

        // check column
        if ((j < this.size - 3)) {
            if ("${this[j][i]}${this[j + 1][i]}${this[j + 2][i]}${this[j + 3][i]}".isXmas()) count++
        }

        // check diagonals
        if ((i < this[0].length - 3) && (j < this.size - 3)) {
            val diagonal1 = "${this[j][i]}${this[j + 1][i + 1]}${this[j + 2][i + 2]}${this[j + 3][i + 3]}"
            val diagonal2 = "${this[j + 3][i]}${this[j + 2][i + 1]}${this[j + 1][i + 2]}${this[j][i + 3]}"
            if (diagonal1.isXmas()) count++
            if (diagonal2.isXmas()) count++
        }
        return count
    }

    private fun List<String>.countXGrid(i: Int, j: Int): Int {
        var count = 0
        // check diagonals
        if ((i < this[0].length - 2) && (j < this.size - 2)) {
            val diagonal1 = "${this[j][i]}${this[j + 1][i + 1]}${this[j + 2][i + 2]}"
            val diagonal2 = "${this[j + 2][i]}${this[j + 1][i + 1]}${this[j][i + 2]}"
            if (diagonal1.isCrossMas() && diagonal2.isCrossMas()) count++
        }
        return count
    }

    private fun String.isXmas() = this == "XMAS" || this == "SAMX"
    private fun String.isCrossMas() = this == "MAS" || this == "SAM"
}