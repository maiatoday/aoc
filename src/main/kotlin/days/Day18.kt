object Day18 {

    fun part1(input: List<String>): Long {
        val c = input.filter{it[0]!= '#'}.map { s -> parse(s) }
            .reduce { acc, sfn -> acc + sfn }
        println("start")
        c.debug()

        var reductionHappened = true
        do {
            println(">>>>>")
            reductionHappened = c.reduce(0)
            c.debug()
        } while (reductionHappened)
        println("   END")
        println("[[[[0,7],4],[[7,8],[6,0]]],[8,1]] ... expected")
        val mag = c.magnitude()
        return mag.toLong()
    }

    fun part2(input: List<String>): Long {
        return -1L
    }

    private fun parse(str: String): SnailfishNumber {
        val sn = SnailfishNumber()
        sn.parse(str)
        return sn
    }

    class SnailfishNumber(
        var regularNumber: Int? = null,
        var left: SnailfishNumber? = null,
        var right: SnailfishNumber? = null,
        var parent: SnailfishNumber? = null,
    ) {
        fun parse(str: String): String {
            var sss = str
            var c = sss[0]
            while (c != ']') {
                when {
                    c == '[' -> {
                        left = SnailfishNumber(parent = this)
                        sss = left?.parse(sss.drop(1)) ?: ""
                    }
                    c == ',' -> {
                        right = SnailfishNumber(parent = this)
                        sss = right?.parse(sss.drop(1)) ?: ""
                    }
                    c.isDigit() -> {
                        regularNumber = c.toString().toInt()
                        return sss.drop(1)
                    }
                }
                c = sss[0]
            }
            return sss.drop(1)
        }

        fun debug() {
            if (isRegular()) {
                print("$regularNumber")
            } else {
                print("[")
                left?.debug()
                print(",")
                right?.debug()
                print("]")
            }
        }

        fun magnitude(): Int =
            regularNumber ?: (((left?.magnitude() ?: 0) * 3) + ((right?.magnitude() ?: 0) * 2))

        fun reduce(nestingLevel: Int): Boolean {
            // complex number
            if (!isRegular() && nestingLevel >= 4) {
                // children could explode
                if (explode(nestingLevel)) return true
            }
            regularNumber?.let {
                // regular number not complex
                if (it >= 10) {
                    debug()
                    println(" nest $nestingLevel ~~~~split")
                    // split
                    val leftNumber = it / 2
                    val rightNumber = (it + 2 - 1) / 2
                    regularNumber = null
                    this.left = SnailfishNumber(regularNumber = leftNumber)
                    this.right = SnailfishNumber(regularNumber = rightNumber)
                    return true
                }
            }
            if (left != null && right != null) {
                //println("down one nesting level $nestingLevel")
                if ((left?.reduce(nestingLevel + 1) == true)) return true
                else if (right?.reduce(nestingLevel + 1) == true) return true
            }
            return false
        }

        private fun explode(nestingLevel: Int): Boolean {
            //  To explode a pair, the pair's left value is added to the first regular number to the left of the exploding pair (if any),
            //  and the pair's right value is added to the first regular number to the right of the exploding pair (if any).
            //  Exploding pairs will always consist of two regular numbers.
            //  Then, the entire exploding pair is replaced with the regular number 0.
            if (!isRegular() && left.isRegular() && right.isRegular()) {
                debug()
                println(" nest $nestingLevel !!!!explode")
                // complex
                // :face_palm: the left of me parent might be me
                parent?.upSearchLeft(left?.regularNumber ?: 0, this)
                parent?.upSearchRight(right?.regularNumber ?: 0, this)
                convertToNumber(0)
                return true
            }
            return false
        }

        private fun convertToNumber(number: Int) {
            regularNumber = number
            left = null
            right = null
        }

        private fun upSearchLeft(number: Int, downStream: SnailfishNumber) {
            if (left != downStream) {
                left?.downSearchRight(number)
                return
            }
            if (parent == null) return
            parent?.upSearchLeft(number, this)
        }

        private fun downSearchRight(number: Int) {
            if (isRegular()) {
                addToRegular(number)
                return
            }
            right?.downSearchRight(number)
        }

        private fun upSearchRight(number: Int, downStream: SnailfishNumber) {
            if (right != downStream) {
                right?.downSearchLeft(number)
                return
            }
            if (parent == null) return
            parent?.upSearchRight(number, this)
        }

        private fun downSearchLeft(number: Int) {
            if (isRegular()) {
                addToRegular(number)
                return
            }
            left?.downSearchLeft(number)
        }

    }

    fun SnailfishNumber?.isRegular() = this?.regularNumber != null

    fun SnailfishNumber?.addToRegular(n: Int) {
        this?.regularNumber?.let {
            this.regularNumber = it + n
        }
    }

    operator fun SnailfishNumber.plus(other: SnailfishNumber): SnailfishNumber {
        val parent = SnailfishNumber(left = this, right = other)
        this.parent = parent
        other.parent = parent
        return parent
    }
}

