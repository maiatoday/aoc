// Day object template
object Day04 {

    fun part1(input: List<String>): Int {
        val calls = input[0].split(",").map { it.toInt() }
        val boards = listOf(
            Board(input.subList(2, 7)),
            Board(input.subList(8, 13)),
            Board(input.subList(14, 19))
        )
        calls.forEach { call ->
            boards.forEach { board ->
                if (board.bingo(call)) return call * board.score()
            }
        }
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

class Board(input: List<String>) {
    private val board: List<List<Cell>> = buildBoard(input)

    private fun buildBoard(input: List<String>): List<List<Cell>> =
        input.map { rowString ->
            rowString.split(" ").map { Cell(it.toInt(), false) }
        }

    fun bingo(call: Int): Boolean {
        board.forEach { row ->
            row.forEach { cell ->
                cell.checked = cell.value == call
            }
            if (row.count { it.checked } == row.size) return true
        }

        for (column in 0 until board[0].size) {
            if (board.map { it[column] }.count { it.checked } == board.size) return true
        }

        return false
    }

    fun score(): Int =
        board.flatten()
            .filterNot { it.checked }
            .sumOf { it.value }

    data class Cell(val value: Int, var checked: Boolean)

}
