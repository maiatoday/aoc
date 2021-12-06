import java.math.BigDecimal
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

    fun part2(input: List<String>): String {
        val days = 256
        val schoolCount = List(9) {
            FishCountDay(BigInteger.ZERO, it)
        }
        input[0].trim().split(",").map { it.toInt() }.forEach {
            // starting off index 0 is day 0
            schoolCount[it].count = schoolCount[it].count.inc()
        }
        for (day in 1..days) {
            schoolCount.forEach { bucket ->
                if (bucket.day == 0) {
                    bucket.day = 8 //babies go directly to 8
                } else bucket.day--
            }
            // the spawn happens here we know there should be as many more parents in day 6 as new babies in 8
            val day8Count = schoolCount.find { parent -> parent.day == 8 }?.count ?: BigInteger.ZERO
            schoolCount.find { parent -> parent.day == 6 }?.let { it.count += day8Count }
        }
        return schoolCount.map { it.count }.fold(BigInteger.ZERO) { total, count -> total + count }.toString()
    }

    // the bucket fish count and days until spawn
    data class FishCountDay(var count: BigInteger, var day: Int)

    // The hungry ones
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
