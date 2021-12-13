object Day13 {

    fun part1(input: List<String>): Long {
        val coordinates = getCoordinates(input)
        val dots = getDots(coordinates)
        val foldInstructions = getFoldInstructions(input)
        val newDots = applyFold(dots, foldInstructions.first())
        return newDots.flatten().filter {
            it
        }.size.toLong()
    }

    private fun applyFold(dots: List<List<Boolean>>, instruction: Instruction): List<List<Boolean>> {
        val newDots = mutableListOf<MutableList<Boolean>>()
        if (instruction.isHorizontal) {
            val maxY = dots.size - 1
            for ( y in 0 until instruction.value) {
                val innerList = mutableListOf<Boolean>()
                for (x in dots.first().indices) {
                    val combined = dots[y][x] or dots[maxY-y][x]
                    innerList.add(combined)
                }
                newDots.add(innerList)
            }
        } else {
            val maxX = dots.first().size -1
            for ( y in dots.indices) {
                val innerList = mutableListOf<Boolean>()
                for (x in 0 until instruction.value) {
                    val combined = dots[y][x] or dots[y][maxX - x]
                    innerList.add(combined)
                }
                newDots.add(innerList)
            }
        }
        return newDots
    }

    private fun getDots(coordinates: List<List<Int>>): List<List<Boolean>> {
        val maxY = coordinates.maxOf { it[1] }
        val maxX = coordinates.maxOf { it[0] }
        val dots = mutableListOf<MutableList<Boolean>>()
        for (y in 0..maxY) {
            val innerList = mutableListOf<Boolean>()
            for (x in 0..maxX) {
                if (coordinates.contains(listOf(x, y))) innerList.add(true)
                else innerList.add(false)
            }
            dots.add(innerList)
        }
        return dots
    }
    
    private fun getCoordinates(input: List<String>): List<List<Int>> =
        input.filter {
            !it.startsWith("fold along") and it.isNotEmpty()
        }.map { s -> s.trim().split(",").map { n -> n.toInt() } }

    private fun getFoldInstructions(input: List<String>): List<Instruction> =
        input.filter {
            it.startsWith("fold along")
        }.map { i -> Instruction(i.contains("y="), i.filter { it.isDigit() }.toInt()) }

    data class Instruction(val isHorizontal: Boolean, val value: Int)

    fun part2(input: List<String>): Long {
        val coordinates = getCoordinates(input)
        var dots = getDots(coordinates)
        val foldInstructions = getFoldInstructions(input)
        foldInstructions.forEach {
            dots = applyFold(dots, it)
        }
        printDots(dots)
        return -1L
    }

    private fun printDots(dots: List<List<Boolean>>) {
        dots.map {
            it.map { d-> if (d) "#" else "."}.joinToString("")
        }.forEach {
            println(it)
        }
    }
}
