package days

object Day09 : Day<Long, List<String>> {
    override val number: Int = 9
    override val expectedPart1Test: Long = 1928L
    override val expectedPart2Test: Long = 2858L
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
        val dd = disk.toMutableList()
        for (i in dd.size - 1 downTo 0) {
            val id = dd[i]
            if (id != -1) {
                // find first empty spot from the beginning ii
                for (ii in 0..<dd.size) {
                    if (ii > i) continue
                    if (dd[ii] == -1) {
                        dd[ii] = id //move it
                        dd[i] = -1 //clear the original spot
                        break
                    }
                }
            }
        }
        return dd.toList()
    }

    fun IntArray.debug() {
        print("[")
        for (i in this.indices) {
            print("${this[i]},")
        }
        println("]")
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
        val disk = parse(input.first())
        val maxId = disk.size - 1
        val fileRecords = buildAmphiFiles(disk)
        val expandedDisk = expand(disk)
        val defrag = defragByFile(expandedDisk, maxId, fileRecords)
        val checksum = checksum(defrag)
        return checksum
    }

    private fun buildAmphiFiles(disk: List<Pair<Int, Int>>): Map<Int, AmphiFile> = buildMap {
        disk.mapIndexed { i, (b, _) ->
            put(i, AmphiFile(i, b))
        }
    }

    data class AmphiFile(val id: Int, val size: Int)

    private fun defragByFile(disk: List<Int>, maxId: Int, fileRecords: Map<Int, AmphiFile>): List<Int> {
        val dd = disk.toMutableList()
        // loop backwards from maxId to 0
        for (id in maxId downTo 0) {
            // find the start of the file
            val start = dd.indexOfFirst { it == id }
            val size = fileRecords[id]?.size ?: 0
            // loop forwards finding an open spot
            val gap = dd.indexOfGap(size)
            if (gap != -1 && gap < start) {
                // if found move the file
                var gg = gap
                for (i in start..start + size-1) {
                    dd[gg] = dd[i]
                    gg++
                    dd[i] = -1
                }
            }
        }
        return dd.toList()
    }

    fun MutableList<Int>.indexOfGap(length: Int): Int = this.asSequence()
        .withIndex()
        .windowed(length)
        .firstOrNull { sublist ->
            sublist.map { it.value }.all { it == -1 }
        }?.first()?.index ?: -1
}