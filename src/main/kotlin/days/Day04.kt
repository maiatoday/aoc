// Day object template
object Day04 {

    private var boards = emptyList<Board>()
    fun part1(input: List<String>): Int {
        val calls = input[0].split(",").map { it.toInt() }
        boards = extractBoards(input)
        calls.forEach { call ->
            boards.forEach { board ->
                if (board.bingo(call)) return call * board.score()
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val calls = input[0].split(",").map { it.toInt() }
        boards = extractBoards(input)
        var winningBoardCount = 0
        calls.forEach { call ->
            boards.forEach { board ->
                if (board.bingo(call)) {
                    winningBoardCount++
                    if (winningBoardCount == boards.size)
                        return call * board.score()
                }
            }
        }
        return 0
    }

    private fun extractBoards(input: List<String>): List<Board> =
        input.drop(1).chunked(6).map { boardInput ->
            Board(boardInput.filter {
                it.isNotEmpty()
            })
        }

    class Board(input: List<String>) {
        private val board: List<List<Cell>> = buildBoard(input)
        var hasWon: Boolean = false

        private fun buildBoard(input: List<String>): List<List<Cell>> =
            input.map { rowString ->
                rowString.trim().replace("  ", " ").split(" ")
                    .map { Cell(it.toInt(), false) }
            }

        fun bingo(call: Int): Boolean {
            if (hasWon) return false

            // check rows
            board.forEach { row ->
                row.forEach { cell ->
                    if (cell.value == call) {
                        cell.checked = true
                    }
                }
                if (row.count { it.checked } == row.size) {
                    hasWon = true
                    return true
                }
            }

            // check columns
            for (column in 0 until board[0].size) {
                if (board.map { it[column] }.count { it.checked } == board.size) {
                    hasWon = true
                    return true
                }
            }

            return false
        }

        fun score(): Int =
            board.flatten()
                .filterNot { it.checked }
                .sumOf { it.value }

        data class Cell(val value: Int, var checked: Boolean)

    }
}
