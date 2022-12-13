package days

import kotlin.math.min

typealias ReturnType = Int

object Day13 : Day<ReturnType, List<String>> {
    override val number: ReturnType = 13
    override val expectedPart1Test: ReturnType = 13
    override val expectedPart2Test: ReturnType = 140

    override fun part1(input: List<String>): ReturnType {
        val messagePairs = parseInput(input)
        var index = 1
        var total = 0
        for (p in messagePairs) {
            print("Comparing...")
            p[0].beep()
            print(" -> ")
            p[1].beep()
            if (p[0] <= p[1]) {
                total += index
                println("\nPair $index  ✅right order! total:$total")
            } else {
                println("\nPair $index  ❌not right order! total:$total")
            }
            index++
        }
        return total
    }

    override fun part2(input: List<String>): ReturnType {


        val messages = input.filter { it.isNotEmpty() && !it.startsWith("#") }
            .map { s -> s.toMessage() }
        val divider1 = "[[2]]".toMessage()
        val divider2 = "[[6]]".toMessage()
        // try with a sorted list but there is something weird going on with hashcode and equals
        // when I try to find the nodes by index in the sorted list if I add the dividers it is unhappy
        // too late now to debug
        val sortedMessages =
            messages.sorted() //TODO either fix the == thing or remove sorted.
        var index1 = 1
        var index2 = 1
        for (m in sortedMessages) {
            // println()
            //m.beep()
            if (m < divider1) index1++
            if (m < divider2) index2++

        }
        index2 += 1 // need to add 1 because the first divider isn't in the list

        return index1 * index2
    }

    private fun parseInput(input: List<String>) = input.filter { it.isNotEmpty() && !it.startsWith("#") }
        .map { s ->
            s.toMessage()
        }
        .chunked(2)


    private fun String.toMessage() = parse(this)//.also { println("\n  -- ") }

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
        // currentNode?.beep()
        return currentNode ?: error("bad parse")
    }

    sealed class Message : Comparable<Message> {
        abstract val parent: MList?
        val debug = true
        abstract fun toList(): Message
        abstract fun beep()
    }

    data class MList(
        override val parent: MList?, var list: MutableList<Message> = mutableListOf()
    ) : Message() {
        override fun toList(): Message = this

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

        override fun compareTo(other: Message): Int {
            return when (other) {
                is MNumber -> this.compareTo(other.toList())
                is MList -> {
//                    println("\n        MList CompareTo ..")
//                    print("        this")
                    //                   this.beep()
//                    print(" other ")
                    //                   other.beep()
                    val thisSize = this.list.size
                    val otherSize = other.list.size
                    for (i in 0 until min(thisSize, otherSize))  {
                        if (this.list[i] > other.list[i])  return 1
                        else if (this.list[i] < other.list[i]) return  -1
                    }
                    if (thisSize > otherSize) return 1
                    if (otherSize > thisSize) return -1
                    return 0
                }
            }
        }
    }

    data class MNumber(
        override val parent: MList?, var value: Int
    ) : Message() {

        override fun toList(): MList {
            val m = MList(this.parent)
            m.list.add(this.copy(parent = m))
            return m
        }

        override fun beep() {
            if (debug) print(value)
        }

        override fun compareTo(other: Message): Int {
            return when (other) {
                is MNumber -> this.value.compareTo(other.value)
                is MList -> this.toList().compareTo(other)
            }
        }
    }
}


