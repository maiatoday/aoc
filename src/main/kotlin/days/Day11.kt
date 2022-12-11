package days

typealias MonkeySwap = Pair<Int, List<Long>>

object Day11 : Day<Long, String> {
    override val number: Int = 11
    override val expectedPart1Test: Long = 10605L
    override val expectedPart2Test: Long = 2713310158L

    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> (Long),
        val test: (Long) -> (Boolean),
        val testValue: Long,
        val trueMonkey: Int,
        val falseMonkey: Int,
        val worryDrop: (Long) -> (Long) = { it / 3 },
        var inspectionCount: Long = 0
    ) {
        fun round(
            doWorryDrop: Boolean = false,
            magicClampNumber: Long = 1L
        ): List<MonkeySwap> {
//            val itemValues = items.map { operation(it) }.map { if (doWorryDrop) worryDrop(it) else it }
            val itemValues =
                if (doWorryDrop) items.map { operation(it) }
                    .map { worryDrop(it) } else items.map { operation(it) % magicClampNumber }
            inspectionCount += itemValues.size
            val trueList = itemValues.filter { test(it) }
            val falseList = itemValues.filter { !test(it) }
            items.clear()
            return listOf(MonkeySwap(trueMonkey, trueList), MonkeySwap(falseMonkey, falseList))
        }

        fun catchItems(newItems: List<Long>) {
            items += newItems
        }

    }

    private fun List<String>.toMonkey(): Monkey {
        val items = valueForTerm("Starting").split(", ").map { it.toLong() }.toMutableList()
        val test: Long = valueForTerm("Test").split(" by ").last().toLong()
        val trueMonkey: Int = valueForTerm(" true").split(" monkey ").last().toInt()
        val falseMonkey: Int = valueForTerm(" false").split(" monkey ").last().toInt()
        val operationString: String = valueForTerm(" Operation").split("new = ").last()
        return Monkey(
            items = items,
            operation = operationString.toOperation(),
            test = { (it % test.toLong()) == 0L },
            testValue = test.toLong(),
            trueMonkey = trueMonkey,
            falseMonkey = falseMonkey,
            worryDrop = { it / 3 }
        )
    }

    private fun List<String>.valueForTerm(term: String) = this
        .first {
            it.contains(term)
        }
        .split(": ").last()

    private fun String.toOperation(): (Long) -> Long {
        val term2String = this.split("old ").last().substring(2)
        val term2: Long = if (term2String == "old") 0L else term2String.toLong()
        return when {
            // (this == "old * old") -> { x: Long -> if (x.divBy(x)) x else (x * x) }
            (this == "old * old") -> { x: Long  ->
                x * x // <==== here get prime factors of x and return that
            }

            (this.startsWith("old + ")) -> { x: Long -> x + term2 }
            (this.startsWith("old * ")) -> { x: Long ->  x * term2 }
            else -> {
                error("Oops!")
            }
        }
    }

    private fun extractMonkeys(input: String, worryDivisor: Int = 3): List<Monkey> =
        input.split("\n\n").map { it.lines() }.map { it.toMonkey() }

    override fun part1(input: String): Long {
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
        return topMonkeys.first().toLong() * topMonkeys.last().toLong()
    }

    private fun monkeyRound(monkeys: List<Monkey>, doWorryDrop: Boolean, magicClampNumber: Long) {
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

    override fun part2(input: String): Long {
        val roundCheck = listOf(
            0,
            1,
            3,
            4,
            5,
            6,
            19,
            20,
//            700,
//            999,
//            1999,
//            2999,
//            3999,
//            4999,
//            5999,
//            6999,
//            7999,
//            8999,
            9999
        )// 19, 20, 21, 1999, 2999, 3999)
        val monkeys: List<Monkey> = extractMonkeys(input, 1)
        val magicClampNumber = monkeys.map { it.testValue }
            .also { println(it) }
            .reduce { acc, i -> i * acc }
        monkeys.inspect()
        repeat(10000) { round ->
            // println("==============Round $round")
            monkeyRound(monkeys, false, magicClampNumber)
            // monkeys.inspect()
            if (round in roundCheck) {
                println("== After Round $round ==")
                monkeys.inspectCount()
                //  monkeys.inspect()
            }
        }
        println("***** Final result *****")
        monkeys.inspectCount()
        val topMonkeys = monkeys.map { it.inspectionCount }.sorted().takeLast(2)
        return topMonkeys.first().toLong() * topMonkeys.last().toLong()
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

