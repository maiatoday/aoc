package days

import kotlin.math.abs

object Day09 : Day<Long, List<String>> {
    override val number: Int = 9
    override val expectedPart1Test: Long = 88L//13L
    override val expectedPart2Test: Long = 36L

    enum class Direction { U, D, L, R, }

    data class Step(val direction: Direction, val count: Int)

    private fun String.toStep() = Step(
        Direction.valueOf(substringBefore(" ").uppercase()),
        substringAfter(" ").toInt()
    )

    override fun part1(input: List<String>): Long {
        val steps = parse(input)
        val positions: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var rope = initRope()
        positions.add(rope.last())
        steps.forEach { s ->
            repeat(s.count) {
                rope = shakeRope(s.direction, rope)
                positions += rope.last()
            }
        }
        return positions.size.toLong()
    }

    private fun initRope(start: Pair<Int, Int> = (0 to 0), ropeLength:Int = 2) = buildList {
        repeat(ropeLength) {
            add(start)
        }
    }

    private fun shakeRope(direction: Direction, rope: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val newRope = mutableListOf<Pair<Int, Int>>()
        // need to research what the functional  way would be to do this,
        for ((index, k) in rope.withIndex()) {
            if (index == 0 ) {
                newRope.add(moveHead(direction, k)) // this would be the initial value
            } else {
                newRope.add(moveKnot(newRope.last(), k)) // this would be he operation
            }
        }
        return newRope
    }

    private fun moveKnot(
        newHead: Pair<Int, Int>,
        tail: Pair<Int, Int>,
        knotLength: Int = 2
    ) = when {
        (newHead.first == tail.first) && (newHead.second == tail.second)-> {
            tail
        }
        // up or down
        (newHead.first == tail.first) -> {
            if (abs(newHead.second - tail.second) >= knotLength) {
                if (newHead.second > tail.second) (tail.first to tail.second + 1)
                else (tail.first to tail.second - 1)
            } else (tail.first to tail.second)
        }
        //  left or right
        (newHead.second == tail.second) -> {
            if (abs(newHead.first - tail.first) >= knotLength) {
                if (newHead.first > tail.first) (tail.first + 1 to tail.second)
                else (tail.first - 1 to tail.second)
            } else (tail.first to tail.second)
        }
        else -> {
            // diagonal
            // are they touching?
            // if not move diagonally towards head
            val firstDistance = abs(newHead.first - tail.first)
            val secondDistance = abs(newHead.second - tail.second)
            if (firstDistance >= knotLength || secondDistance >= knotLength) {
                val first = if (newHead.first > tail.first) tail.first + 1
                else tail.first - 1
                val second = if (newHead.second > tail.second) tail.second + 1
                else tail.second - 1
                (first to second)
            } else tail
        }

    }

    private fun moveHead(
        direction: Direction,
        head: Pair<Int, Int>
    ) = when (direction) {
        Direction.U -> head.first to head.second + 1
        Direction.D -> head.first to head.second - 1
        Direction.L -> head.first - 1 to head.second
        Direction.R -> head.first + 1 to head.second
    }

    private fun parse(input: List<String>): List<Step> = input
        .map { it.toStep() }

    override fun part2(input: List<String>): Long {
        val steps = parse(input)
        val positions: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var rope = initRope(( 0 to 0), 10)
        positions.add(rope.last())
        steps.forEach { s ->
//            println("====== ${s} =====")
            repeat(s.count) {
                //println("====== ====== ${s.direction}${it} ====== ====== ")
                rope = shakeRope(s.direction, rope)
                positions += rope.last()
            }
//            showRope(rope)
        }
//        println("====== Positions ${positions.count()} =====")
//        showPositions(positions)
        return positions.size.toLong()
    }

    private fun showRope(rope: List<Pair<Int, Int>>) {
        for (m in 23 downTo -23) for (n in  -23..23) {
            if (rope.contains( (n to m))) {
                print(rope.indexOf((n to m)))
            } else if (m == 0 && n == 0) print("s")
            else print(".")
            if (n == 23) print("\n")
        }

    }

    private fun showPositions(pp: Set<Pair<Int, Int>>) {
        for (m in 23 downTo -23) for (n in  -23..23) {
            if (m == 0 && n == 0) print("s")
            else if (pp.contains( (n to m))) {
                print("#")
            }
            else print(".")
            if (n == 23) print("\n")
        }

    }
}