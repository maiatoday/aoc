package days

object Day15 : Day<Long, List<String>> {
    override val number: Int = 15
    override val expectedPart1Test: Long = 1320L
    override val expectedPart2Test: Long = 145L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val snip = input.flatMap {
            it.split(',')
        }
        return snip.sumOf { it.hash() }.toLong()
    }

    private fun String.hash(): Int =
            this.fold(0) { a, c ->
                ((a + c.code) * 17) % 256
            }

    override fun part2(input: List<String>): Long {
        val boxes = Array(256) { emptyList<Lens>() }
        val commands = input.flatMap { it.split(',') }.map { Command(it) }
        commands.forEach {
            boxes.update(it)
        }
        return boxes.mapIndexed { i, l -> (i + 1) * l.calculate() }.sum().toLong()
    }

    private fun Array<List<Lens>>.update(command: Command) {
        val boxIndex = command.boxNumber
        if (command.isDelete) {
            this[boxIndex].filter { it.label == command.label }.forEach { this[boxIndex] = this[boxIndex] - it }
        } else {
            if (this[boxIndex].any { it.label == command.label }) {
                this[boxIndex] = this[boxIndex].map {
                    if (it.label == command.label) it.copy(focalLength = command.value)
                    else it
                }
            } else {
                this[boxIndex] = this[boxIndex] + Lens(command.label, command.value)
            }
        }
    }

    private fun List<Lens>.calculate(): Int =
            this.mapIndexed { i, l ->
                (i + 1) * l.focalLength
            }.sum()


    data class Lens(val label: String, val focalLength: Int)

    data class Command(val label: String, private val operation: Char, val value: Int = 0) {
        val isDelete = operation == '-'
        val boxNumber = label.hash()
    }

    private fun Command(command: String): Command {
        return if (command.contains('-')) {
            val (id, _) = command.split('-')
            Command(id, '-')
        } else {
            val (id, value) = command.split('=')
            Command(id, '=', value.toInt())
        }
    }
}
