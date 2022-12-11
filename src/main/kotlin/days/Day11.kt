package days

import java.math.BigInteger

typealias MonkeySwap = Pair<Int, List<BigInteger>>

object Day11 : Day<BigInteger, String> {
    override val number: Int = 11
    override val expectedPart1Test: BigInteger = BigInteger.valueOf(10605)
    override val expectedPart2Test: BigInteger = BigInteger.valueOf(2713310158)

    data class Monkey(
        val items: MutableList<BigInteger>,
        val operation: (BigInteger, BigInteger) -> (BigInteger),
        val test: (BigInteger) -> (Boolean),
        val testValue: BigInteger,
        val trueMonkey: Int,
        val falseMonkey: Int,
        val worryDrop: (BigInteger) -> (BigInteger) = { it / BigInteger.valueOf(3) },
        var inspectionCount: Long = 0
    ) {
        fun round(
            doWorryDrop: Boolean = false,
            magicClampNumber: BigInteger = BigInteger.ONE
        ): List<MonkeySwap> {
//            val itemValues = items.map { operation(it) }.map { if (doWorryDrop) worryDrop(it) else it }
            val itemValues =
                if (doWorryDrop) items.map { operation(it, magicClampNumber) }
                    .map { worryDrop(it) } else items.map { operation(it, magicClampNumber) }
            inspectionCount += itemValues.size
            val trueList = itemValues.filter { test(it) }
            val falseList = itemValues.filter { !test(it) }
            items.clear()
            return listOf(MonkeySwap(trueMonkey, trueList), MonkeySwap(falseMonkey, falseList))
        }

        fun catchItems(newItems: List<BigInteger>) {
            items += newItems
        }

    }

    private fun List<String>.toMonkey(): Monkey {
        val items = valueForTerm("Starting").split(", ").map { it.toBigInteger() }.toMutableList()
        val test: Long = valueForTerm("Test").split(" by ").last().toLong()
        val trueMonkey: Int = valueForTerm(" true").split(" monkey ").last().toInt()
        val falseMonkey: Int = valueForTerm(" false").split(" monkey ").last().toInt()
        val operationString: String = valueForTerm(" Operation").split("new = ").last()
        return Monkey(
            items = items,
            operation = operationString.toOperation(),
            test = { it.mod(test.toBigInteger()) == BigInteger.ZERO },
            testValue = test.toBigInteger(),
            trueMonkey = trueMonkey,
            falseMonkey = falseMonkey,
            worryDrop = { it / BigInteger.valueOf(3) }
        )
    }

    private fun List<String>.valueForTerm(term: String) = this
        .first {
            it.contains(term)
        }
        .split(": ").last()

    private fun String.toOperation(): (BigInteger, BigInteger) -> BigInteger {
        val term2String = this.split("old ").last().substring(2)
        val term2: BigInteger = if (term2String == "old") BigInteger.ZERO else term2String.toBigInteger()
        return when {
            (this == "old * old") -> { x: BigInteger, clamp: BigInteger -> if (x.divBy(clamp)) x else (x * x) }
            (this.startsWith("old + ")) -> { x: BigInteger, _: BigInteger -> x + term2 }
            (this.startsWith("old * ")) -> { x: BigInteger, _: BigInteger -> if (x.divBy(term2)) x else x * term2 }
            else -> {
                error("Oops!")
            }
        }
    }

    fun BigInteger.divBy(i: BigInteger): Boolean = this.mod(i) == BigInteger.ZERO

    private fun extractMonkeys(input: String, worryDivisor: Int = 3): List<Monkey> =
        input.split("\n\n").map { it.lines() }.map { it.toMonkey() }

    override fun part1(input: String): BigInteger {
        val monkeys: List<Monkey> = extractMonkeys(input)
        val magicClampNumber = monkeys.map { it.testValue }.reduce { acc, i -> i * acc }
        // monkeys.inspect()
        println("====== And Go!")
        repeat(20) { round ->
            //   println("Round $round")
            monkeyRound(monkeys, true, magicClampNumber)
            //   monkeys.inspect()
        }
        val topMonkeys = monkeys.map { it.inspectionCount }.sorted().takeLast(2)
        return topMonkeys.first().toBigInteger() * topMonkeys.last().toBigInteger()
    }

    private fun monkeyRound(monkeys: List<Monkey>, doWorryDrop: Boolean, magicClampNumber: BigInteger) {
        monkeys.forEachIndexed() { index, monkey ->
            val swaps = monkey.round(doWorryDrop, magicClampNumber)
//            println("----Before Throw----")
//            monkey.inspect(index)
//            swaps.inspect(index)
            swaps.forEach { swap ->
                monkeys[swap.first].catchItems(swap.second)
            }

        }
    }

    override fun part2(input: String): BigInteger {
        val roundCheck = listOf(0, 1, 3, 4, 5, 6, 20, 700, 999, 1999, 2999)// 19, 20, 21, 1999, 2999, 3999)
        val monkeys: List<Monkey> = extractMonkeys(input, 1)
        val magicClampNumber = monkeys.map { it.testValue }.reduce { acc, i -> i * acc }
        monkeys.inspect()
        repeat(2000) { round ->
            // println("==============Round $round")
            monkeyRound(monkeys, false, magicClampNumber)
           // monkeys.inspect()
            if (round in roundCheck) {
                println("== After Round $round ==")
              //  monkeys.inspectCount()
              //  monkeys.inspect()
            }
        }
        println("***** Final result *****")
        monkeys.inspectCount()
        val topMonkeys = monkeys.map { it.inspectionCount }.sorted().takeLast(2)
        return topMonkeys.first().toBigInteger() * topMonkeys.last().toBigInteger()
    }

    //================ inspect methods


    private fun List<Monkey>.inspectCount() = this.forEachIndexed { index, monkey ->
//        print("Monkey $index: true=${monkey.trueMonkey} false=${monkey.falseMonkey} ")
        println("Monkey $index inspected items ${monkey.inspectionCount} times.")
    }

    private fun List<MonkeySwap>.inspect(index: Int) {
        println("From $index True: throws to ${this[0].first}  items: ${this[0].second}")
        println("From $index False: throws to ${this[1].first}  items: ${this[1].second}")
    }

    private fun List<Monkey>.inspect() = this.forEachIndexed { index, monkey ->
//        print("Monkey $index: true=${monkey.trueMonkey} false=${monkey.falseMonkey} ")
        monkey.inspect(index)
    }

    private fun Monkey.inspect(index: Int) {
        println("Monkey $index: inspected ${this.inspectionCount} ${this.items} ")
    }
}

