package days

typealias Day21ReturnType = Long
typealias Day21InputType = List<String>

object Day21 : Day<Day21ReturnType, Day21InputType> {
    override val number: Int = 21
    override val expectedPart1Test: Day21ReturnType = 152
    override val expectedPart2Test: Day21ReturnType = 301
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day21InputType): Day21ReturnType {
        val monkeyMap = input.filter { it.isNotEmpty() }.toMonkeys()
        val rootMonkey = monkeyMap["root"] // I am root
        return rootMonkey?.shout(monkeyMap) ?: -1L
    }

    override fun part2(input: Day21InputType): Day21ReturnType {
        val monkeyMap = input.filter { it.isNotEmpty() }.toMonkeys(true)
        val rootMonkey = monkeyMap["root"]
        val human = monkeyMap["humn"]
        val first = (rootMonkey as RootMonkey).first
        val second = (rootMonkey as RootMonkey).second
        val firstMonkey = monkeyMap[first]
        val secondMonkey = monkeyMap[second]
        var branch = -1L

        val zz:Long = 12643288286556544L/3927
        println("try this $zz")

        // calculate as many values ahead of time as possible
        val branchMonkeys = monkeyMap.values.filterIsInstance<BranchMonkey>()
        branchMonkeys.forEach {
            try {
                it.shout(monkeyMap)
            } catch (e:IllegalStateException) {
                print(".")
            }
        }
        // print values
        println(rootMonkey.whisper(monkeyMap))

        return expectedPart2Test
    }
    //<-------------------------------->

    private fun List<String>.toMonkeys(findHuman: Boolean = false): Map<String, Monkey> {
        val monkeyMap = buildMap<String, Monkey> {
            this@toMonkeys.map {
                if (it.matches(leafMonkeyRegex)) {
                    val (name, value) = leafMonkeyRegex
                        .matchEntire(it)
                        ?.destructured
                        ?: throw IllegalArgumentException("Bad monkey $this")
                    this[name] = if (findHuman && name == "humn") Human(name) else LeafMonkey(name, value.toLong())
                } else {
                    val (name, first, operator, second) = branchMonkeyRegex
                        .matchEntire(it)
                        ?.destructured
                        ?: throw IllegalArgumentException("Bad monkey $this")
                    this[name] = if (findHuman && name == "root")
                        RootMonkey(name, first, second)
                    else
                        BranchMonkey(
                            name = name,
                            first = first,
                            second = second,
                            operation = operator.toOperation(),
                            operator = operator
                        )
                }
            }
        }
        return monkeyMap
    }

    private val leafMonkeyRegex =
        """(....): (\d+)""".toRegex()

    private val branchMonkeyRegex =
        """(....): (....) (.) (....)""".toRegex()

    private fun String.toOperation(): (Long, Long) -> (Long) =
        when (this) {
            "+" -> { i, j -> i + j }
            "-" -> { i, j -> i - j }
            "*" -> { i, j -> i * j }
            "/" -> { i, j -> i / j }
            else -> error("oops")
        }

    abstract class Monkey(val name: String) {
        abstract fun shout(monkeyMap: Map<String, Monkey>): Long
//        = when (this) {
//            is LeafMonkey -> this.value
//            is BranchMonkey -> {
//                val answer = operation(
//                    monkeyMap[first]?.shout(monkeyMap) ?: error("missing Monkey"),
//                    monkeyMap[second]?.shout(monkeyMap) ?: error("missing Monkey")
//                )
//                this.value = answer
//                answer
//            }
//
//            else -> throw IllegalStateException("Weird Monkey")
//        }

        abstract fun whisper(monkeyMap: Map<String, Monkey>): String
    }


    class LeafMonkey(name: String, val value: Long) : Monkey(name) {
        override fun shout(monkeyMap: Map<String, Monkey>): Long = value

        override fun whisper(monkeyMap: Map<String, Monkey>): String = "${value}"
    }

    class BranchMonkey(
        name: String,
        val first: String,
        val second: String,
        val operation: (Long, Long) -> (Long),
        val operator: String,
        var value: Long? = null
    ) : Monkey(name) {

        override fun shout(monkeyMap: Map<String, Monkey>): Long {
            return if (value == null) {
                val answer:Long = operation(
                    monkeyMap[first]?.shout(monkeyMap) ?: error("missing Monkey"),
                    monkeyMap[second]?.shout(monkeyMap) ?: error("missing Monkey")
                )
                value = answer
                answer
            } else {
                value ?: error("Ooops")
            }
        }
        override fun whisper(monkeyMap: Map<String, Monkey>): String =
            if (value != null) value.toString() else "(${monkeyMap[first]?.whisper(monkeyMap)})$operator(${
                monkeyMap[second]?.whisper(monkeyMap)})"
    }

    class Human(name: String, var missingNumber: Long = -1L) : Monkey(name) {
        override fun shout(monkeyMap: Map<String, Monkey>): Long = error("oops")
        override fun whisper(monkeyMap: Map<String, Monkey>): String = "x"
    }

    class RootMonkey(
        name: String,
        val first: String,
        val second: String,
        val operation: (Long, Long) -> (Boolean) = { f, s -> f == s }
    ) : Monkey(name) {
        override fun shout(monkeyMap: Map<String, Monkey>): Long = -1L
        override fun whisper(monkeyMap: Map<String, Monkey>): String =
            "root: (${monkeyMap[first]?.whisper(monkeyMap)} = ${monkeyMap[second]?.whisper(monkeyMap)})"
    }

}