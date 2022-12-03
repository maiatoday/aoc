import days.Day

object Day03 : Day {

    override fun part1(input: List<String>): Long = input.map { rucksack ->
        rucksack.substring(0 until rucksack.length / 2).toCharArray() to rucksack.substring(rucksack.length / 2).toCharArray()
    }
        .flatMap { (first, second) -> first intersect second.toSet() }
        .sumOf { it.toScore() }.toLong()

    // In the test example, the priority of the item type that appears in both compartments
    // of each rucksack is 16 (p), 38 (L), 42 (P), 22 (v), 20 (t), and 19 (s); the sum of these is 157.
    private fun Char.toScore(): Int =
        if (this.isUpperCase()) {
            this.code - 'A'.code + 27
        } else {
            this.code - 'a'.code + 1
        }

    override fun part2(input: List<String>): Long =
        input.chunked(3)
            .map { elfGroup ->
                elfGroup.zipWithNext()
                    .map { (first, second) ->
                        first.toCharArray() intersect second.toSet()
                    }
            }.flatMap { sharedItems ->
                // index safe because we chunked by 3 and then did zipWithNext
                sharedItems[0] intersect sharedItems[1]
            }.sumOf { it.toScore() }.toLong()

}