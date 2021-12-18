object Day18 {

    fun part1(input: List<String>): Long {
//        val values = input.map { s ->
//            s.removePrefix("[")
//                .removeSuffix("]")
//                .split(",")
//                .map { SnailfishNumber(regularNumber = it.toInt()) }
//        }.map { l -> l[0] + l[1] }
//        val c = values.reduce { acc, sfn ->
//            acc + sfn
//        }
        val c = SnailfishNumber("[0,1]", parent = null)

//        val i = SnailfishNumber(4)
//        val j = SnailfishNumber(3)
//        val d = SnailfishNumber(4)
//        val dd = SnailfishNumber(4)
//        val ddd = SnailfishNumber(7)
//        val b = SnailfishNumber(8)
//        val bb = SnailfishNumber(4)
//        val bbb = SnailfishNumber(9)
//        val bbbb = SnailfishNumber(1)
//        val bbbbb = SnailfishNumber(1)
        //val c = ((((i + j) + d) + dd) + (ddd + ((b + bb) + bbb))) + (bbbb + bbbbb)
        println("start")
        c.debug()

        var reductionHappened = true
        do {
            println(">>>>>")
            reductionHappened = c.reduce(0)
            c.debug()
        } while (reductionHappened)

        val mag = c.magnitude()
        return mag.toLong()
    }

    fun part2(input: List<String>): Long {
        return -1L
    }


    class SnailfishNumber(
        var regularNumber: Int? = null,
        var left: SnailfishNumber? = null,
        var right: SnailfishNumber? = null,
        var parent: SnailfishNumber? = null,
    ) {
        constructor(
            str: String,
            parent: SnailfishNumber? = null
        ) : this(parent = parent) {
            str.forEach { s ->
                when {
                    s == '[' -> {
                        left = SnailfishNumber(str.drop(1))
                    }
                    s == ']' -> {}
                    s == ',' -> {
                        right = SnailfishNumber(str.drop(1))
                    }
                    s.isDigit() -> {
                        regularNumber = str[0].toString().toInt()
                    }
                }
            }
        }


        //        fun parse(str:String) : SnailfishNumber {
//            if (str[0] ==  '[') {
//                var node = SnailfishNumber(this)
//            }
//        }
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
            // complex number
            if (nestingLevel == 4) {
                // children could explode
                if (explode(nestingLevel)) return true
            }
            if ((left != null) and (right != null)) {
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
            if (regularNumber == null) {
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
            } else if (parent != null) {
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
