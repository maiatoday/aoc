object Day01 {

    fun part1(input: List<String>): Int {
        var increasedCount = 0
        var previousDepth = 0
        input.map { it.toInt()}
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
                if (index > 0 && index<=input.size-2) {
                    val sumDepth = input[index-1].toInt()+input[index].toInt()+input[index+1].toInt()
                    if (sumDepth > previousDepth && index != 1) increasedCount++
                    previousDepth = sumDepth
                }
            }
        return increasedCount
    }
}
