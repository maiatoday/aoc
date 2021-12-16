import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/main/resources", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

//val input: String = File("src/Day01.txt").readText()
//val input2: List<String> = Path("src/Day01.txt").readLines()
//
//val sanitized: String = "   5-6-7 ".trim()
//val numbers: List<Int> = sanitized.split("-").map { it.toInt() }
//
//val regex = """(\d)-(\d)-(\d)""".toRegex() //https://rubular.com/
//val moreNumbers = "1-2-3"
//val (a, b, c) = regex.find(moreNumbers)?.destructured ?: kotlin.error("oops")

fun Int.reverseBits(): Int {
    var n = this
    var reverse = 0
    while (n > 0) {
        reverse = reverse shl 1
        if ((n and 1) == 1) reverse = reverse xor 1
        n = n shr 1
    }
    return reverse
}

fun String.decodeHex(): LongArray {
    return chunked(8)
        .map {
            it
                // .toInt(16)
                .toLong(16)
            //.reverseBits()
            // .toByte()
        }
        .toLongArray()
    // .toIntArray()
    // .toByteArray()
}