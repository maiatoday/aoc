package days

import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.*
import readInput
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
open class DayBenchmark{

    private var input: List<String> = emptyList()
    private val day = Day04
    @Setup
    fun setUp() {
        input = readInput(day.number, "Day")
    }

    @Benchmark
    fun part1(): Long = day.part1(input)

    @Benchmark
    fun part2(): Long = day.part2(input)

    @Benchmark
    fun part1Alternative(): Long = day.part1Set(input)

    @Benchmark
    fun part2Alternative(): Long = day.part2Set(input)

}