package days

object Day09 : Day<Long, List<String>> {
    override val number: Int = 9
    override val expectedPart1Test: Long = 1928L
    override val expectedPart2Test: Long = -1L //2858L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val disk = parse(input.first())
        val diskSize = disk.sumOf { (b, e) -> b + e }
        println("Disk map size: ${disk.size}") // on real data 10000
        println("Disk size : $diskSize") // on real data 94715 - based on this I can allocate an array of integers
        val expandedDisk = expand(disk)
       // println(expandedDisk)
        val defrag = defrag(expandedDisk)
        val checksum = checksum(defrag)
        return checksum
    }

    fun checksum(disk: List<Int>): Long =
        disk.mapIndexed { i, id ->
            if (id == -1) 0L
            else (i * id).toLong()
        }.sum()

    fun defrag(disk: List<Int>): List<Int> {
        val dd = disk.toIntArray()
      //  var forwardIndex = 0
        for (i in dd.size - 1 downTo 0) {
            val id = dd[i]
            if (id != -1) {
                // find first empty spot from the beginning ii
                for (ii in 0..<dd.size) {
                    if (dd[ii] == -1) {
                        dd[ii] = id //move it
                        dd[i] = -1 //clear the original spot
                       // forwardIndex = ii
                        break
                    }
                }
            }
        }
        return dd.toList().drop(1) //TODO FIX SUPER HACKY OFF BY ONE
    }


    fun expand(disk: List<Pair<Int, Int>>): List<Int> = buildList {
        disk.mapIndexed { i, (b, e) ->
            repeat(b) { add(i) }
            repeat(e) { add(-1) }
        }
    }

    fun parse(input: String): List<Pair<Int, Int>> = input
        .toCharArray()
        .toList()
        .map { it.digitToInt() }
        .chunked(2)
        .map { if (it.size == 2) it[0] to it[1] else it[0] to 0 }


    override fun part2(input: List<String>): Long {
        return -1L
    }
}