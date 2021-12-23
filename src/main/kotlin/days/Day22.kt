object Day22 {

    fun part1(input: List<String>): Long {
        val instructions = readInstructions(input).filter { it.inRange(-50, 50) }
        val count = runInstructions(instructions)
        return count
    }

    private fun readInstructions(input: List<String>): List<Instruction> {
        val ii = input.map { s ->
            val on = s.startsWith("on")
            val coords = s.split(" ")[1].split(",")
                .map { cc ->
                    cc.drop(2).split("..").map { it.toLong() }
                }
            Instruction(on, coords)
        }
        return ii
    }

    class Instruction(val on: Boolean, private val bounds: List<Range>) {

        fun inRange(rangeBottom: Int, rangeTop: Int): Boolean =
            bounds.find { it[0] < rangeBottom || it[1] > rangeTop }?.isEmpty() ?: true

        val xBounds = bounds[0][0]..bounds[0][1]+1
        val yBounds = bounds[1][0]..bounds[1][1]+1
        val zBounds = bounds[2][0]..bounds[2][1]+1

    }

    fun part2(input: List<String>): Long {
        val instructions = readInstructions(input)//.filter { it.inRange(-50, 50) }
        return runInstructions(instructions)
    }

    private fun runInstructions(instructions: List<Instruction>): Long {
        val (xSeams, ySeams, zSeams) = getSeams(instructions)
        val mondrianCube = makeMondrianCube(xSeams.size, ySeams.size, zSeams.size).also {
            parseInstructions(instructions, it, xSeams, ySeams, zSeams)
        }
        val count = countLights(mondrianCube, xSeams, ySeams, zSeams)
        return count
    }

    private fun getSeams(instructions: List<Instruction>): Seams {
        val xSeams = instructions.map { listOf(it.xBounds.first, it.xBounds.last) }.flatten().distinct().sorted()
        val ySeams = instructions.map { listOf(it.yBounds.first, it.yBounds.last) }.flatten().distinct().sorted()
        val zSeams = instructions.map { listOf(it.zBounds.first, it.zBounds.last) }.flatten().distinct().sorted()
        return Seams(xSeams, ySeams, zSeams)
    }

    private fun makeMondrianCube(
        xSize: Int,
        ySize: Int,
        zSize: Int,
    ): Array<Array<Array<Boolean>>> =
        Array(zSize) { Array(ySize) { Array(xSize) { false } } }


    private fun parseInstructions(
        instructions: List<Instruction>,
        mondrianCube: Array<Array<Array<Boolean>>>,
        xSeams: List<Long>,
        ySeams: List<Long>,
        zSeams: List<Long>,
    ) {
        for (ii in instructions) {
            val xRange = xSeams.indexOf(ii.xBounds.first) until xSeams.indexOf(ii.xBounds.last)
            val yRange = ySeams.indexOf(ii.yBounds.first) until ySeams.indexOf(ii.yBounds.last)
            val zRange = zSeams.indexOf(ii.zBounds.first) until zSeams.indexOf(ii.zBounds.last)
            for (z in zRange) for (y in yRange) for (x in xRange) {
                mondrianCube[z][y][x] = ii.on
            }
        }
    }

    private fun countLights(
        mondrianCube: Array<Array<Array<Boolean>>>,
        xSeams: List<Long>,
        ySeams: List<Long>,
        zSeams: List<Long>
    ): Long {
        var count = 0L
        for (z in 0 until mondrianCube.size -1)
            for (y in 0 until mondrianCube[0].size -1)
                for (x in 0 until mondrianCube[0][0].size -1) {
                    if (mondrianCube[z][y][x]) {
                        val xSize = xSeams[x + 1] - xSeams[x]
                        val ySize = ySeams[y + 1] - ySeams[y]
                        val zSize = zSeams[z + 1] - zSeams[z]
                        count += xSize * ySize * zSize
                    }
                }
        return count
    }

    data class Seams(
        val xSeams: List<Long>,
        val ySeams: List<Long>,
        val zSeams: List<Long>,
    )
}

typealias Range = List<Long>
