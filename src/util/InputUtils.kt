package util

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
// const val resourceFolder = "src/main/resources/"
const val resourceFolder = "resources/"

fun readInput(dayNumber: Int, name: String) = File("$resourceFolder$dayNumber", "$name.txt").readLines()
fun readInputString(dayNumber: Int, name: String) = File("$resourceFolder$dayNumber", "$name.txt").readText().trim()
fun readInputStringRepeat(dayNumber: Int, name: String, n: Int) = File("$resourceFolder$dayNumber", "$name.txt").readText().repeat(n)
fun List<String>.filterComments(): List<String> = this.filter { it[0] != '#' }

/**
 * Converts string to md5 hash.
 */
//fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

//val input: String = File("src/Day.txt").readText()
//val input2: List<String> = Path("src/Day.txt").readLines()
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

fun String.readLongs() = Regex("""\d+""").findAll(this)
        .map(MatchResult::value)
        .map(String::toLong)
        .toList()

fun String.readInts() = Regex("""\d+""").findAll(this)
        .map(MatchResult::value)
        .map(String::toInt)
        .toList()

fun List<String>.splitByBlankLine(): List<List<String>> = this.fold(mutableListOf(mutableListOf<String>())) { acc, s ->
    if (s.isBlank())
        acc.add(mutableListOf())
    else
        acc.last().add(s)
    acc
}.filter {
    it.isNotEmpty()
}
