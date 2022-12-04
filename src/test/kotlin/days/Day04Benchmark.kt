package days
import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.*
import readInput
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
open class Day04Benchmark{

    private var input: List<String> = emptyList()
    @Setup
    fun setUp() {
        input = readInput(4, "Day")
    }

    @Benchmark
    fun part1(): Long = Day04.part1(input)

    @Benchmark
    fun part2(): Long = Day04.part2(input)

    @Benchmark
    fun part1Set(): Long = Day04.part1Set(input)

    @Benchmark
    fun part2Set(): Long = Day04.part2Set(input)

}
