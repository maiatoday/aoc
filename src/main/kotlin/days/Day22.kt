// Day object template
object Day22 {

    fun part1(input: List<String>): Long {
        val instructions = readInstructions(input).filter { it.inRange(-50, 50) }
        val cubes = runInstructionsNaive(instructions)
        return cubes.size.toLong()
    }

    private fun runInstructionsNaive(instructions: List<Instruction>): Set<Cuboid> {
        val cubes = mutableSetOf<Cuboid>()
        for (i in instructions) {
            for (x in i.xBounds) for (y in i.yBounds) for (z in i.zBounds) {
                if (i.on) {
                    cubes.add(Cuboid(x, y, z))
                } else {
                    cubes.remove(Cuboid(x, y, z))
                }
            }
        }
        return cubes
    }

    private fun readInstructions(input: List<String>): List<Instruction> {
        val ii = input.map { s ->
            val on = s.startsWith("on")
            val coords = s.split(" ")[1].split(",")
                .map { cc ->
                    cc.drop(2).split("..").map { it.toInt() }
                }
            Instruction(on, coords)
        }
        return ii
    }

    class Instruction(val on: Boolean, private val bounds: List<Range>) {

        fun inRange(rangeBottom: Int, rangeTop: Int): Boolean =
            bounds.find { it[0] < rangeBottom || it[1] > rangeTop }?.isEmpty() ?: true

        val xBounds = bounds[0][0]..bounds[0][1]
        val yBounds = bounds[1][0]..bounds[1][1]
        val zBounds = bounds[2][0]..bounds[2][1]

        fun inBounds(x: Int, y: Int, z: Int): Boolean =
            (x in xBounds) && (y in yBounds) && (z in zBounds)

    }

    fun part2(input: List<String>): Long {
        val instructions = readInstructions(input)//.filter { it.inRange(-50, 50) }
        val onCount = runInstructions(instructions)
        return onCount
    }

    private fun runInstructions(instructions: List<Instruction>): Long {
        val (xMin, yMin, zMin) = findLowest(instructions)
        val (xMax, yMax, zMax) = findHighest(instructions)
        var count = 0L
        for (ox in xMin..xMax)
            for (oy in yMin..yMax)
                for (oz in zMin..zMax) {
                    count += parseInstructions(instructions, Triple(ox, oy, oz))
                }
        return count
    }

    private fun parseInstructions(
        instructions: List<Instruction>,
        coord: Triple<Int, Int, Int>,
    ): Long {
        var b = false
        for (i in instructions) {
            b = if (i.on) {
                b or i.inBounds(coord.first, coord.second, coord.third)
            } else {
                b and (!i.inBounds(coord.first, coord.second, coord.third))
            }
        }
        return if (b) 1 else 0
    }

    private fun findLowest(instructions: List<Instruction>): Triple<Int, Int, Int> {
        val lowX = instructions.minOfOrNull { it.xBounds.first } ?: 0
        val lowY = instructions.minOfOrNull { it.yBounds.first } ?: 0
        val lowZ = instructions.minOfOrNull { it.zBounds.first } ?: 0
        return Triple(lowX, lowY, lowZ)
    }

    private fun findHighest(instructions: List<Instruction>): Triple<Int, Int, Int> {
        val highX = instructions.maxOfOrNull { it.xBounds.last } ?: 0
        val highY = instructions.maxOfOrNull { it.yBounds.last } ?: 0
        val highZ = instructions.maxOfOrNull { it.zBounds.last } ?: 0
        return Triple(highX, highY, highZ)
    }
}

typealias Cuboid = Triple<Int, Int, Int>
typealias Range = List<Int>
