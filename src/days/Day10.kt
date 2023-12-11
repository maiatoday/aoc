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
        val (field, _) = inputToField(input)
        val loop = findLoop(field)
        return loop.size / 2.toLong()
    }

    private fun findLoop(field: Map<PPoint, Char>): Map<PPoint, Pipe> {
        val startPoint = field.filter { it.value == 'S' }.map { it.key }.first()
        val start = Pipe(startPoint, 'S')
        val loop: Map<PPoint, Pipe> = buildMap {
            put(start.position, start)
            var currentPipe = findConnections(field, start).first()
            // this is where I count steps if I need to
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
    private fun inputToField(input: List<String>): Pair<Map<PPoint, Char>, Pair<Int, Int>> {
        val field = buildMap<PPoint, Char> {
            for (r in input.withIndex())
                for (c in r.value.withIndex())
                    this[r.index to c.index] = c.value
        }
        val fieldSize = input.size to input.first().length
        return field to fieldSize
    }

    override fun part2(input: List<String>): Long {
        val (field, fieldSize) = inputToField(input)
        val loop = findLoop(field)
        val nest = findNest(field, fieldSize, loop)
        return nest.size.toLong()
    }

    private fun findNest(field: Map<PPoint, Char>, fieldSize: Pair<Int, Int>, loop: Map<PPoint, Pipe>): Set<PPoint> {
        val loopPoints = loop.keys
        val outside = mutableSetOf<PPoint>().apply { addAll(getEdges(fieldSize).filter { it !in loopPoints }) }
        val inside = mutableSetOf<PPoint>()
        val checked = mutableSetOf<PPoint>()

        // add any unchecked neighbours 
        val queue = mutableSetOf(outside.first())
        while (queue.isNotEmpty()) {
            val currentSpot = queue.first()
            queue.remove(currentSpot)
            checked.add(currentSpot)
            val neighbours = currentSpot.neighbours(maxM = fieldSize.first, maxN = fieldSize.second, onlyPositive = true, stayBelowMax = true)
            // a spot is either loop, inside or outside or unchecked
            if (currentSpot in loopPoints) {
                // if loop and x neighbour is ouside then other x  neighbour is inside
                // if loop and y neighbour is outside then other y  neighbour is inside
            } else {
                if (neighbours.any { it in outside }) outside.add(currentSpot)
            }
            neighbours.forEach {
                if (it !in checked) queue.add(it)
            }
        }
        return inside
    }

    private fun getEdges(size: Pair<Int, Int>): Set<PPoint> = buildSet {
        // vertical edges
        for (r in 0..size.first) {
            add(r to 0)
            add(r to size.second - 1)
        }
        // horizontal edges
        for (c in 0..size.second) {
            add(c to 0)
            add(c to size.first - 1)
        }


    }

}
