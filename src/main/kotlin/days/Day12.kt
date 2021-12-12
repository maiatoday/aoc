object Day12 {


    fun part1(input: List<String>): Long {
        val collectedPaths = mutableSetOf<String>()
        val paths = parsePaths(input)
        val caveMap = paths.groupBy { it[0] }
        findPaths(caveMap, listOf("start"), collectedPaths)
        return collectedPaths.size.toLong()
    }

    private fun findPaths(
        caveMap: Map<String, List<List<String>>>,
        partialPath: List<String>,
        collectedPaths: MutableSet<String>
    ) {
        if (partialPath.last().isTheEnd()) {
            collectedPaths.add(partialPath.joinToString())
        }
        caveMap[partialPath.last()]?.filter {
            (it[1].isMainCave() or
                    (!it[1].isMainCave() and !it[1].hasBeenVisited(partialPath)) or
                    it[1].isTheEnd())
        }?.forEach { p ->
            findPaths(caveMap, partialPath + p[1], collectedPaths)
        }
    }


    private fun String.isMainCave(): Boolean = this.all { it.isUpperCase() }

    private fun String.hasBeenVisited(path: List<String>) = path.contains(this)

    private fun String.isTheEnd() = this == "end"
    private fun String.isStart() = this == "start"

    private fun parsePaths(input: List<String>): List<List<String>> =
        buildList {
            input.map {
                it.trim().split("-")
            }.forEach {
                if (it.first().isStart()) {
                    add(it)
                } else if (it.last().isStart()) {
                    add(it.reversed())
                } else if  (it.first().isTheEnd()) {
                    add(it.reversed())
                } else if (it.last().isTheEnd()) {
                    add(it)
                } else {
                    add(it)
                    add(it.reversed())
                }
            }
        }

    fun part2(input: List<String>): Long {
        return -1L
    }
}
