package days

import util.Point
import util.neighbours

typealias Day24ReturnType = Int
typealias Day24InputType = List<String>

@ExperimentalStdlibApi
object Day24 : Day<Day24ReturnType, Day24InputType> {
    override val number: Int = 24
    override val expectedPart1Test: Day24ReturnType = 18
    override val expectedPart2Test: Day24ReturnType = 54
    override var useTestData = true
    override val debug = false

    private var mapWidth: Int = -1
    private var mapHeight: Int = -1
    private val blizzardTypes = setOf('v', '^', '>', '<')
    private var xMarksSpot: Point = Point(-1, -1)
    private var debugWalls: Set<Point> = emptySet()
    override fun part1(input: Day24InputType): Day24ReturnType {
        mapWidth = input[0].length
        mapHeight = input.size
        val blizzardList = input.toBlizzardList()
        val startPoint = Point(input.first().indexOfFirst { it == '.' }, 0)
        val endPoint = Point(input.last().indexOfFirst { it == '.' }, input.lastIndex)
        val walls = input.toWalls()
        debugWalls = walls
        xMarksSpot = startPoint
        val blizzardState = BlizzardState(blizzardList)
        val minutes = findShortestPath(startPoint, endPoint, blizzardState, 0, walls)
        return minutes
    }

    override fun part2(input: Day24InputType): Day24ReturnType {
        mapWidth = input[0].length
        mapHeight = input.size
        val blizzardList = input.toBlizzardList()
        val startPoint = Point(input.first().indexOfFirst { it == '.' }, 0)
        val endPoint = Point(input.last().indexOfFirst { it == '.' }, input.lastIndex)
        val walls = input.toWalls()
        val blizzardState = BlizzardState(blizzardList)
        val aToB = findShortestPath(startPoint, endPoint, blizzardState, 0, walls)
        val bToA = findShortestPath(endPoint, startPoint, blizzardState, aToB, walls)
        val andBackAgain = findShortestPath(startPoint, endPoint, blizzardState, bToA, walls)
        return andBackAgain
    }
    //>--------------------------------------------------

    private fun findShortestPath(
        start: Point,
        end: Point,
        blizzardState: BlizzardState,
        startTime: Int,
        walls: Set<Point>
    ): Day24ReturnType {
        val visited: MutableSet<TryPath> = mutableSetOf()
        val frontier: MutableList<TryPath> = mutableListOf()
        frontier.add(TryPath(startTime, start))

        while (frontier.isNotEmpty()) {
            val currentPath = frontier.removeFirst()
            xMarksSpot = currentPath.position
            if (currentPath !in visited) {
                visited += currentPath
                val nextStep = currentPath.steps + 1
                log {
                    println("step count  $nextStep position: ${currentPath.position} ")
                }
                val occupiedAtNextStep = blizzardState.occupiedAt(nextStep)

                // if (currentPath.position !in occupiedAtNextStep) frontier.add(TryPath(nextStep, currentPath.position))

                val neighbours = currentPath.position.validNeighbours(occupiedAtNextStep)
                if (end in neighbours) return nextStep
                for (n in neighbours) {
                    if (n !in walls)
                        frontier.add(TryPath(nextStep, n))
                }
            } else {
                log {
                    print(".")
                }
            }

        }
        return error("I am wandering around in a snowstorm")
    }


    private fun List<String>.toBlizzardList(): List<Blizzard> =
        this.flatMapIndexed { y, s -> s.mapIndexed { x, c -> c.toBlizzard(x, y) } }
            .filterNotNull()

    private fun List<String>.toWalls(): Set<Point> =
        this.flatMapIndexed { y, s -> s.mapIndexed { x, c -> if (c == '#') Point(x, y) else null } }
            .filterNotNull()
            .toSet()

    private fun Char.toBlizzard(x: Int, y: Int): Blizzard? =
        if (this in blizzardTypes) Blizzard(this, Point(x, y)) else null

    data class Blizzard(val direction: Char, val position: Point) {
        val orig: Point = position

        fun blow(): Blizzard {
            return this.copy(position = position.blow(direction))
        }

    }

    private fun Point.blow(direction: Char): Point =
        when (direction) {
            '>' -> {
                val xx = if (x + 1 == mapWidth - 1) 1 else x + 1
                Point(xx, y)
            }

            '<' -> {
                val xx = if (x - 1 == 0) mapWidth - 2 else x - 1
                Point(xx, y)
            }

            'v' -> {
                val yy = if (y + 1 == mapHeight - 1) 1 else y + 1
                Point(x, yy)
            }

            '^' -> {
                val yy = if (y - 1 == 0) mapHeight - 2 else y - 1
                Point(x, yy)
            }

            else -> error("oops $direction")
        }

    private fun List<Blizzard>.tick(): List<Blizzard> {
        return this.map { it.blow() }
    }

    private fun List<Blizzard>.occupiedSet(): Set<Point> = this.map { b -> b.position }.toSet()

    private fun List<Blizzard>.toMap(): Map<Point, Blizzard> = buildMap {
        this@toMap.forEach {
            put(it.position, it)
        }
    }

    class BlizzardState(private val blizzards: List<Blizzard>) {
        private val cachedBlizzardsPerTick = mutableMapOf(0 to blizzards)
        init {
            log {
                println("BlizzardState init")
               // blizzards.debug()
            }
        }
        fun occupiedAt(tick: Int): Set<Point> {
            val blizzardsForTick = cachedBlizzardsPerTick.computeIfAbsent(tick) { key ->
                cachedBlizzardsPerTick[key - 1]?.tick() ?: error("missing map state for tick $key")
            }
            log {
               // println("Tick $tick")
              //  blizzardsForTick.debug()
            }
            return blizzardsForTick.occupiedSet()
        }
    }

    private fun Point.validNeighbours(occupied: Set<Point>): List<Point> {
        val neighbours = this.neighbours(
            maxY = mapHeight,
            maxX = mapWidth,
            diagonal = false,
            includeSelf = true,
        )
        // add some preference to down, right and self, self lets you wait some compareBy sort
        return neighbours.filter { it !in occupied }
    }

    private data class TryPath(val steps: Int, val position: Point)

//>--------------------------------------------------

    fun List<Blizzard>.debug() {
        val points = this.occupiedSet().toList()
        val boundaries = (0 ..< mapWidth to (0 ..< mapHeight))
        val map = this.toMap()
        log {
            for (y in boundaries.second) {
                for (x in boundaries.first) {
                    val p = Point(x, y)
                    if (p in points) {
                        print(map[Point(x, y)]?.direction)
                    } else if (p == xMarksSpot) {
                        print('O')
                    } else if (p in debugWalls) {
                        print("#")
                    }
                    else print(".")
                }
                println()
            }
        }
    }
}

