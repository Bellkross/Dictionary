import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.experimental.inv

class Dictionary {

    var dictionary: String = ""
    var compressedInvertedIndexBytes: TreeMap<Int, ByteArray> = TreeMap()

    var list: TreeSet<String> = TreeSet()
    var invertedIndex: HashMap<String, MutableList<Int>> = HashMap()

    fun addDocument(document: Document) {
        val documentId = document.id

        val tokens = document.dictionary

        tokens.forEach { k ->
            if (!invertedIndex.contains(k)) {
                invertedIndex[k] = ArrayList()
                invertedIndex[k]!!.add(documentId)
            } else {
                if (!invertedIndex[k]!!.contains(documentId))
                    invertedIndex[k]!!.add(documentId)
            }
        }

        list.addAll(invertedIndex.keys)
        list = TreeSet<String>(list)

    }

    fun compress() {
        var pos = 0

        var prev = 0
        var byteArray: ByteArray?
        var listOfBytes : MutableList<Byte>
        list.forEachIndexed { i, word ->
            dictionary += word
            pos += word.length

            invertedIndex[word]!!.forEach {
                listOfBytes = vbEncodeNumber(it - prev)
                byteArray = ByteArray(listOfBytes.size)
                listOfBytes.forEachIndexed { j, byte ->
                    byteArray!![j] = byte
                }
                compressedInvertedIndexBytes[pos] = byteArray!!
                prev = it
                byteArray = null
            }

        }

    }

    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        val resList: MutableList<Int> = ArrayList()
        var i = 0
        var j = 0
        while (i < vector1.size && j < vector2.size) {
            when {
                distance >= abs(vector2[j] - vector1[i]) -> {
                    resList.add(vector1[i++])
                    ++j
                }
                distance < abs(vector2[j] - vector1[i]) -> ++i
                else -> ++j
            }
        }

        return resList
    }

    fun vbEncodeNumber(value: Int): MutableList<Byte> {
        var n = value
        var bytes: MutableList<Byte> = ArrayList()
        while (true) {
            bytes = prepend(bytes, n % 128)
            if (n < 128) {
                break
            }
            n /= 128
        }
        bytes[bytes.lastIndex] = bytes[bytes.lastIndex].dec().inv()
        return bytes
    }

    private fun prepend(bytes: MutableList<Byte>, n: Int): MutableList<Byte> {
        val res: MutableList<Byte> = ArrayList()
        val byte: Byte = n.toByte()
        res.add(byte)
        res.addAll(bytes)
        return res
    }

    fun vbEncode(numbers: MutableList<Int>): MutableList<Byte> {
        val res: MutableList<Byte> = ArrayList()
        numbers.forEach { res.addAll(vbEncodeNumber(it)) }
        return res
    }

    fun vbDecode(numbers: ByteArray) : MutableList<Int> {
        val res : MutableList<Int> = ArrayList()
        var n = 0
        numbers.forEach {
            if(it > 0){
                n = 128*n - it
            } else {
                n = 128*n + it
                res.add(n*(-1))
                n=0
            }
        }
        return res
    }

}