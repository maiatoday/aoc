object Day12 {

    fun part1(input: List<String>): Long {
        val collectedPaths = mutableSetOf<String>()
        val paths = parsePaths(input)
        val caveMap = paths.groupBy { it[0] }
        findPaths(caveMap, listOf("start"), collectedPaths) { nextCave, partialPath ->
            (nextCave.isMainCave() or
                    (!nextCave.isMainCave() and !nextCave.hasBeenVisited(partialPath)) or
                    nextCave.isTheEnd())
        }
        return collectedPaths.size.toLong()
    }

    private fun findPaths(
        caveMap: Map<String, List<List<String>>>,
        partialPath: List<String>,
        collectedPaths: MutableSet<String>,
        caveCheck: (String, List<String>) -> Boolean
    ) {
        if (partialPath.last().isTheEnd()) {
            collectedPaths.add(partialPath.joinToString())
        }
        caveMap[partialPath.last()]?.filter {
            caveCheck(it[1], partialPath)
        }?.forEach { p ->
            findPaths(caveMap, partialPath + p[1], collectedPaths, caveCheck)
        }
    }

    private fun String.isMainCave(): Boolean = this.all { it.isUpperCase() }
    private fun String.hasBeenVisited(path: List<String>) = path.contains(this)

    private fun String.okToVisit(path: List<String>) =
        if (path.contains(this)) {
            val minorCaves = path.filter {
                !it.isMainCave()
            }
            // if the number of minor caves in the path is the same as the
            // unique number of minor caves then we don't have duplicates
            minorCaves.size == minorCaves.toSet().size
        } else true

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
                } else if (it.first().isTheEnd()) {
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

        val collectedPaths = mutableSetOf<String>()
        val paths = parsePaths(input)
        val caveMap = paths.groupBy { it[0] }
        findPaths(caveMap, listOf("start"), collectedPaths) { nextCave, partialPath ->
            (nextCave.isMainCave() or
                    (!nextCave.isMainCave() and nextCave.okToVisit(partialPath)) or
                    nextCave.isTheEnd())
        }
        return collectedPaths.size.toLong()
    }
}
