object Day18 {

    fun part1(input: List<String>): Long {
        val c = input.map { s -> parse(s) }
            .reduce { acc, sfn -> acc + sfn }
       // val c = parse("[[1,2],[[3,4],5]]")
        println("start")
        c.debug()

        var reductionHappened = true
        do {
            println(">>>>>")
            reductionHappened = c.reduce(0)
            c.debug()
        } while (reductionHappened)
        println("   END")
        println("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]] ... expected")
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
            if (regularNumber != null) {
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
            if (regularNumber == null && nestingLevel >= 4) {
                // children could explode
                if (explode(nestingLevel)) return true
            }
            regularNumber?.let {
                // regular number not complex
                if (it >= 10) {
                    println("nest $nestingLevel split")
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
            if (regularNumber == null && left?.regularNumber !== null && right?.regularNumber != null) {
                println("nest $nestingLevel explode")
                // complex
                // :face_palm: the left of me parent might be me
                parent?.addToLeft(left?.regularNumber ?: 0, this)
                parent?.addToRight(right?.regularNumber ?: 0, this)
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

        private fun addToLeft(number: Int, downStream: SnailfishNumber) {
            if (left?.regularNumber != null) {
                left?.regularNumber = (left?.regularNumber ?: 0) + number
            } else if (right == downStream) {
                left?.right?.regularNumber = (left?.right?.regularNumber ?: 0) + number
            }else if (parent != null) {
                parent?.addToLeft(number, this)
            } else {
                // parent == null we are at the top of the parent list
                // but we can still add it to the right most element of the sibling lefthand
                // but only if we came from the right
                if (downStream == right) addToLeftFromTop(number)
            }
        }

        private fun addToLeftFromTop(number: Int) {
            if (left?.regularNumber != null) {
                left?.regularNumber = (left?.regularNumber ?: 0) + number
            } else if (left != null) {
                left?.rr(number)
            }
        }

        private fun rr(number: Int) {
            if (regularNumber != null) {
                regularNumber = (regularNumber ?: 0) + number
            } else if (right != null) {
                right?.ll(number)
            }
        }

        private fun addToRight(number: Int, downStream: SnailfishNumber) {
            if (right?.regularNumber != null) {
                right?.regularNumber = (right?.regularNumber ?: 0) + number
            } else if (left == downStream) {
                right?.left?.regularNumber = (right?.left?.regularNumber ?: 0) + number
            } else if (parent != null) {
                parent?.addToRight(number, this)
            } else {
                // parent == null so we are at the top of the parent list
                // but we can still add it to the left most element of the sibling righthand
                // but only if we came from the left
                if (downStream == left)
                    addToRightFromTop(number)
            }
        }


        private fun addToRightFromTop(number: Int) {
            if (right?.regularNumber != null) {
                right?.regularNumber = (right?.regularNumber ?: 0) + number
            } else if (right != null) {
                right?.ll(number)
            }
        }

        private fun ll(number: Int) {
            if (regularNumber != null) {
                regularNumber = (regularNumber ?: 0) + number
            } else if (left != null) {
                left?.ll(number)
            }
        }

    }

    operator fun SnailfishNumber.plus(other: SnailfishNumber): SnailfishNumber {
        val parent = SnailfishNumber(left = this, right = other)
        this.parent = parent
        other.parent = parent
        return parent
    }
}
