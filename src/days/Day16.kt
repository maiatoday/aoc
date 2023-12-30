package days

import util.Point
import util.debug

object Day16 : Day<Long, List<String>> {
    override val number: Int = 16
    override val expectedPart1Test: Long = 46L
    override val expectedPart2Test: Long = 51L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val contraption = input.parse()
        val energisePoints = energise(contraption)
        return energisePoints.count().toLong()
    }

    private fun energise(
        contraption: Map<Point, Component>,
        start: Beam = Beam(Point(-1, 0), Direction.Right)
    ): Set<Point> {
        val visited = mutableSetOf<Beam>()
        val deque = ArrayDeque<Beam>()
        deque.add(start)
        while (deque.isNotEmpty()) {
            val beam = deque.removeFirst()
            if (beam !in visited) {
                visited.add(beam)
                // if the point we want to go to is not in the contraption it is out of range
                contraption[beam.pointing]?.let {
                    deque.addAll(it.next(beam))
                }
            }
        }
        log {
            visited.map { it.position }.toSet().toList().debug()
        }
        // filter out points not in the contraption e.g. the start which starts outside
        return visited.map { it.position }.filter { it in contraption }.toSet()
    }

    data class Beam(val position: Point, val direction: Direction) {
        val pointing = position + direction.p
    }

    private fun List<String>.parse(): Map<Point, Component> {
        val components = mutableMapOf<Point, Component>()
        for (y in this.indices)
            for (x in this[0].indices) {
                components[Point(x, y)] = Component(this[y][x], Point(x, y))
            }
        return components
    }

    enum class ComponentType(val orig: Char) {
        MirrorLeft('\\'),
        MirrorRight('/'),
        SplitV('|'),
        SplitH('-'),
        Space('.');

        companion object {
            private val map = entries.associateBy { it.orig }
            infix fun from(value: Char) = map[value] ?: error("bad component $value")
        }
    }

    data class Component(val type: ComponentType, val position: Point) {
        fun next(incoming: Beam): List<Beam> {

            val outgoing = when (type) {
                ComponentType.MirrorLeft -> {
                    when (incoming.direction) {
                        Direction.Left -> listOf(Beam(position, Direction.Up))
                        Direction.Right -> listOf(Beam(position, Direction.Down))
                        Direction.Up -> listOf(Beam(position, Direction.Left))
                        Direction.Down -> listOf(Beam(position, Direction.Right))
                    }
                }

                ComponentType.MirrorRight -> {
                    when (incoming.direction) {
                        Direction.Left -> listOf(Beam(position, Direction.Down))
                        Direction.Right -> listOf(Beam(position, Direction.Up))
                        Direction.Up -> listOf(Beam(position, Direction.Right))
                        Direction.Down -> listOf(Beam(position, Direction.Left))
                    }
                }

                ComponentType.SplitV -> {
                    when (incoming.direction) {
                        Direction.Left,
                        Direction.Right -> {
                            listOf(Beam(position, Direction.Up), Beam(position, Direction.Down))
                        }

                        // | looks like space if the beam is traveling up or down
                        Direction.Up,
                        Direction.Down -> listOf(Beam(position, incoming.direction))
                    }
                }

                ComponentType.SplitH -> {
                    when (incoming.direction) {
                        // - looks like space if the beam is traveling left or right
                        Direction.Left,
                        Direction.Right -> listOf(Beam(position, incoming.direction))

                        Direction.Up,
                        Direction.Down -> listOf(Beam(position, Direction.Left), Beam(position, Direction.Right))
                    }
                }

                ComponentType.Space -> listOf(Beam(position, incoming.direction))
            }
            log {
                println("\n $this")
                println("    incoming $incoming ... outgoing $outgoing")
            }
            return outgoing
        }

    }

    private fun Component(c: Char, position: Point): Component = Component(ComponentType.from(c), position)

    private infix operator fun Point.plus(other: Point): Point =
        Point(this.x + other.x, this.y + other.y)


    enum class Direction(val p: Point) {
        Left(Point(-1, 0)), Right(Point(1, 0)), Up(Point(0, -1)), Down(Point(0, 1));

        companion object {
            private val map = entries.associateBy { it.p }
            infix fun from(p: Point) = map[p] ?: error("bad direction $p")
        }
    }

    override fun part2(input: List<String>): Long {
        val contraption = input.parse()
        val xRange = input.first().indices
        val yRange = input.indices
        val allEdges = xRange.map { Beam(Point(it, -1), Direction.Down) } +
                xRange.map { Beam(Point(it, yRange.last + 1), Direction.Up) } +
                yRange.map { Beam(Point(-1, it), Direction.Right) } +
                yRange.map { Beam(Point(xRange.last + 1, it), Direction.Left) }

        val pathLengths = allEdges.map { energise(contraption, it).count() }

        return pathLengths.max().toLong()
    }
}
