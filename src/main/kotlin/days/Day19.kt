package days

typealias Day19ReturnType = Int
typealias Day19InputType = List<String>

object Day19 : Day<Day19ReturnType, Day19InputType> {
    override val number: Int = 19
    override val expectedPart1Test: Day19ReturnType = -1
    override val expectedPart2Test: Day19ReturnType = -1
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day19InputType): Day19ReturnType {
        val blueprints = input.map { it.toBlueprint() }
        blueprints.forEach {
            it.runSimulation(24)
        }
        val answer = blueprints.sumOf { it.quality() }
        return answer
    }

    override fun part2(input: Day19InputType): Day19ReturnType {
        return expectedPart2Test
    }

    //------------------------------------------
    enum class Resource { ORE, CLAY, OBSIDIAN, GEODE }
    data class Blueprint(
        val id: Int,
        val costOre: List<Int>,
        val costClay: List<Int>,
        val costObsidian: List<Int>,
        val costGeode: List<Int>
    ) {
        var geodeCount = 0
        fun runSimulation(time: Int) {


        }

        fun quality(): Int = id * geodeCount
    }

    //Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
    private val blueprintRegex =
        """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

    private fun String.toBlueprint(): Blueprint {
        val (id, costOre, costClay, costObsidianOre, costObsidianClay, costGeodeOre, costGeodeObsidian) = blueprintRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        return Blueprint(
            id.toInt(),
            listOf(costOre.toInt()),
            listOf(costClay.toInt()),
            listOf(costObsidianOre.toInt(), costObsidianClay.toInt()),
            listOf(costGeodeOre.toInt(), costGeodeObsidian.toInt())
        )
    }

    data class State(
        val time: Int,
        val resourceOre: Int, val resourceClay: Int, val resourceObsidian: Int,  val resourceGeode:Int,
        val robotOre:Int, val  robotClay:Int, val robotObsidian:Int, val robotGeode:Int
    )
}