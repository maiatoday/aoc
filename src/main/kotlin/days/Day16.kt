package days

typealias Day16ReturnType = Int
typealias Day16InputType = List<String>

typealias Tunnel = Pair<String, String>

object Day16 : Day<Day16ReturnType, Day16InputType> {
    override val number: Int = 16
    override val expectedPart1Test: Day16ReturnType = 1651
    override val expectedPart2Test: Day16ReturnType = -1
    override var useTestData = true

    override fun part1(input: Day16InputType): Day16ReturnType {
        val tunnelMap = input.toTunnelMap()
        val valves = tunnelMap.keys.map { it.first }.toSet()
        val valveFlows = tunnelMap.map { it.key.first to it.value }.toMap()
        val shortestPaths = findShortestPaths(valves, tunnelMap.keys)
        shortestPaths.forEach {
            println("Reach ${it.key} in ${it.value.distance} steps prev valve ${it.value.previousValve}")
        }
        return expectedPart1Test
    }

    private fun findShortestPaths(
        valves: Set<String>,
        tunnelMap: Set<Tunnel>
    ): MutableMap<String, TableEntry> {
        val valvesToVisit = mutableSetOf<String>().also { it.addAll(valves) }
        val visited = mutableListOf<String>()

        val table = mutableMapOf<String, TableEntry>()
        valves.forEach {
            table[it] = TableEntry(Int.MAX_VALUE, "")
        }
        table[valves.first()] = TableEntry(0, "")

        while (valvesToVisit.isNotEmpty()) {
            val currentValve = table.filter { it.key in valvesToVisit }.minBy { it.value.distance }.key
            valvesToVisit.remove(currentValve)
            val distance = table[currentValve]?.distance ?: Int.MAX_VALUE
            tunnelMap.filter { it.first == currentValve }.forEach {
                // all edges are weighted 1  for now
                val newDistance: Int = distance + 1
                val tableDistance: Int = table[it.second]?.distance ?: Int.MAX_VALUE
                if (tableDistance > newDistance) {
                    table[it.second] = TableEntry(newDistance, currentValve)
                }
            }
            visited.add(currentValve)
        }
        return table
    }

    data class TableEntry(val distance: Int, val previousValve: String)

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
        return to.split(", ").map { toTunnel -> from to toTunnel to rate.toInt() }
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