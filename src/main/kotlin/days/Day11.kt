object Day11 {

    fun part1(input: List<String>): Long {
        val octoMap = parse(input)
        var flashes = 0L
        val steps = 100
        for (s in 1..steps) {
            energyUp(octoMap)
            flashes += flashCountReset(octoMap)
        }
        return flashes
    }

    private fun parse(input: List<String>) =
        input.map { it.toCharArray().map { c -> c.toString().toInt() }.toMutableList() }

    private fun energyUp(octoMap: List<MutableList<Int>>) {
        for (y in octoMap.indices) {
            for (x in octoMap.first().indices) {
                energyUpOne(octoMap, y, x)
            }
        }
    }

    private fun energyUpOne(octoMap: List<MutableList<Int>>, y: Int, x: Int) {
        val isFlash = octoMap[y][x] == 9
        octoMap[y][x] += 1
        if (isFlash) {
            neighbours(octoMap, y, x).forEach {
                energyUpOne(octoMap, it.first, it.second)
            }
        }
    }

    private fun flashCountReset(octoMap: List<MutableList<Int>>): Long {
        var count = 0L
        for (y in octoMap.indices) {
            for (x in octoMap.first().indices) {
                if (octoMap[y][x] >= 10) {
                    octoMap[y][x] = 0
                    count++
                }
            }
        }
        return count
    }

    private fun neighbours(octoMap: List<MutableList<Int>>, y: Int, x: Int): List<Pair<Int, Int>> {
        val maxY = octoMap.size
        val maxX = octoMap.first().size
        return buildList {
            /*
            x-1,y-1   x,y-1    x+1,y-1
            x-1,y              x+1,y
            x-1,y+1   x,y+1    x+1,y+1
             */
            val xStart = if (x > 0) x - 1 else x
            val xEnd = if (x == maxX - 1) x else x + 1
            val yStart = if (y > 0) y - 1 else y
            val yEnd = if (y == maxY - 1) y else y + 1
            for (ny in yStart..yEnd) {
                for (nx in xStart..xEnd) {
                    if (octoMap[ny][nx] < 10)
                        add(Pair(ny, nx))
                }
            }
        }
    }

    fun part2(input: List<String>): Long {
        val octoMap = parse(input)
        var step = 0
        while (!allFlash(octoMap)) {
            energyUp(octoMap)
            flashCountReset(octoMap)
            step++
        }
        return step.toLong()
    }

    private fun allFlash(octoMap: List<MutableList<Int>>): Boolean =
        octoMap.flatten().all { it == 0 }
}

