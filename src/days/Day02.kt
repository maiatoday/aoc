package days

object Day02 : Day<Long, List<String>> {
    override val number: Int = 2
    override val expectedPart1Test: Long = 8L
    override val expectedPart2Test: Long = 2286L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val maxCombo = CubeCombo(12, 13, 14)
        return input.shakeTheBag()
                .filter {
                    it.second.all { combo -> combo <= maxCombo }
                }
                .sumOf {
                    it.first
                }.toLong()
    }

    override fun part2(input: List<String>): Long =
            input.shakeTheBag()
                    .map {
                        it.second
                    }
                    .map { it.findMinimum() }
                    .sumOf { it.power() }.toLong()

    data class CubeCombo(val r: Int = 0, val g: Int = 0, val b: Int = 0) {
        fun power() = r * g * b
    }

    private fun List<String>.shakeTheBag() = this.mapIndexed { i, s ->
        (i + 1) to s.split(":")[1].toRounds().map { it.toCubeCombo() }
    }

    private fun String.toRounds() =
            this.split(";")

    private fun String.toCubeCombo(): CubeCombo =
            this.split(",")
                    .map { it.split(" ") }
                    .map { it.drop(1) }
                    .associate { it[1] to it[0].toInt() }
                    .let {
                        CubeCombo(it["red"] ?: 0, it["green"] ?: 0, it["blue"] ?: 0)
                    }

    private operator fun CubeCombo.compareTo(other: CubeCombo): Int =
            when {
                this == other -> 0
                ((this.r <= other.r) and (this.b <= other.b) and (this.g <= other.g)) -> -1
                ((this.r >= other.r) or (this.b >= other.b) or (this.g >= other.g)) -> 1
                else -> 1
            }

    private fun List<CubeCombo>.findMinimum(): CubeCombo =
            CubeCombo(this.maxOf { it.r }, this.maxOf { it.g }, this.maxOf { it.b })

}


