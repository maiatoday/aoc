object Day01 {

    fun part1(input: List<String>): Int {
        var increasedCount = 0
        var previousDepth = 0
        input.map { it.toInt() }
            .forEachIndexed { index, depth ->
                if (index > 0) {
                    if (depth > previousDepth) increasedCount++
                }
                previousDepth = depth
            }
        return increasedCount
    }

    fun part2(input: List<String>): Int {
        var increasedCount = 0
        var previousDepth = 0
        input.forEachIndexed { index, _ ->
            if (index > 0 && index <= input.size - 2) {
                val sumDepth = input[index - 1].toInt() + input[index].toInt() + input[index + 1].toInt()
                if (sumDepth > previousDepth && index != 1) increasedCount++
                previousDepth = sumDepth
            }
        }
        return increasedCount
    }

    fun part1Idiomatic(input: List<String>, windowSize: Int = 2): Int =
        input.map { it.toInt() }
            .windowed(windowSize).count { it[0] < it[1] }

    fun part2Idiomatic(input: List<String>, windowSize: Int = 3): Int =
        input.map { it.toInt() }
            .windowed(windowSize)
            .map {it.sum()}
            .windowed(2)
            .count { it[0] < it[1] }

}
