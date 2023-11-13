package days

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import util.readInputStringRepeat

import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
open class DayBenchmark{

    private var input: String = ""
    private val day = Day99
    @Setup
    fun setUp() {
        input = readInputStringRepeat(day.number, "Day", 10000)
    }

    @Benchmark
    fun part1(): Long = day.part1(emptyList())

    @Benchmark
    fun part2(): Long = day.part2(emptyList())

    @Benchmark
    fun part1Alternative(): Long = day.part1Alt(emptyList())

    @Benchmark
    fun part2Alternative(): Long = day.part2Alt(emptyList())

}