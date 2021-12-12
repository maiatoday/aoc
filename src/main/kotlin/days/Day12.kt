object Day12 {

    fun part1(input: List<String>): Long {
        val paths = parsePaths(input)
        val caveMap = paths.groupBy { it[0] }
        val possiblePaths = findPaths(caveMap)
        return possiblePaths.size.toLong()
    }

    private fun findPaths(caveMap: Map<String, List<List<String>>>): Set<String> {
        val collectedPaths = mutableSetOf<String>()
        caveMap["start"]?.forEach {
            var exhausted = false
            while (!exhausted) {
                val wholePath = findPathOne(caveMap, collectedPaths, it, listOf("start"))
                if (!wholePath.last().isTheEnd()) {
                    exhausted = true
                } else {
                    collectedPaths.add(wholePath.joinToString())
                }
            }
        }
        return collectedPaths
    }

    private fun findPathOne(
        caveMap: Map<String, List<List<String>>>,
        collectedPaths: Set<String>,
        currentSegment: List<String>?,
        partialPath: List<String>,
    ): List<String> {
        if (currentSegment == null) return partialPath
        val path = partialPath + currentSegment[1]
        return if (currentSegment[1] == "end") {
            path
        } else {
            val possiblePaths = caveMap[currentSegment[1]]
            val newSegment = possiblePaths?.firstOrNull { possible ->

                // need another test to check for a path unfollowed
                val dejavu = collectedPaths.any { collectedPath ->
                    val qq = (path + possible[1]).joinToString()
                    collectedPath.startsWith(qq)
                }
                (possible[1].isMainCave() or
                        (!possible[1].isMainCave() and !possible[1].hasBeenVisited(path)) or
                        possible[1].isTheEnd()) and !dejavu
            }
            findPathOne(caveMap, collectedPaths, newSegment, path)
        }
    }

    fun String.isMainCave(): Boolean = this.all { it.isUpperCase() }

    fun String.hasBeenVisited(path: List<String>) = path.contains(this)

    fun String.isTheEnd() = this == "end"

    private fun parsePaths(input: List<String>): List<List<String>> =
        buildList {
            input.map {
                it.trim().split("-")
            }.forEach {
                if (!it.contains("start") and (!it.contains("end"))) {
                    add(it)
                    add(it.reversed())
                } else {
                    add(it)
                }
            }
        }

    data class Path(val end1: String, val end2: String)

    fun part2(input: List<String>): Long {
        return -1L
    }
}
