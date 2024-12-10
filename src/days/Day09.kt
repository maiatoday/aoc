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
        //  var forwardIndex = 0 // optimise by moving forward
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
                    // need a break in here to detect if there are no more holes or to detect if we start moving forward
                }
            }
            //dd.debug()
        }
        // it doesn't stop and then moves the last valid element up by one and then all of them up by one
        /*
        at the point of failure the array does this
[0,0,9,9,8,1,1,1,8,8,8,2,7,7,7,3,3,3,6,4,4,6,5,5,5,5,6,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,]
[0,0,9,9,8,1,1,1,8,8,8,2,7,7,7,3,3,3,6,4,4,6,5,5,5,5,6,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,]
[0,0,9,9,8,1,1,1,8,8,8,2,7,7,7,3,3,3,6,4,4,6,5,5,5,5,-1,6,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,]
         */
        return dd.toList().drop(1) //TODO FIX SUPER HACKY OFF BY ONE my array has a -1 as a first element!!!!!
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
        val expandedDisk = expand(disk)
        val defrag = defragByFile(expandedDisk, maxId)
        val checksum = checksum(defrag)
        return checksum
    }

    private fun defragByFile(disk: List<Int>, maxId: Int): List<Int> {
        val dd = disk.toIntArray()
        // loop backwards from maxId to 0
        for (id in maxId downTo 0) {
            // find the start of the file
            val start = dd.indexOfFirst { it == id }
            val size = dd.indexOfLast { it == id } - start
            // loop forwards finding an open spot
            val gap = dd.indexOfGap(size)
            if (gap != -1) {
                // if found move the file
                var gg = gap
                for (i in start..start + size) {
                    dd[gg] = dd[i]
                    dd[i] == -1
                    gg++
                }
            }
        }
        return dd.toList()
    }

    fun IntArray.indexOfGap(length: Int): Int {
        var pos = 0
        while (pos < this.size) {
            if ((pos + length) > this.size) return -1
            for (i in pos..pos + length) {
                if (this[i] != -1) {
                    pos = pos+i
                    break
                }
            } // need to find the success case here
        }
        return -1
    }
}