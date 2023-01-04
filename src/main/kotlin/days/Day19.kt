package days

import filterComments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.PriorityQueue

typealias Day19ReturnType = Int
typealias Day19InputType = List<String>

typealias Robots = List<Int>
typealias Resources = List<Int>

object Day19 : Day<Day19ReturnType, Day19InputType> {
    override val number: Int = 19
    override val expectedPart1Test: Day19ReturnType = 33
    override val expectedPart2Test: Day19ReturnType = 62
    override var useTestData = true
    override val debug = true

    override fun part1(input: Day19InputType): Day19ReturnType {
        val blueprints = input.filterComments().map { it.toBlueprint() }
        blueprints.forEach {
            log {
                println("Exploring $it")
            }
            it.openGeodes(24)
            log {
                println(" ---  opened ${it.geodeCount} Geodes - quality ${it.quality()}")
            }
        }
        return blueprints.sumOf { it.quality() }
    }

    override fun part2(input: Day19InputType): Day19ReturnType = runBlocking {
        val blueprints = input.filterComments().map { it.toBlueprint() }
        val answer = withContext(Dispatchers.Default) {
            blueprints.take(3).map {
                async {
                    log {
                        println("Exploring $it")
                    }
                    it.openGeodes(32).also { log { println(" ---  opened $it Geodes") } }
                }
            }.fold(1) { acc, d ->
                acc * d.await()
            }
        }
        answer
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

        var geodeCount = 0
        fun openGeodes(maxTime: Int): Int {
            val queue = PriorityQueue<State>().apply { add(State(time = maxTime)) }
            while (queue.isNotEmpty()) {
                val current = queue.poll()
                queue.addAll(prune(current.processState(maxTime, this)))
            }
            return geodeCount
        }

        private fun prune(processState: List<State>): List<State> = buildList {
            processState.forEach {
                if (it.possibleGeodes > geodeCount) geodeCount = it.possibleGeodes
                if (potentialBestGuess(it) > geodeCount) add(it)
            }
        }

        fun quality(): Int = id * geodeCount

        fun canBuildRobot(resources: Resources, cost: Resources): Boolean =
            cost.zip(resources).filter { (c, _) -> c > 0 }.all { (c, r) -> r >= c }

        private fun potentialBestGuess(state: State): Int {
            val futureRobots = mutableListOf<Int>(0, 0, 0, 0)
            val futureResources = state.resources.toMutableList()
            repeat(state.time) {
                // guess possible resources from robots
                for ((r, c) in futureRobots.withIndex()) {
                    futureResources[r] += state.robots[r] + c
                }
                // guess possible robots from resources
                for ((r, c) in futureRobots.withIndex()) {
                    if (canBuildRobot(futureResources, cost[r])) {
                        futureRobots[r] = c + 1
                    }
                }
            }
            return futureResources[GEODE]
        }
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
        val possibleGeodes = resources[GEODE] + robots[GEODE] * time
        fun processState(maxTime: Int, blueprint: Blueprint): List<State> {
            val nextStates = mutableListOf<State>()
            if (time == 0) return nextStates

            val timeLeft = maxTime - time

            // which robots can I build? try all of them
            if (time > 1)
                build(time, GEODE, blueprint, maxTime)?.let { nextStates += it }
            if (robots[OBSIDIAN] <= blueprint.maxRobots[OBSIDIAN] && time > 2)
                build(time, OBSIDIAN, blueprint, maxTime)?.let { nextStates += it }
            if (robots[CLAY] <= blueprint.maxRobots[CLAY] && time > 3)// && resources[CLAY] < blueprint.cost[OBSIDIAN][CLAY] / 2)
                build(time, CLAY, blueprint, maxTime)?.let { nextStates += it }
            if (robots[ORE] <= blueprint.maxRobots[ORE] && time > 2)
                build(time, ORE, blueprint, maxTime)?.let { nextStates += it }

            // no robots built
            log {
//                println("++++ Blueprint ${blueprint.id}: At minute ${maxTime - time}  ($time) continue collecting")
            }
            val collectedResources = resources.zip(robots).map { (c, r) -> c + r }
            nextStates += State(time - 1, collectedResources, robots)

            return nextStates
        }

        private fun build(
            time: Int,
            robotType: Int,
            blueprint: Blueprint,
            maxTime: Int
        ): State? {
            val cost = blueprint.cost[robotType]
            val canBuild = blueprint.canBuildRobot(resources, cost)

            log {
//                if (canBuild) {//{&& blueprint.id == 2) {
//                    println("**** Blueprint: ${blueprint.id} at minute ${maxTime - time} ($time)  ===  built ${Resource.values()[robotType]}")
//                    println("     with resources $resources")
//                }
            }
            return if (canBuild)
                State(
                    time = time - 1,
                    // robots collect 1 resource... that's why I'm adding them to the resources
                    resources = resources.use(1, cost).add(robots),
                    robots = robots.add(1, robotType)
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