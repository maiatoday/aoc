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
        val (xSeams, ySeams, zSeams) = getSeams(instructions)
        val mondrianCube = makeMondrianCube(xSeams, ySeams, zSeams).also {
            parseInstructions(instructions, it, xSeams, ySeams, zSeams)
        }
        val count = countLights(mondrianCube, xSeams, ySeams, zSeams)
        return count
    }

    private fun getSeams(instructions: List<Instruction>): Seams {
        return Seams(emptyArray(), emptyArray(), emptyArray())
    }

    private fun makeMondrianCube(
        xSeams: Array<Int>,
        ySeams: Array<Int>,
        zSeams: Array<Int>
    ):
            Array<Array<Array<Boolean>>> {
        return emptyArray()
    }
    
    private fun parseInstructions(
        instructions: List<Instruction>,
        mondrianCube: Array<Array<Array<Boolean>>>,
        xSeams: Array<Int>,
        ySeams: Array<Int>,
        zSeams: Array<Int>,
    ) {

    }

    private fun countLights(
        mondrianCube: Array<Array<Array<Boolean>>>,
        xSeams: Array<Int>,
        ySeams: Array<Int>,
        zSeams: Array<Int>
    ): Long {

        return -1L
    }


    data class Seams(
        val xSeams: Array<Int>,
        val ySeams: Array<Int>,
        val zSeams: Array<Int>,
    )

}

typealias Cuboid = Triple<Int, Int, Int>
typealias Range = List<Int>
