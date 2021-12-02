object Day02 {

    fun part1(input: List<String>): Int {
        var horizontalPosition = 0
        var depth = 0
        input.map { it.trim().split(" ") }
            .forEach {
                when (it[0]) {
                    "forward" -> horizontalPosition += it[1].toInt()
                    "up" -> depth -= it[1].toInt()
                    "down" -> depth += it[1].toInt()
                }
            }
        return horizontalPosition * depth
    }

    fun part2(input: List<String>): Int {

        var aim = 0
        var horizontalPosition = 0
        var depth = 0
        input.map { it.trim().split(" ") }
            .forEach {
                when (it[0]) {
                    "forward" -> {
                        val x = it[1].toInt()
                        horizontalPosition += x
                        depth += aim*x
                    }
                    "up" -> aim -= it[1].toInt()
                    "down" -> aim += it[1].toInt()
                }
            }
        return horizontalPosition * depth
    }
}
