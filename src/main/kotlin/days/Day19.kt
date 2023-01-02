package days

import java.util.PriorityQueue

typealias Day19ReturnType = Int
typealias Day19InputType = List<String>

typealias Robots = List<Int>
typealias Resources = List<Int>

object Day19 : Day<Day19ReturnType, Day19InputType> {
    override val number: Int = 19
    override val expectedPart1Test: Day19ReturnType = 33
    override val expectedPart2Test: Day19ReturnType = -1
    override var useTestData = true
    override val debug = true

    override fun part1(input: Day19InputType): Day19ReturnType {
        val blueprints = input.map { it.toBlueprint() }
        blueprints.forEach {
            it.openGeodes(24)
        }
        return blueprints.sumOf { it.quality() }
    }

    override fun part2(input: Day19InputType): Day19ReturnType {
        return expectedPart2Test
    }

    //------------------------------------------
    enum class Resource { ORE, CLAY, OBSIDIAN, GEODE }

    val ORE = Resource.ORE.ordinal
    val CLAY = Resource.CLAY.ordinal
    val OBSIDIAN = Resource.OBSIDIAN.ordinal
    val GEODE = Resource.GEODE.ordinal

    data class Blueprint(
        val id: Int,
        val cost: List<Resources>
    ) {
        val maxRobots: Robots = maxRobotsFromCost()

        private fun maxRobotsFromCost(): Resources = buildList {
            add(cost.maxOf { it[ORE] })
            add(cost.maxOf { it[CLAY] })
            add(cost.maxOf { it[OBSIDIAN] })
            add(cost.maxOf { it[GEODE] }) // this one isn't necessary but my arrays work like this
        }

        private var geodeCount = 0
        fun openGeodes(maxTime: Int) {
            val queue = PriorityQueue<State>().apply { add(State(time = maxTime)) }
            val statesAtTimeZero = mutableListOf<State>()
            while (queue.isNotEmpty()) {
                val current = queue.poll()
                if (current.time == 0) {
                    statesAtTimeZero += current
                } else {
                    queue.addAll(current.processState(maxTime, this))
                }
                log {
                    if (id == 1) {
                        println("Blueprint $id  queue length ${queue.size}")
                    }
                }
            }
            geodeCount = statesAtTimeZero.maxOf { it.resources[GEODE] }
        }

        fun quality(): Int = id * geodeCount

        fun canBuildRobot(resources: Resources, cost: Resources): Int =
            cost.zip(resources).filter { (c, _) -> c > 0 }.minOf { (c, r) -> if (r >= c) r / c else 0 }
    }

    //Blueprint 1: Each ore robot costs 4 ores. Each clay robot costs 2 ores. Each obsidian robot costs 3 ores and 14 clay. Each geode robot costs 2 ores and 7 obsidian.
    private val blueprintRegex =
        """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian."""
            .toRegex()

    private fun String.toBlueprint(): Blueprint {
        val (id, costOreNeedsOre, costClayNeedsOre, costObsidianNeedsOre, costObsidianNeedsClay, costGeodeNeedsOre, costGeodeNeedsObsidian) =
            blueprintRegex
                .matchEntire(this)
                ?.destructured
                ?: throw IllegalArgumentException("Bad data $this")
        return Blueprint(
            id = id.toInt(),
            cost = listOf(
                listOf(costOreNeedsOre.toInt(), 0, 0, 0),
                listOf(costClayNeedsOre.toInt(), 0, 0, 0),
                listOf(costObsidianNeedsOre.toInt(), costObsidianNeedsClay.toInt(), 0, 0),
                listOf(costGeodeNeedsOre.toInt(), 0, costGeodeNeedsObsidian.toInt(), 0)
            )
        )
    }

    data class State(
        val time: Int,
        val resources: Resources = listOf(0, 0, 0, 0),
        val robots: Robots = listOf(1, 0, 0, 0)
    ) : Comparable<State> {
        fun processState(maxTime: Int, blueprint: Blueprint): List<State> {
            val nextStates = mutableListOf<State>()
            if (time == 0) return nextStates

            // which robots can I build? try all of them
            build(time, GEODE, blueprint)?.let { nextStates += it }
            if (robots[OBSIDIAN] <= blueprint.maxRobots[OBSIDIAN])
                build(time, OBSIDIAN, blueprint)?.let { nextStates += it }
            if (robots[ORE] <= blueprint.maxRobots[ORE])
                build(time, ORE, blueprint)?.let { nextStates += it }
            if (robots[CLAY] <= blueprint.maxRobots[CLAY] && resources[CLAY] < blueprint.cost[OBSIDIAN][CLAY] / 2)
                build(time, CLAY, blueprint)?.let { nextStates += it }

            // no robots built
         //   if (nextStates.isEmpty()) {
                log {
                    if (blueprint.id == 1)
                        println("Blueprint ${blueprint.id}: At time $time continue collecting")
                }
                val collectedResources = resources.zip(robots).map { (c, r) -> c + r }
                nextStates += State(time - 1, collectedResources, robots)
          //  }

            return nextStates
        }

        private fun build(
            time: Int,
            robotType: Int,
            blueprint: Blueprint
        ): State? {
            val count = 1
            val cost = blueprint.cost[robotType]
            val canBuild = blueprint.canBuildRobot(resources, cost)

            log {
                if (count <= canBuild && blueprint.id == 1) {
                    println("**** Blueprint: ${blueprint.id} at time $time built $robotType")
                    println("     with resources $resources")
                }
            }
            return if (count <= canBuild) State(
                time - 1,
                resources.use(count, cost)
                    .add(robots),// robots are collect resources... that's why I'm adding them to the resources
                robots.add(count, robotType)
            ) else null
        }

        override fun compareTo(other: State): Int = other.resources[GEODE].compareTo(resources[GEODE])
    }

    fun Robots.add(count: Int, typeIndex: Int): Robots =
        this.mapIndexed { i, r ->
            if (i == typeIndex) r + count else r
        }

    fun Resources.use(count: Int, cost: Resources): Resources =
        this.zip(cost).map { (r, c) -> r - c * count }

    fun Resources.add(collectedResources: Resources) = this.zip(collectedResources).map { (r, c) -> r + c }
}