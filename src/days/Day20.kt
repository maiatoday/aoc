package days

object Day20 : Day<Long, List<String>> {
    override val number: Int = 20

    override val expectedPart1Test: Long = 11687500L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    private val deque = ArrayDeque<Signal>()
    private var circuit = mapOf<String, Module>()

    fun sendToCircuit(source: String, destination: String,  signal: Boolean) {
        if (debug) {
            val level = if (signal) "high" else "low"
            println("$source -$level -> $destination")
        }
        deque.add(Signal(source, destination, signal))
    }

    data class Signal(val source: String, val destination: String, val signal: Boolean)

    override fun part1(input: List<String>): Long {
        circuit = readCircuit(input)
        setInputStates(circuit)
        val broadcast = circuit[BROADCASTER] as Broadcast
        repeat(1000) {
            broadcast.pressButton()
            while (deque.isNotEmpty()) {
                val (source, destination, signal) = deque.removeFirst()
                circuit[destination]?.pulse(source, signal)
            }
        }
        val high = circuit.values.sumOf { it.highCount }
        val low = circuit.values.sumOf { it.lowCount }
        return high * low
    }

    private fun readCircuit(input: List<String>) =
            input.map { it.toModule() }.associateBy { it.name }

    private fun setInputStates(circuit: Map<String, Module>) {
        circuit.values.filterIsInstance<Con>().forEach { con ->
            findInputs(circuit, con.name).forEach { con.addInput(it) }
        }
    }

    private fun findInputs(circuit: Map<String, Module>, module: String) = circuit.values.filter { module in it.outputs }.map { it.name }

    private fun String.toModule(): Module {
        val (source, dest) = this.split(" -> ")
        val destinations = if (dest.contains(",")) dest.split(", ") else listOf(dest)
        return when {
            source == BROADCASTER -> Broadcast(BROADCASTER, destinations)
            source.startsWith("%") -> FlipFlop(source.drop(1), destinations)
            source.startsWith("&") -> Con(source.drop(1), destinations)
            else -> error("oops bad source $source")
        }
    }

    sealed class Module(
            val name: String,
            val outputs: List<String>,
            val sendToCircuit: (source: String, destination: String, signal: Boolean) -> Unit = Day20::sendToCircuit) {
        var highCount: Long = 0L
        var lowCount: Long = 0L
        abstract fun pulse(from: String, signal: Boolean)
        abstract fun debug(): String

        fun send(signal: Boolean) {
            if (signal) highCount += outputs.size else lowCount += outputs.size
            outputs.forEach { sendToCircuit(name, it, signal) }
        }

        fun isBroadcaster() = name == BROADCASTER

    }

    class FlipFlop(name: String, outputs: List<String>) : Module(name, outputs) {

        private var state: Boolean = false
        override fun pulse(from: String, signal: Boolean) {
            if (!signal) {
                state = !state
                send(state)
            }

        }

        override fun debug(): String = "%$name -> ${outputs.joinToString(", ")} ... state $state"

    }

    class Con(name: String, outputs: List<String>) : Module(name, outputs) {
        private val inputs = mutableMapOf<String, Boolean>()

        fun addInput(input: String) {
            inputs.put(input, false)
        }

        override fun pulse(from: String, signal: Boolean) {
            inputs[from] = signal
            val newSignal = !(inputs.values.all { it })
            send(newSignal)
        }

        override fun debug(): String = "&$name -> ${outputs.joinToString(", ")}"

    }

    class Broadcast(name: String, outputs: List<String>) : Module(name, outputs) {

        fun pressButton() {
            lowCount++
            if (debug) println("Button -low -> broadcaster")
            pulse("button", false)
        }

        override fun pulse(from: String, signal: Boolean) {
            send(signal)
        }

        override fun debug(): String = "$name -> ${outputs.joinToString(", ")}"

    }

    override fun part2(input: List<String>): Long {
        deque.clear() // just in case
        circuit = readCircuit(input)
        setInputStates(circuit)
        var buttonCount = 1L
        val broadcast = circuit[BROADCASTER] as Broadcast
        val rxInputs = findInputs(circuit, ACTIVATOR).flatMap { findInputs(circuit, it) }
        val cycleCounts = mutableMapOf<String, Long>()
        stopLoop@ while (true) {
            broadcast.pressButton()
            while (deque.isNotEmpty()) {
                val (source, destination, signal) = deque.removeFirst()
                if (signal && source in rxInputs) {
                    cycleCounts[source] = buttonCount
                }
                if (cycleCounts.size == rxInputs.size) break@stopLoop
                circuit[destination]?.pulse(source, signal)
            }
            buttonCount++
        }
        return cycleCounts.values.fold(1L) { acc, l -> acc * l }
    }

    const val BROADCASTER: String = "broadcaster"
    const val ACTIVATOR: String = "rx"
}
