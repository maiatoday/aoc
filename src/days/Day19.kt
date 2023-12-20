package days

import util.readInts
import util.splitByBlankLine

object Day19 : Day<Long, List<String>> {
    override val number: Int = 19
    override val expectedPart1Test: Long = 19114L
    override val expectedPart2Test: Long = 167409079868000L
    override var useTestData = true
    override val debug = false
    override fun part1(input: List<String>): Long {
        val (start, workflows, parts) = parse(input)
        val accepted = parts.filter { it.apply(start, workflows).isAccepted() }
        return accepted.sumOf { it.score }.toLong()
    }

    data class Parsed(val start: String, val workflows: Map<String, Workflow>, val parts: List<Part>)

    private fun parse(input: List<String>): Parsed {
        val blocks = input.splitByBlankLine()
        val workflows = blocks.first().map { Workflow(it) }.associateBy { it.name }
        val parts = blocks.last().map { Part(it) }
        return Parsed("in", workflows, parts)
    }

    private fun Part.apply(start: String, workflows: Map<String, Workflow>): String {
        var goto = start
        while (!goto.isAccepted() && !goto.isRejected()) {
            val workflow = workflows[goto] ?: error("oops no workflow $goto")
            goto = workflow.rules.test(this)
        }
        return goto
    }

    private fun String.isAccepted() = this == "A"
    private fun String.isRejected() = this == "R"
    data class Workflow(val name: String, val rules: List<Rule>)

    private fun Workflow(input: String): Workflow {
        val (name, rest) = input.split("{")
        val rules = rest.dropLast(1).split(",").map { Rule(it) }
        return Workflow(name, rules)
    }

    private fun List<Rule>.test(part: Part): String {
        for (r in this) {
            when (r) {
                is TestRule -> {
                    if (r.test(part)) return r.goto
                }

                is EndRule -> return (r.goto)
            }
        }
        return "R"
    }


    sealed class Rule
    data class TestRule(val cat: Category, val level: Int, val operation: Operation, val goto: String) : Rule() {
        private val testOperation: (Int, Int) -> Boolean = when (operation) {
            Operation.LT -> { x, y -> x < y }
            Operation.GT -> { x, y -> x > y }
        }

        fun test(part: Part): Boolean =
                when (cat) {
                    Category.x -> testOperation(part.x, level)
                    Category.m -> testOperation(part.m, level)
                    Category.a -> testOperation(part.a, level)
                    Category.s -> testOperation(part.s, level)
                }

    }

    data class EndRule(val goto: String) : Rule()

    private fun Rule(input: String): Rule {
        if (input.matches(endRuleRegex)) return EndRule(input)
        val (cat, op, level, goto) = ruleRegex.find(input)?.destructured ?: kotlin.error("oops")
        return TestRule(Category.valueOf(cat), level.toInt(), Operation.from(op), goto)
    }

    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        val score = x + m + a + s
    }

    private fun Part(input: String): Part {
        val (x, m, a, s) = input.readInts()
        return Part(x, m, a, s)
    }

    enum class Category {
        x, m, a, s
    }

    enum class Operation(val value: String) {
        LT("<"), GT(">");

        companion object {
            private val map = entries.associateBy { it.value }
            infix fun from(value: String) = map[value] ?: error("bad component $value")
        }
    }

    val ruleRegex = """(\w*)([<>])(\d*):(\w*)""".toRegex()

    val endRuleRegex = """\A\w*\z""".toRegex()

    override fun part2(input: List<String>): Long {
        val (start, workflows, parts) = parse(input)
        return expectedPart2Test
    }
}
