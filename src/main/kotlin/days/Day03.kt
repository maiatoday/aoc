package days
import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.*
import readInput
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
open class Day03{

    private var input: List<String> = emptyList()
    @Setup
    fun setUp() {
//        input = readInput(3, "Day_test")
        input = readInput(3, "Day")
    }

    @Benchmark
    fun part1(): Long = input.map { rucksack ->
        rucksack.substring(0 until rucksack.length / 2).toCharArray() to rucksack.substring(rucksack.length / 2).toCharArray()
    }
        .flatMap { (first, second) -> first intersect second.toSet() }
        .sumOf { it.toScore() }.toLong()

    // In the test example, the priority of the item type that appears in both compartments
    // of each rucksack is 16 (p), 38 (L), 42 (P), 22 (v), 20 (t), and 19 (s); the sum of these is 157.
    private fun Char.toScore(): Int =
        if (this.isUpperCase()) {
            this.code - 'A'.code + 27
        } else {
            this.code - 'a'.code + 1
        }

    @Benchmark
    fun part2(): Long =
        input.chunked(3)
            .map { elfGroup ->
                elfGroup.zipWithNext()
                    .map { (first, second) ->
                        first.toCharArray() intersect second.toSet()
                    }
            }.flatMap { sharedItems ->
                // index safe because we chunked by 3 and then did zipWithNext
                sharedItems[0] intersect sharedItems[1]
            }.sumOf { it.toScore() }.toLong()

}