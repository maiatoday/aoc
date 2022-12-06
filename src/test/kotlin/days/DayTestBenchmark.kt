package days

import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.*
import readInput
import readInputString
import readInputStringRepeat
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
open class DayBenchmark{

    private var input: String = ""
    private val day = Day06
    @Setup
    fun setUp() {
        input = readInputStringRepeat(day.number, "Day", 10000)
    }

    @Benchmark
    fun part1(): Int = day.part1(input)

    @Benchmark
    fun part2(): Int = day.part2(input)

    @Benchmark
    fun part1Alternative(): Int = day.part1Alt(input)

    @Benchmark
    fun part2Alternative(): Int = day.part2Alt(input)

}