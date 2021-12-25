object Day25 {

    fun part1(input: List<String>): Long {
        val seaFloor = input.map { it.toCharArray() }.toTypedArray()
        debug(seaFloor, "*****Initial****")
        val maxY = seaFloor.size
        val maxX = seaFloor[0].size
        var steps = 0
        var canMove = true
        while (canMove) {
            steps++
            canMove = step(seaFloor, maxY, maxX)
        }

        return steps.toLong()
    }

    private fun debug(seaFloor: Array<CharArray>, message:String) {
        println(message)
        seaFloor.map {
            println(it.joinToString(""))
        }
    }

    private fun step(seaFloor: Array<CharArray>, maxY: Int, maxX: Int): Boolean {
        var moveCount = 0
        moveCount += eastMoves(seaFloor, maxX)
        moveCount += southMoves(seaFloor, maxY)
        return moveCount > 0
    }
    
    private fun eastMoves(seaFloor: Array<CharArray>, maxX: Int): Int {
        val moveList = mutableListOf<Triple<Int, Int, Int>>()
        for (y in seaFloor.indices) for (x in seaFloor[0].indices) {
            if (seaFloor[y][x] == '>') {
                val nextX = (x + 1) % maxX
                if (seaFloor[y][nextX] == '.') {
                    moveList.add(Triple(x, y, nextX))
                }
            }
        }
        for (m in moveList) {
            seaFloor[m.second][m.first] = '.'
            seaFloor[m.second][m.third] = '>'
        }

        debug(seaFloor, ">>>>>>>>>East moved")
        return moveList.size
    }

    private fun southMoves(seaFloor: Array<CharArray>, maxY: Int): Int {
        val moveList = mutableListOf<Triple<Int, Int, Int>>()
        for (y in seaFloor.indices) for (x in seaFloor[0].indices) {
            if (seaFloor[y][x] == 'v') {
                val nextY = (y + 1) % maxY
                if (seaFloor[nextY][x] == '.') {
                    moveList.add(Triple(x, y, nextY))
                }
            }
        }
        for (m in moveList) {
            seaFloor[m.second][m.first] = '.'
            seaFloor[m.third][m.first] = 'v'
        }

        debug(seaFloor, "vvvvvvvvvvv south moved")
        return moveList.size
    }

    fun part2(input: List<String>): Long {
        return -1L
    }
}