package days

import util.Point

object Day16 : Day<Long, List<String>> {
    override val number: Int = 16
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val contraption = input.parse()
        val energisePoints = energise(contraption, input.first().indices, input.indices)
        return energisePoints.count().toLong()
    }

    private fun energise(contraption: Map<Point, Component>, xRange: IntRange, yRange: IntRange): Set<Point> {
        val visited = mutableSetOf<Point>()
        val queue = mutableListOf(Point(-1, 0) to Point(0, 0))
        while (queue.isNotEmpty()) {
            val beam = queue.first()
            queue.remove(beam)
            val newRays = followBeam(
                    contraption = contraption,
                    xRange = xRange,
                    yRange = yRange,
                    visited = visited,
                    beam = beam,
            )
            if (newRays.isNotEmpty()) {
                queue.addAll(newRays)
            }
        }
        return visited
    }

    private fun followBeam(
            contraption: Map<Point, Component>,
            xRange: IntRange,
            yRange: IntRange,
            visited: MutableSet<Point>,
            beam: Pair<Point, Point>,
    ): List<Pair<Point, Point>> {
        // follow beam, and add points to visited
        val currentComponent = contraption[beam.second]
        val next
        // the beam stops if it goes out of bounds or
        // if it hits a splitter or 
        // if it loops
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
        fun next(prev: Point): List<Point> = when (type) {
            ComponentType.MirrorLeft -> TODO()
            ComponentType.MirrorRight -> TODO()
            ComponentType.SplitV -> if (prev.x == position.x) listOf(position) else listOf(position.copy(), position.copy())
            ComponentType.SplitH -> if (prev.y == position.y) listOf(position) else listOf(position.copy(), position.copy())
            ComponentType.Space -> listOf(position)
        }
    }

    private fun Component(c: Char, position: Point): Component = Component(ComponentType.from(c), position)

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
