package days

typealias Day21ReturnType = Long
typealias Day21InputType = List<String>

object Day21 : Day<Day21ReturnType, Day21InputType> {
    override val number: Int = 21
    override val expectedPart1Test: Day21ReturnType = 152
    override val expectedPart2Test: Day21ReturnType = -1
    override var useTestData = true

    override fun part1(input: Day21InputType): Day21ReturnType {
        val monkeyMap = input.filter { it.isNotEmpty() }.toMonkeys()
        val rootMonkey = monkeyMap["root"] // I am root
        return rootMonkey?.shout(monkeyMap) ?: -1L
    }

    override fun part2(input: Day21InputType): Day21ReturnType {
        val monkeyMap = input.filter { it.isNotEmpty() }.toMonkeys(true)
        val rootMonkey = monkeyMap["root"]
        val human = monkeyMap["humn"]
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
                    this[name] = BranchMonkey(name, first, second, operator.toOperation())
                }
            }
        }
        return monkeyMap
    }

    private val leafMonkeyRegex =
        """([a-z][a-z][a-z][a-z]): (\d+)""".toRegex()

    private val branchMonkeyRegex =
        """([a-z][a-z][a-z][a-z]): ([a-z][a-z][a-z][a-z]) (.) ([a-z][a-z][a-z][a-z])""".toRegex()

    private fun String.toOperation(): (Long, Long) -> (Long) =
        when (this) {
            "+" -> { i, j -> i + j }
            "-" -> { i, j -> i - j }
            "*" -> { i, j -> i * j }
            "/" -> { i, j -> i / j }
            else -> error("oops")
        }

    open class Monkey(val name: String) {
        fun shout(monkeyMap: Map<String, Monkey>): Long = when (this) {
            is LeafMonkey -> this.value
            is BranchMonkey -> operation(
                monkeyMap[first]?.shout(monkeyMap) ?: error("missing Monkey"),
                monkeyMap[second]?.shout(monkeyMap) ?: error("missing Monkey")
            )

            else -> error("Weird Monkey")
        }
    }

    class LeafMonkey(name: String, val value: Long) : Monkey(name)
    class BranchMonkey(
        name: String,
        val first: String,
        val second: String,
        val operation: (Long, Long) -> (Long)
    ) : Monkey(name)

    class Human(name: String, var missingNumber: Long = -1L) : Monkey(name)

}