package days

typealias Day20ReturnType = Int
typealias Day20InputType = List<String>

object Day20 : Day<Day20ReturnType, Day20InputType> {
    override val number: Int = 20
    override val expectedPart1Test: Day20ReturnType = 3
    override val expectedPart2Test: Day20ReturnType = 1623178306
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day20InputType): Day20ReturnType {
        val itemsOG = input.mapIndexed { index, s -> s.toInt().toItem(index) }
        val items = itemsOG.toMutableList()
        repeat(1) {
            itemsOG.indices.forEach {
                items.mix(it)
            }
        }
        val answer = items.calculate(listOf(1000, 2000, 3000))
        return answer
    }

    val DECRYPTION_KEY = 811589153L

    override fun part2(input: Day20InputType): Day20ReturnType {
        val itemsOG = input.mapIndexed { index, s -> s.toInt().toLItem(index) }
        val items = itemsOG.toMutableList()
        repeat(10) {
            itemsOG.indices.forEach {
                items.lmix(it)
            }
        }
        val answer = items.lcalculate(listOf(1000, 2000, 3000))
        println("Answer long $answer")
        return answer.toInt()
    }

    //    <--------------------------------------->
    data class LItem(val id: Int, val value: Long)
    data class Item(val id: Int, val value: Int)

    private fun Int.toLItem(index: Int) = LItem(index, this * DECRYPTION_KEY)
    private fun Int.toItem(index: Int) = Item(index, this)

    private fun MutableList<Item>.mix(id: Int) {
        val itemIdx = this.indexOfFirst { i -> i.id == id }
        val item = this[itemIdx]
        this.removeAt(itemIdx)
        val newIdx = (((itemIdx + item.value) % this.size) + this.size) % this.size
        if (newIdx == 0) {
            this.add(item)
        } else {
            this.add(newIdx, item)
        }
//        println("id: $id item: $item newIdx $newIdx")
//        this.forEach { print("${it.value}, ") }
//        println()
    }

    private fun List<Item>.calculate(values: List<Int>): Int {
        //  there is only one 0
        val zero = this.indexOfFirst { it.value == 0 }
        val size = this.size
        val xx = values.map {
            // println(it)
            val yy = (zero + it) % size
            //  println(yy)
            this[yy].value
        }
        //    println(xx)
        return xx.sum()
    }

    private fun MutableList<LItem>.lmix(id: Int) {
        val itemIdx = this.indexOfFirst { i -> i.id == id }
        val item = this[itemIdx]
        this.removeAt(itemIdx)
        val newIdx = ((((itemIdx + (item.value % this.size)) % this.size) + this.size) % this.size).toInt()
        if (newIdx == 0) {
            this.add(item)
        } else {
            this.add(newIdx, item)
        }
    }

    private fun List<LItem>.lcalculate(values: List<Int>): Long {
        //  there is only one 0
        val zero = this.indexOfFirst { it.value == 0L }
        val size = this.size
        val xx = values.map {
            // println(it)
            val yy = (zero + it) % size
            //  println(yy)
            this[yy].value
        }
        //    println(xx)
        return xx.sum()
    }
}