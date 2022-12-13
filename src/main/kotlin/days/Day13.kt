package days

typealias ReturnValue = Int

object Day13 : Day<ReturnValue, List<String>> {
    override val number: ReturnValue = 13
    override val expectedPart1Test: ReturnValue = -1
    override val expectedPart2Test: ReturnValue = -1

    override fun part1(input: List<String>): ReturnValue {
        val messagePairs = parseInput(input)
        return expectedPart1Test
    }

    private fun parseInput(input: List<String>) = input.filter { it.isNotEmpty() && !it.startsWith("#") }
        .map { s ->
            s.toMessage()
        }
        .chunked(2)

    override fun part2(input: List<String>): ReturnValue {
        return expectedPart2Test
    }

    private fun String.toMessage() = parse(this).also { println("\n  -- ") }

    private fun parse(s: String): MList {
        var currentNode: MList? = null
        var i = 0
        while (i < s.length) {
            val c = s[i]
            when {
                c == '[' -> {
                    i++
                    val newLst = MList(currentNode)
                    currentNode?.list?.add(newLst)
                    currentNode = newLst
                }

                c == ',' -> {
                    i++
                }

                c == ']' -> {
                    i++
                    if (currentNode?.parent != null) {
                        currentNode = currentNode.parent
                    }
                }

                c.isDigit() -> {
                    var count = 1
                    var n = c.toString()
                    for (d in s.substring(i + 1)) {
                        if (d.isDigit()) {
                            n += c
                            count++
                        } else {
                            break
                        }
                    }

                    i += count
                    currentNode?.list?.add(MNumber(parent = currentNode, n.toInt()))

                }

                else -> error("Oops $c in $s")
            }
        }
        currentNode?.beep()
        return currentNode ?: error("bad parse")
    }

    sealed class Message {
        abstract val parent: MList?
        val debug = true
        abstract fun beep()
    }

    class MList(
        override val parent: MList?, var list: MutableList<Message> = mutableListOf()
    ) : Message() {
        override fun beep() {
            if (debug) {
                print("[")
                list.forEach {
                    it.beep()
                    print(",")
                }
                print("]")
            }
        }

    }

    operator fun MList.compareTo(other: MList): Int {
        return 0
    }

    data class MNumber(
        override val parent: MList?, var value: Int
    ) : Message() {
        override fun beep() {
            if (debug) print(value)
        }
    }

    operator fun MNumber.compareTo(other: MNumber): Int = this.value.compareTo(other.value)

}


