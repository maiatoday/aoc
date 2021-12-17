object Day17 {

    fun part1(input: List<String>): Long {
        val xRange = getTargetRange(input, "x=")
        val yRange = getTargetRange(input, "y=")
        val xHigh = xRange.maxOrNull() ?: (2*xRange[1])
        var trying = true
        var maxApex = Int.MIN_VALUE
        while (trying) {
            for (sx in 0..xHigh) { // what should the range be?
                for (sy in -400..400) {
                    val p = Probe(targetX = xRange, targetY = yRange, startVx = sx, startVy = sy)
                    while (!p.inTarget() and !p.overShot()) {
                        p.updatePosition()
                    }
                   // println( "$sx-$sy gives apex ${p.apex} with hit/miss ${p.targetHit}")
                    if ((p.apex > maxApex) and p.targetHit) maxApex = p.apex
                }
                trying = false // this brute force, need to find an elegant way to detect when to stop
            }
        }
        return maxApex.toLong()
    }

    data class Probe(
        val startX: Int = 0,
        val startY: Int = 0,
        val targetX: List<Int>,
        val targetY: List<Int>,
        val startVx: Int,
        val startVy: Int
    ) {
        var x = startX
        var y = startY
        var vx = startVx
        var vy = startVy
        var apex = Int.MIN_VALUE
        var targetHit = false

        fun updatePosition():Boolean {
            x += vx
            y += vy
            if (y > apex) apex = y
            if (inTarget()) {
                targetHit = true
            }
            if (vx > 0) vx--
            else if (vx < 0) vx++
            vy--
            return targetHit
        }

        fun inTarget() = ((targetX[0]..targetX[1]).contains(x) and (targetY[0]..targetY[1]).contains(y))

        fun overShot() = (x > (targetX.maxOrNull() ?: 0)) or (y < (targetY.minOrNull() ?: 0))
    }

    fun part2(input: List<String>): Long {
        val xRange = getTargetRange(input, "x=")
        val yRange = getTargetRange(input, "y=")
        val xHigh = xRange.maxOrNull() ?: (2*xRange[1])
        var trying = true
        var shotCount = 0L
        while (trying) {
            for (sx in 0..xHigh) { // what should the range be?
                for (sy in -400..400) {
                    val p = Probe(targetX = xRange, targetY = yRange, startVx = sx, startVy = sy)
                    while (!p.inTarget() and !p.overShot()) {
                        p.updatePosition()
                    }
                    if (p.targetHit) shotCount++
                  //  println( "$sx-$sy gives apex ${p.apex} with hit/miss ${p.targetHit} shotCount $shotCount")
                }
                trying = false // this brute force, need to find an elegant way to detect when to stop
            }
        }
        return shotCount
    }

    fun getTargetRange(input: List<String>, identifier: String) =
        input[0].split(" ").drop(1).map {
            it.trim().replace(",", "")
        }.filter { it.contains("..") }
            .first { it.startsWith(identifier) }.replace(identifier, "").split("..")
            .map { it.toInt() }

}
