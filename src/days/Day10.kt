package days

import util.PPoint
import util.neighbours

object Day10 : Day<Long, List<String>> {
    override val number: Int = 10
    override val expectedPart1Test: Long = 80L
    override val expectedPart2Test: Long = 10L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val field = inputToField(input)
        val loop = findLoop(field)
        return loop.size / 2.toLong()
    }

    private fun findLoop(field: Map<PPoint, Char>): Map<PPoint, Pipe> {
        val startPoint = field.filter { it.value == 'S' }.map { it.key }.first()
        val start = Pipe(startPoint, 'S')
        val loop: Map<PPoint, Pipe> = buildMap {
            put(start.position, start)
            var currentPipe = findConnections(field, start).first()
            while (currentPipe != start) {
                put(currentPipe.position, currentPipe)
                val possibleConnections = findConnections(field, currentPipe).filter { !containsKey(it.position) }
                if (possibleConnections.isEmpty()) break
                else
                    currentPipe = possibleConnections.first()
            }
        }
        return loop
    }

    // find connections checks both directions, this filters out the case where start connections is a junk pipe
    // I could have figured out the shape of the start pipe here too
    private fun findConnections(field: Map<PPoint, Char>, from: Pipe): List<Pipe> =
            from.connected().map { Pipe(it, field.getValue(it)) }.filter { from.position in it.connected() }


    data class Pipe(val position: PPoint, val type: Char) {
        fun connected(): List<PPoint> =
                when (type) {
                    'S' -> position.neighbours()
                    '-' -> listOf((position.first to position.second - 1), (position.first to position.second + 1))
                    '|' -> listOf((position.first - 1 to position.second), (position.first + 1 to position.second))
                    'L' -> listOf((position.first - 1 to position.second), (position.first to position.second + 1))
                    'J' -> listOf((position.first - 1 to position.second), (position.first to position.second - 1))
                    '7' -> listOf((position.first to position.second - 1), (position.first + 1 to position.second))
                    'F' -> listOf((position.first to position.second + 1), (position.first + 1 to position.second))
                    else -> emptyList()
                }
    }

    // Pair Point PPoint is row to column - first=row secon=column
    private fun inputToField(input: List<String>): Map<PPoint, Char> {
        val field = buildMap<PPoint, Char> {
            for (r in input.withIndex())
                for (c in r.value.withIndex())
                    this[r.index to c.index] = c.value
        }
        return field
    }

    override fun part2(input: List<String>): Long {
        val dirtyField = inputToField(input)
        val loop = findLoop(dirtyField)
        val field = cleanupField(dirtyField, loop)
        val nest = findNest(field)
        return nest.size.toLong()
    }

    private fun cleanupField(dirtyField: Map<Pair<Int, Int>, Char>, loop: Map<Pair<Int, Int>, Pipe>): Map<Pair<Int, Int>, Char> = buildMap {
        // remove scrap pipes
        for ((k, v) in dirtyField) {
            if (k in loop) {
                this[k] = v
            } else {
                this[k] = '.'
            }
        }
        //fix start
        val start = loop.values.first { it.type == 'S' }
        this[start.position] = loop.findMissingPipe(start)
    }

    private fun Map<Pair<Int, Int>, Pipe>.findMissingPipe(missing: Pipe): Char {
        val reallyConnected = missing.connected().filter { this.getValue(it).connected().contains(missing.position) }
        val pipes = reallyConnected.map { this.getValue(it).type }.sorted().joinToString("")
        // TODO this when is totally not exhaustive and also has an error, it won't work for all inputs so you need add the case for the specific input
        return when (pipes) {
            "F|" -> '7'
            "--" -> '-'
            "||" -> '|'
            "7J" -> 'F'
            else -> error("oops unknown combo $pipes investigate input and figure out the when case")
        }
    }

    private fun findNest(field: Map<PPoint, Char>): Set<PPoint> {
        // we can get the real maybe nest points because we removed the rubble pipes
        val maybeNest = field.filter { (_, v) -> v == '.' }.map { (k, _) -> k }
        // we can use the isInside method because we replaced the start  with the actual pipe
        return maybeNest.filter { it.isInside(field) }.toSet()
    }

    private fun PPoint.isInside(field: Map<PPoint, Char>): Boolean {
        val (r, c) = this
        // ray goes left to 0 and count crossings
        // only JL| pipes are crossings
        val crossings = (0..c).map { field.getValue(r to it) }.count { it in "JL|" }
        return (crossings % 2) != 0
    }
}
