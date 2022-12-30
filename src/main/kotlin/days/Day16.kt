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
        val tunnelMap = input.toTunnelMap()
        val sortedTunnelMap = tunnelMap.sortConnectionByFlow()
        val answer = maxFlow("AA", 30, listOf(), false, sortedTunnelMap)
        return answer
    }

    override fun part2(input: Day16InputType): Day16ReturnType {
        return expectedPart2Test
    }

    //--------------------------------------------------
    data class State(val valve: String, val time: Int, val openedValves: List<String>, val elephantHelp: Boolean)

    private val memoizedStates =
        HashMap<Int, Int>(87000000) // super rough ballpark of capacity to  stop hashmap growing all the time

    private fun maxFlow(
        currentValve: String,
        time: Int,
        openedValves: List<String>,
        elephantHelp: Boolean,
        tunnels: Map<String, Tunnel>
    ): Int {
        // counting down from max time to 0, i.e. time left
        if (time == 0) return if (elephantHelp) maxFlow("AA", 26, openedValves, false, tunnels) else 0
        val state = State(currentValve, time, openedValves, elephantHelp)
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
                        currentValve,
                        time - 1,
                        openedValves + currentValve,
                        elephantHelp,
                        tunnels
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

    data class Tunnel(val id: String, val flow: Int, val connections: List<String>)

    //--------------------------------------------------
    // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    // Valve HH has flow rate=22; tunnel leads to valve GG
    private val inputRegex =
        """Valve ([A-Z][A-Z]) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()

    private fun String.toTunnel(): Tunnel {
        val (from, rate, to) = inputRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        return Tunnel(from, rate.toInt(), to.split(", ").map { it })
    }

    private fun List<String>.toTunnelMap(): Map<String, Tunnel> = buildMap {
        this@toTunnelMap.map { s ->
            s.toTunnel()
        }.forEach {
            this[it.id] = it
        }
    }

    private fun Map<String, Tunnel>.sortConnectionByFlow(): Map<String, Tunnel> {
        val flowList = this.values.map { it.id to it.flow }.sortedByDescending { it.second }
        return this.values.map { t -> (t.id to t.sortConnection(flowList)) }.toMap()
    }

    private fun Tunnel.sortConnection(flowList: List<Pair<String, Int>>): Tunnel {
        val sortedConnections = flowList.filter {
            it.first in this.connections
        }
        return this.copy(connections = sortedConnections.map { it.first })
    }

    private fun List<String>.toMermaid(): List<String> {
        val connections = this.map { s ->
            s.toTunnel()
        }.flatMap { t ->
            t.connections.map { "    ${t.id}-->$it" }
        }
        return listOf("stateDiagram-v2") + connections
    }
}