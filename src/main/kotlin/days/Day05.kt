// Day object template
object Day05 {
    private const val x1 = 0
    private const val y1 = 1
    private const val x2 = 2
    private const val y2 = 3

    fun part1(input: List<String>): Int {
        val lines = filterValid90DegreeLines(extractLines(input))
        return HydroThermalMap(lines).dangerPointCount()
    }

    fun part2(input: List<String>): Int {
        val lines = filterValidLines(extractLines(input))
        return HydroThermalMap(lines).dangerPointCount()
    }

    private fun extractLines(input: List<String>): List<List<Int>> =
        input.map { line ->
            line.trim().replace(" -> ", ",")
                .split(",").map { it.toInt() }
        }

    private fun filterValid90DegreeLines(input: List<List<Int>>): List<List<Int>> =
        input.filter {
            (it.size == 4) and
                    (it[x1] == it[x2]) or (it[y1] == it[y2])
        }

    private fun filterValidLines(input: List<List<Int>>): List<List<Int>> =
        input.filter {
            (it.size == 4) // assuming all valid lines
        }

    class HydroThermalMap(private val lines: List<List<Int>>) {

        private val internalMap = List((maxY(lines) ?: 1) + 1) {
            List((maxX(lines) ?: 1) + 1) {
                Cell(0)
            }
        }

        init {
            populateMap(lines)
        }

        private fun populateMap(lines: List<List<Int>>) {
            for (line in lines) {
                val startX = line[x1]
                val endX = line[x2]
                val startY = line[y1]
                val endY = line[y2]
                val length = when {
                    (startX > endX) -> startX - endX
                    (startX < endX) -> endX - startX
                    (startY > endY) -> startY - endY
                    (startY < endY) -> endY - startY
                    else -> 1
                }
                val stepX = if (startX > endX) -1 else if (startX == endX) 0 else 1
                val stepY = if (startY > endY) -1 else if (startY == endY) 0 else 1
                var x = startX
                var y = startY
                for (i in 0..length) {
                    internalMap[y][x].count++
                    x += stepX
                    y += stepY
                }
            }
        }

        private fun maxX(lines: List<List<Int>>) =
            lines.map { listOf(it[x1], it[x2]) }.flatten().maxOrNull()

        private fun maxY(lines: List<List<Int>>) =
            lines.map { listOf(it[y1], it[y2]) }.flatten().maxOrNull()

        fun dangerPointCount(level: Int = 2): Int =
            internalMap.flatten().count { it.count >= level }

        data class Cell(var count: Int)
    }

}
