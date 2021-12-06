import java.math.BigInteger

// Day object template
object Day06 {

    fun part1(input: List<String>): Int {
        val days = 80
        val school = mutableListOf<LanternFish>()
        school.addAll(input.first().trim().split(",").map { LanternFish(it.toLong()) })
        val babies = mutableListOf<LanternFish>()
        for (day in 1 until days) {
            babies.clear()
            school.forEach { fish ->
                val baby = fish.dayTick()
                baby?.let {
                    babies.add(it)
                }
            }
            school.addAll(babies)
        }
        return school.size
    }

    fun part2(input: List<String>): Int {
        val days = 256
        // split it into array of BigIntegers counting fish at a timer day.
        // set from input array
        // decrement means bins shift ie count in bin for 3 days becomes count for bin in 2 days
        // count for bin in 0 days becomes new count for bin in 8 days
        // some tricksy ness with the 8 days vs 6 day counts
        // answer is addition of all counts

        // so it is the number of days mapped to the array index that moves and the 0 day one that gets addition in it
        val schoolCount = List(9) {
            FishCountDay(BigInteger("0"), it)
        }
        input[0].trim().split(",").map { it.toInt() }.forEach{
            // starting off index 0 is day 0
            schoolCount[it].count.inc()
            schoolCount[it].day = it
        }
        for (days in 1 until days) {
            schoolCount.forEach {
                if (it.day == 0) it.day = 6
                else it.day--
            }
            val zeroDay = schoolCount.find {it.day == 0}
            // take the count of zeroDay and add it to an 8 Day
        }
        // could be done with a reduce
        val total = BigInteger("0")
        schoolCount.forEach() {
            total.add(it.count)
        }
        return total.toInt()
    }

    data class FishCountDay(var count: BigInteger, var day: Int)

    class LanternFish(private var dayCount: Long) {
        fun dayTick(): LanternFish? {
            if (dayCount == 0L) dayCount = 6
            else dayCount--
            if (dayCount == 0L) {
                return LanternFish(9) // 9 not 8 so it decrements after the next day
            }
            return null
        }
    }
}
