package days

import kotlin.math.max

typealias Day16ReturnType = Int
typealias Day16InputType = List<String>

object Day16 : Day<Day16ReturnType, Day16InputType> {
    override val number: Int = 16
    override val expectedPart1Test: Day16ReturnType = 1651
    override val expectedPart2Test: Day16ReturnType = 1707
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day16InputType): Day16ReturnType {
        val tunnelMap = input.toTunnelMap().toMapById()
        return maxFlow(0, 30, listOf(), 0, tunnelMap)
    }

    private const val timeAllocated = 26
    override fun part2(input: Day16InputType): Day16ReturnType {
        val tunnelMap = input.toTunnelMap().toMapById()
        return maxFlow(0, timeAllocated, listOf(), 1, tunnelMap)
    }

    //--------------------------------------------------
    fun packState(
        valve: Int,
        time: Int,
        openedValves: List<Int>,
        elephantHelp: Int
    ): Int =
    // opened valves fits into 16 bits because there are never more than 15 valves with non-zero flow
    // valve max  id is 58 so 6 bits enough
    // time max is 30 fits into 5 bits
    // elephant help is one bit
        // total 28 bits which fit into an Int of 32
        packValves(openedValves) or (valve shl 16) or (time shl (16 + 6)) or (elephantHelp shl (16 + 6 + 5))

    fun packValves(valves: List<Int>): Int = valves.fold(0) { a, v ->
        // remember index 0 was AA which is flow rate 0 followed  by valid flow valves no more than 16 of them
        a or (1 shl v - 1)
    }

    private val memoizedStates =
        HashMap<Int, Int>(87000000) //super rough ballpark of capacity to stop hashmap growing all the time

    private fun maxFlow(
        currentValve: Int,
        time: Int,
        openedValves: List<Int>,
        elephantHelp: Int,
        tunnels: Map<Int, Tunnel>
    ): Int {
        // counting down from max time to 0, i.e. time left
        if (time == 0) return if (elephantHelp == 1) maxFlow(0, timeAllocated, openedValves, 0, tunnels) else 0
        val state = packState(currentValve, time, openedValves, elephantHelp)
        var answer = 0
        if (state in memoizedStates) {
            answer = memoizedStates[state] ?: error("oops I thought I remembered something $state")
        } else {
            val (_, currentFlow, connections) = tunnels[currentValve]
                ?: error("oops elephants don't forget $currentValve")
            if (currentValve !in openedValves && currentFlow != 0) {
                // open the valve!
                answer = max(
                    answer,
                    (time - 1) * currentFlow + maxFlow(
                        currentValve = currentValve,
                        time = time - 1,
                        openedValves = openedValves + currentValve,
                        elephantHelp = elephantHelp,
                        tunnels = tunnels
                    )
                )
            }
            // if possible walk somewhere else from here
            connections.forEach {
                answer = max(answer, maxFlow(it, time - 1, openedValves, elephantHelp, tunnels))
            }
            memoizedStates[state] = answer
        }
        return answer
    }

    data class TunnelPretty(val id: String, val flow: Int, val connections: List<String>)
    data class Tunnel(val id: Int, val flow: Int, val connections: List<Int>)

    //--------------------------------------------------
    // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    // Valve HH has flow rate=22; tunnel leads to valve GG
    private val inputRegex =
        """Valve ([A-Z][A-Z]) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()

    private fun String.toTunnel(): TunnelPretty {
        val (from, rate, to) = inputRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        return TunnelPretty(from, rate.toInt(), to.split(", ").map { it })
    }

    private fun List<String>.toTunnelMap(): Map<String, TunnelPretty> = buildMap {
        this@toTunnelMap.map { s ->
            s.toTunnel()
        }.forEach {
            this[it.id] = it
        }
    }

    // convert the String tunnel ids to numbers so that they can be mashed into a key for the state
    private fun Map<String, TunnelPretty>.toMapById(): Map<Int, Tunnel> = buildMap {
        val idsIndex = this@toMapById.sortedIds()
        val newList = this@toMapById.values.map { s -> s.toTunnelWithIds(idsIndex) }
        newList.forEach {
            this[it.id] = it
        }
    }

    // a version of the tunnel where the strings are replaced with the id integer of each tunnel
    private fun TunnelPretty.toTunnelWithIds(ids: List<String>): Tunnel =
        Tunnel(ids.indexOf(id), flow, connections.map { ids.indexOf(it) })

    // A list of ids sorted correctly
    private fun Map<String, TunnelPretty>.sortedIds(): List<String> = buildList {
        // we need AA to be first and then the only valves that can be opened is 1-16 the rest have flow 0 because the data is like that
        this += this@sortedIds.values.sortedByDescending { it.flow }.map { it.id }
        this -= "AA"
        this.add(0, "AA")
    }

    //--------------------------------------------------
    // used to visualise the tunnel map
    private fun List<String>.toMermaid(): List<String> {
        val connections = this.map { s ->
            s.toTunnel()
        }.flatMap { t ->
            t.connections.map { "    ${t.id}-->$it" }
        }
        return listOf("stateDiagram-v2") + connections
    }
}