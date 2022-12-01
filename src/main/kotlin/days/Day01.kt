// Day object template
object Day01 {

    fun part1(input: List<String>): Long {
        var maxCalories = Int.MIN_VALUE
        var elfNumber = 1
        var runningTotal = 0
        for (line in input) {
            if (line.isEmpty()) {
                // new elf
                if (maxCalories < runningTotal) maxCalories = runningTotal
                runningTotal = 0
                elfNumber++
            } else {
                runningTotal += line.toInt()
            }
        }
        return maxCalories.toLong()
    }

    fun part2(input: List<String>): Long {
        val elfRations = mutableListOf<Int>()
        var runningTotal = 0
        for (line in input) {
            if (line.isEmpty()) {
                elfRations.add(runningTotal)
                runningTotal = 0
            } else {
                runningTotal += line.toInt()
            }
        }
        elfRations.add(runningTotal)
        return elfRations.sortedDescending().take(3).sum().toLong()
    }
}
