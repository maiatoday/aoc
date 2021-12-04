object Day03 {

    fun part1(input: List<String>): Int {
        var gammaBits = ""
        var epsilonBits = ""
        for (i in 0 until input[0].count()) {
            if (isCommonChar(position = i, input = input)) {
                gammaBits += '1'
                epsilonBits += '0'
            } else {
                gammaBits += '0'
                epsilonBits += '1'
            }
        }

        return gammaBits.toInt(2) * epsilonBits.toInt(2)
    }

    fun part2(input: List<String>): Int {

        var oxygenBits = ""
        var co2Bits = ""

        var oxygenFindList: List<String> = input
        for (i in 0 until input[0].count()) {
            if (oxygenFindList.count() == 1) break
            val commonChar = isCommonChar(
                cc = '1',
                position = i,
                input = oxygenFindList
            )
            oxygenFindList = if (commonChar) {
                findLeftoverList(i, oxygenFindList, '1')
            } else {
                findLeftoverList(i, oxygenFindList, '0')
            }
        }
        oxygenBits = oxygenFindList[0]

        var co2FindList: List<String> = input
        for (i in 0 until input[0].count()) {
            if (co2FindList.count() == 1) break
            val commonChar = isCommonChar(
                cc = '1',
                position = i,
                input = co2FindList
            )
            co2FindList = if (commonChar) {
                findLeftoverList(i, co2FindList, '0')
            } else {
                findLeftoverList(i, co2FindList, '1')
            }
        }
        co2Bits = co2FindList[0]

        return oxygenBits.toInt(2) * co2Bits.toInt(2)
    }

    private fun findLeftoverList(
        position: Int,
        input: List<String>,
        checkChar: Char,
        invert: Boolean = false
    ): List<String> {
        return input.filter {
            it[position] == checkChar
        }
    }

    private fun isCommonChar(cc: Char = '1', position: Int, input: List<String>): Boolean {

        val temp = input.filter {
            it[position] == cc
        }
        return if (temp.count()*2 == input.count()) true
        else temp.count() * 2 > input.count()
    }

}
