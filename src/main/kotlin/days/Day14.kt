object Day14 {

    fun part1(input: List<String>, steps: Int = 10): Long {
        val template = findTemplate(input)
        val rules = findRules(input)
        var polymer = template
        for (s in 0 until steps) {
            polymer = applyRules(rules, polymer)
        }
        val elements = polymer.toCharArray().toSet()
        val sizes = elements.associateBy { polymer.count { e -> e == it } }
        val common = sizes.keys.maxOrNull() ?: 0
        val uncommon = sizes.keys.minOrNull() ?: 0
        return common.toLong() - uncommon.toLong()
    }

    private fun applyRules(rules: Map<String, String>, polymer: String): String {
        var updatedPolymer = polymer.first().toString()
        polymer.windowed(2) { s ->
            updatedPolymer += (rules[s] ?: "") + s[1]
        }
        return updatedPolymer
    }

    fun part2(input: List<String>, steps: Int = 40): Long {
        val template = findTemplate(input)
        val rules = findRules(input)
        val elements = (rules.values.flatMap { it.toList() } +
                rules.keys.flatMap { it.toList() }).toSet()
        val elementCount = mutableMapOf<Char, Long>()
        for (e in elements) {
            elementCount[e] = template.count { it == e }.toLong()
        }
        val pairCount = mutableMapOf<String, Long>()
        for (e1 in elements) {
            for (e2 in elements) {
                pairCount[e1.toString() + e2.toString()] = 0L
            }
        }
        template.windowed(2) {
            val key = it.toString()
            pairCount[key] = (pairCount[key] ?: 0) + 1L
        }
        for (s in 0 until steps) {
            applyRulesQuick(rules, pairCount, elementCount)
        }
        val common = elementCount.values.maxOrNull() ?: 0
        val uncommon = elementCount.values.filter { it != 0L }.minOrNull() ?: 0
        return common - uncommon
    }

    private fun applyRulesQuick(
        rules: Map<String, String>,
        pairCount: MutableMap<String, Long>,
        elementCount: MutableMap<Char, Long>,
    ) {
        val origCombosCount = pairCount.toMap()
        rules.forEach {
            val oldCount = origCombosCount[it.key] ?: 0L
            if (oldCount > 0L) {
                elementCount[it.value[0]] = (elementCount[it.value[0]] ?: 0) + oldCount
                pairCount[it.key] = (pairCount[it.key] ?: 0) - oldCount
                val firstNew = it.key[0] + it.value
                val secondNew = it.value + it.key[1]
                pairCount[firstNew] = (pairCount[firstNew] ?: 0) + oldCount
                pairCount[secondNew] = (pairCount[secondNew] ?: 0) + oldCount
            }
        }
    }

    private fun findRules(input: List<String>): Map<String, String> =
        (input.filter { it.contains("->") }.map { it.split(" -> ") }).associate { it[0] to it[1] }

    private fun findTemplate(input: List<String>): String =
        input.first()

}
