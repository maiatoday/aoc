package days

object Day08 : Day<Long, List<String>> {
    override val number: Int = 8
    override val expectedPart1Test: Long = 21L
    override val expectedPart2Test: Long = 8L
    override var useTestData = true

    override fun part1(input: List<String>): Long {
        val visibleTrees: MutableMap<Pair<Int, Int>, Boolean> = mutableMapOf()
        input.forEachIndexed() { r, treeRow ->
            treeRow.forEachIndexed { c, tree ->
                if (visibleLeftRight(treeRow, r, c, tree) || visibleTopBottom(input, r, c, tree))
                    visibleTrees[(r to c)] = true
            }
        }
        return visibleTrees.size.toLong()
    }

    private fun visibleTopBottom(treemap: List<String>, r: Int, c: Int, tree: Char): Boolean {
        if (r == 0 || c == 0 || r == treemap.size - 1 || c == treemap.size - 1) return true
        else {
            val up = treemap.take(r).map { it[c] }.joinToString("")
            val down = treemap.takeLast(treemap.size - r - 1).map { it[c] }.joinToString("")
            if (up.none { it >= tree }) return true
            if (down.none { it >= tree }) return true
        }
        return false
    }

    private fun visibleLeftRight(treeRow: String, r: Int, c: Int, tree: Char): Boolean {
        if (r == 0 || c == 0 || r == treeRow.length - 1 || c == treeRow.length - 1) return true
        else {
            val left = treeRow.substring(0, c)
            val right = treeRow.substring(c + 1)
            if (left.none { it >= tree }) return true
            if (right.none { it >= tree }) return true
        }
        return false
    }

    private fun scoreLeftRight(treeRow: String, r: Int, c: Int, tree: Char): Int {
        val left = treeRow.substring(0, c).reversed()
        val right = treeRow.substring(c + 1)
        return countVisible(left, tree) * countVisible(right, tree)
    }

    private fun scoreTopBottom(treemap: List<String>, r: Int, c: Int, tree: Char): Int {
        val up = treemap.take(r).map { it[c] }.joinToString("").reversed()
        val down = treemap.takeLast(treemap.size - r - 1).map { it[c] }.joinToString("")
        return countVisible(up, tree) * countVisible(down, tree)
    }

    private fun countVisible(sight: String, tree: Char): Int {
        var count = 0
        for (c in sight) {
            count++
            if (c >= tree) return count
        }
        return count
    }

    override fun part2(input: List<String>): Long {
        val scenicScore: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        input.forEachIndexed() { r, treeRow ->
            treeRow.forEachIndexed { c, tree ->
                val scoreLeftRight = scoreLeftRight(treeRow, r, c, tree)
                val scoreTopBottom = scoreTopBottom(input, r, c, tree)
                scenicScore[(r to c)] = scoreLeftRight * scoreTopBottom
            }
        }
        return scenicScore.maxOf { it.value }.toLong()
    }
}