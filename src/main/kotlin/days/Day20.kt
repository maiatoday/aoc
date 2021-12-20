// Day object template
object Day20 {

    fun part1(input: List<String>): Long {
        val algorithm = input[0]
        val seedImage = input.drop(2)
        val newImage = processImage(seedImage, algorithm, 2)
        return newImage.joinToString("").count { it == '#' }.toLong()
    }

    fun part2(input: List<String>): Long {
        val algorithm = input[0]
        val seedImage = input.drop(2)
        val newImage = processImage(seedImage, algorithm, 50)
        return newImage.joinToString("").count { it == '#' }.toLong()
    }

    private fun processImage(
        seedImage: List<String>,
        algorithm: String,
        steps:Int
    ): List<String> {
        var newImage = seedImage
        // how to decide what the background should be?
        // I don't want to store fields of background, I only need as much as the lens needs to get a value.
        // To explain differently I only need to know a value to fill in if the x and y is out of bounds.
        // However, the background also looks up in the algorithm array.
        // e.g if the background is all '.' then it's value is 000000000 and it gets value algorithm[0]
        // If the background is all '#' it's value is 111111111 and it gets value algorithm[511]

        // This means if algorithm[0] == '.' then the background lookup is always '.' and the background never changes

        // If algorithm[0] == '#' then the background lookup is algorithm[0] the first time then algorithm[511] 
        // the next time and then algorithm[0] on round three etc.
        // So we have an odd/even flip between algorithm[0] and algorithm[511]
        val oddBackground = algorithm[0]
        val evenBackground = if (algorithm[0] == '.') algorithm[0] else algorithm[511]
        repeat(steps) {
            newImage = enhance(algorithm, newImage, it, oddBackground, evenBackground)
        }
        return newImage
    }

    private fun enhance(
        algorithm: String,
        seedImage: List<String>,
        stepCount: Int,
        zeroOdd: Char,
        zeroEven: Char
    ): List<String> {
        val lensSize = 1
        val newImage = mutableListOf<String>()
        val maxY = seedImage.size - 1 + lensSize
        val maxX = seedImage[0].length - 1 + lensSize
        val zeroValue = if (stepCount % 2 == 0) zeroEven else zeroOdd
        for (y in -lensSize..maxY) {
            val sb = StringBuilder(maxX)
            for (x in -lensSize..maxX) {
                val ii = getIntFromSeed(x, y, seedImage, zeroValue)
                sb.append(algorithm[ii])
            }
            newImage.add(sb.toString())
        }
        return newImage
    }

    private fun getIntFromSeed(x: Int, y: Int, seedImage: List<String>, zeroValue: Char): Int {
        val maxY = seedImage.size
        val maxX = seedImage[0].length
        val sb = StringBuilder(9) // related to lens size, 3x3
        for (yy in y - 1..y + 1) for (xx in x - 1..x + 1) {
            if ((xx in 0 until maxX) && (yy in 0 until maxY)) sb.append(seedImage[yy][xx])
            else sb.append(zeroValue)
        }
        val valueString = sb.toString().replace(".", "0").replace("#", "1")
        return valueString.toInt(2)
    }

}
