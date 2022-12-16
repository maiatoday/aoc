package days

typealias Day16ReturnType = Int
typealias Day16InputType = List<String>

typealias Tunnel = Pair<String, String>

object Day16 : Day<Day16ReturnType, Day16InputType> {
    override val number: Int = 16
    override val expectedPart1Test: Day16ReturnType = 1651
    override val expectedPart2Test: Day16ReturnType = -1

    override fun part1(input: Day16InputType): Day16ReturnType {
        val tunnelMap = input.toTunnelMap()
        return expectedPart1Test
    }

    override fun part2(input: Day16InputType): Day16ReturnType {
        return expectedPart2Test
    }
    //  Map<Tunnel, Int>

    // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    //  """Valve [A-Z][A-Z] has flow rate=0; tunnels lead to valves .*""".toRegex()
    private val inputRegex =
        """Valve ([A-Z][A-Z]) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()

    private fun String.toTunnels(): List<Pair<Tunnel, Int>> {
        val (from, rate, to) = inputRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        val toList = to.split(", ")
        val tunnelList = buildList {
            for (t in toList) {
                this.add(from to t to rate.toInt())
            }
        }
        return tunnelList
    }

    private fun List<String>.toTunnelMap(): Map<Tunnel, Int> =
        buildMap {
            this@toTunnelMap.flatMap {
                it.toTunnels()
            }.forEach {
                this@buildMap[it.first] = it.second
            }
        }


}