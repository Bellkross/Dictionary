import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.experimental.inv

class Helper {

    companion object {
        val lim = 200000000
    }


    fun merge(files: Array<File>) {
        val brs = files.map { it.bufferedReader() }
        File("r.txt").createNewFile()
        val bw = File("r.txt").bufferedWriter()
        val strings = ArrayList<String?>()
        val index: TreeMap<String, TreeSet<Int>> = TreeMap()

        var word = ""
        var i = 0
        while (i++ < brs.size) {
            strings.add("")
        }

        brs.forEachIndexed { i, br ->
            strings[i] = br.readLine()
        }

        while (haveOneNotNull(strings)) {
            i = 0
            while (i < strings.size) {
                if (strings[i] != null) {
                    word = getWordFromIndex(strings[i])
                    if (index.containsKey(word)) {
                        index[word]!!.addAll(parseStringToIndex(strings[i]!!))
                    } else {
                        index[word] = TreeSet()
                        index[word]!!.addAll(parseStringToIndex(strings[i]!!))
                    }
                }
                i++
            }

            if (allEquals(strings)) {

                if (strings[0] != null)
                    bw.append("${strings[0]}\n")
                brs.forEachIndexed { j, _ ->
                    if (strings[j] != null)
                        strings[j] = brs[j].readLine()
                }

                index.clear()
            } else {

                bw.append("${index.keys.first()} -> ${index[index.keys.first()]}\n")
                bw.flush()
                i = 0
                while (i < strings.size) {
                    if (strings[i] != null &&
                            getWordFromIndex(strings[i]) == index.keys.first()) {
                        strings[i] = brs[i].readLine()
                    }
                    i++
                }
                index[index.keys.first()]!!.clear()
                index.remove(index.keys.first())
            }
        }
        brs.forEach { it.close() }
        bw.close()
    }

    fun mergeRange(files: Array<File>) {
        val brs = files.map { it.bufferedReader() }
        File("r.txt").createNewFile()
        val bw = File("r.txt").bufferedWriter()
        val strings = ArrayList<String?>()
        val index: TreeMap<String, TreeMap<Int, HashSet<Meta>>> = TreeMap()

        var word = ""
        var i = 0
        while (i++ < brs.size) {
            strings.add("")
        }

        brs.forEachIndexed { i, br ->
            strings[i] = br.readLine()
        }

        while (haveOneNotNull(strings)) {
            i = 0
            while (i < strings.size) {
                if (strings[i] != null) {
                    word = getWordFromIndex(strings[i])
                    val rangeData = parseStringToRangeIndex(strings[i]!!)
                    if (index.containsKey(word)) {
                        rangeData.keys.forEach {
                            if (!index[word]!!.contains(it)) {
                                index[word]!![it] = HashSet()
                                index[word]!![it]!!.addAll(rangeData[it]!!)
                            } else {
                                index[word]!![it]!!.addAll(rangeData[it]!!)
                            }
                        }
                    } else {
                        index[word] = TreeMap()
                        rangeData.keys.forEach {
                            if (!index[word]!!.contains(it)) {
                                index[word]!![it] = HashSet()
                                index[word]!![it]!!.addAll(rangeData[it]!!)
                            } else {
                                index[word]!![it]!!.addAll(rangeData[it]!!)
                            }
                        }
                    }
                }
                i++
            }

            if (allEquals(strings)) {

                if (strings[0] != null)
                    bw.append("${strings[0]}\n")
                brs.forEachIndexed { j, _ ->
                    if (strings[j] != null)
                        strings[j] = brs[j].readLine()
                }

                index.clear()
            } else {

                bw.append("${index.keys.first()} -> ${index[index.keys.first()]}\n")
                bw.flush()
                i = 0
                while (i < strings.size) {
                    if (strings[i] != null &&
                            getWordFromIndex(strings[i]) == index.keys.first()) {
                        strings[i] = brs[i].readLine()
                    }
                    i++
                }
                index[index.keys.first()]!!.clear()
                index.remove(index.keys.first())
            }
        }
        brs.forEach { it.close() }
        bw.close()
    }

    fun mergeBytes(files: Array<File>) {
        val brs = files.map { it.bufferedReader() }
        val fileInvIndex = File("compressed\\bodyIndex.txt")
        val fileDictionary = File("compressed\\dictionary.txt")
        val filePointer = File("compressed\\pointers.txt")

        val fosII = FileOutputStream(fileInvIndex)
        val fosD = FileOutputStream(fileDictionary)
        val fosP = FileOutputStream(filePointer)
        var prev = 0//for p on D
        var number = 0//number of word for p on II

        val strings = ArrayList<String?>()
        val index: TreeMap<String, TreeSet<Int>> = TreeMap()

        var word = ""
        var i = 0
        while (i++ < brs.size) {
            strings.add("")
        }

        brs.forEachIndexed { i, br ->
            strings[i] = br.readLine()
        }

        while (haveOneNotNull(strings)) {
            i = 0
            while (i < strings.size) {
                if (strings[i] != null) {
                    word = getWordFromIndex(strings[i])
                    if (index.containsKey(word)) {
                        index[word]!!.addAll(parseStringToIndex(strings[i]!!))
                    } else {
                        index[word] = TreeSet()
                        index[word]!!.addAll(parseStringToIndex(strings[i]!!))
                    }
                }
                i++
            }

            if (allEquals(strings)) {

                if (strings[0] != null) {
                    //here
                    val myWord = index.keys.first()
                    val myIndex = index[index.keys.first()]!!
                    fosII.write(fromListToByteArray(vbEncode(myIndex.toMutableList())))
                    fosD.write(myWord.toByteArray())
                    fosP.write("${myIndex.size},${prev + myWord.length},${number++}\n".toByteArray())
                    prev += myWord.length
                    fosII.flush()
                    fosD.flush()
                    fosP.flush()
                }
                brs.forEachIndexed { j, _ ->
                    if (strings[j] != null)
                        strings[j] = brs[j].readLine()
                }

                index.clear()
            } else {

                //here
                val myWord = index.keys.first()
                val myIndex = index[index.keys.first()]!!
                fosII.write(fromListToByteArray(vbEncode(myIndex.toMutableList())))
                fosD.write(myWord.toByteArray())
                fosP.write("${myIndex.size},${prev + myWord.length},${number++}\n".toByteArray())
                prev += myWord.length
                fosII.flush()
                fosD.flush()
                fosP.flush()

                i = 0
                while (i < strings.size) {
                    if (strings[i] != null &&
                            getWordFromIndex(strings[i]) == index.keys.first()) {
                        strings[i] = brs[i].readLine()
                    }
                    i++
                }
                index[index.keys.first()]!!.clear()
                index.remove(index.keys.first())
            }
        }
        brs.forEach { it.close() }
        fosII.close()
    }

    private fun allEquals(strings: ArrayList<String?>): Boolean {
        strings.forEach { it1 ->
            strings.forEach { it2 ->
                if (it1 != it2) {
                    return false
                }
            }
        }
        return true
    }

    private fun haveOneNotNull(strings: ArrayList<String?>): Boolean {
        strings.forEach { if (it != null) return true }
        return false
    }

    fun getWordFromIndex(inputString: String?): String = inputString!!.substring(0, inputString.indexOf(" ->"))

    fun parseStringToRangeIndex(s: String): HashMap<Int, HashSet<Meta>> {
        val result: HashMap<Int, HashSet<Meta>> = HashMap()
        val string = s.substring(s.indexOf("{")).removePrefix("{").removeSuffix("}")
        string.removePrefix("{").removeSuffix("}").split(", ").forEach {
            val id = Integer.parseInt(it.substring(0, it.indexOf("=")))
            val value = helper.stringToMeta(it.substring(it.indexOf("[") + 1).removePrefix("[").removeSuffix("]"))
            if (!result.containsKey(id)) {
                result[id] = HashSet()
                result[id]!!.add(value)
            } else if (!result[id]!!.contains(value)) {
                result[id]!!.add(value)
            }
        }
        return result
    }

    fun stringToMeta(string: String): Meta {
        return when (string) {
            "BODY" -> Meta.BODY
            "AUTHOR" -> Meta.AUTHOR
            "TITLE" -> Meta.TITLE
            "DATE" -> Meta.DATE
            else -> Meta.BODY
        }
    }

    fun parseStringToIndex(inputString: String): TreeSet<Int> {
        val string = inputString.substring(inputString.indexOf("["))
        val result = TreeSet<Int>()
        result.addAll(string.removePrefix("[").removeSuffix("]").replace(" ", "").split(",").map { Integer.parseInt(it) })
        return result
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

    fun vbDecode(numbers: ByteArray): MutableList<Int> {
        val res: MutableList<Int> = ArrayList()
        var n = 0
        numbers.forEach {
            if (it > 0) {
                n = 128 * n - it
            } else {
                n = 128 * n + it
                res.add(n * (-1))
                n = 0
            }
        }
        return res
    }

    fun fromListToByteArray(list: MutableList<Byte>): ByteArray {
        val array = ByteArray(list.size)
        list.forEachIndexed { i, byte -> array[i] = byte }
        return array
    }

    private fun split(file: File, path: String) {
        val newFile = File(path + file.nameWithoutExtension + "1.txt")
        val byteArray = file.readBytes()
        file.writeBytes(byteArray.copyOf(byteArray.size / 2))
        newFile.appendBytes(byteArray.copyOfRange(byteArray.size / 2, byteArray.size))
    }

    fun split(files: Array<File>, path: String) {
        files.filter { (it.length() > lim) }.forEach { split(it, path) }
    }

    fun check(files: Array<File>): Boolean {
        files.forEach {
            if (it.length() > lim) return false
        }
        return true
    }

    fun findWordRangeIndex(inputWord: String): HashMap<Int, HashSet<Meta>> {
        val br = File("r.txt").bufferedReader()
        var data: String
        var word = ""
        var index: HashMap<Int, HashSet<Meta>> = HashMap()
        while (word != inputWord) {
            data = br.readLine()
            word = getWordFromIndex(data)
            if (word == inputWord) {
                index = parseStringToRangeIndex(data)
            }
        }

        return index
    }

    fun range(query: String, lim: Int = 10): List<Int> {
        val words = query.split(MyDocument.regex)
        val weight: HashMap<String, HashMap<Int, Float>> = HashMap()

        words.forEach {
            weight[it] = countWeight(findWordRangeIndex(it))
        }

        return getIdsOfTheBestFiles(weight, lim)
    }

    fun countWeight(index: HashMap<Int, HashSet<Meta>>): HashMap<Int, Float> {
        val wght: HashMap<Int, Float> = HashMap()
        var weight = 0f
        index.keys.forEach { id ->
            index[id]!!.forEach {
                weight += getValueByMeta(it)
            }
            wght[id] = weight
            weight = 0f
        }

        return wght
    }

    fun getIdsOfTheBestFiles(weight: HashMap<String, HashMap<Int, Float>>, lim: Int = 10): List<Int> {
        val all: HashMap<Int, Float> = HashMap()
        weight.keys.forEach {
            all.putAll(weight[it]!!)
        }
        var i = 0
        val list : MutableList<Int> = ArrayList()
        while (i++ < lim && all.isNotEmpty()){
            val index = getFirst(all)
            list.add(index)
            all.remove(index)
        }
        return list
    }

    fun getFirst(hashMap: HashMap<Int, Float>) : Int {
        var maxValue = -1f
        var maxKey = 0
        hashMap.keys.forEach {
            if(hashMap[it]!!>maxValue) {
                maxKey = it
                maxValue = hashMap[it]!!
            }
        }
        return maxKey
    }

    //wght.toSortedMap(kotlin.Comparator { o1, o2 -> check(wght[o1]!!-wght[o2]!!) }).subMap(0,lim).keys.toList()
    fun count(float: Float): Int {
        return when {
            float > 0 -> 1
            float == 0f -> 0
            else -> -1
        }
    }

    fun getValueByMeta(meta: Meta): Float {
        return when (meta) {
            Meta.BODY -> 0.3f
            Meta.AUTHOR -> 0.3f
            Meta.TITLE -> 0.3f
            Meta.DATE -> 0.3f
        }
    }

    //BODY -> 0.3 AUTHOR -> 0.3 TITLE -> 0.3 DATE -> 0.1
    //a0*0.3 + a1*0.3 + a2*0.3 + a3*0.1

    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        val resList: MutableList<Int> = ArrayList()
        var i = 0
        var j = 0
        while (i < vector1.size && j < vector2.size) {
            when {
                distance >= Math.abs(vector2[j] - vector1[i]) -> {
                    resList.add(vector1[i++])
                    ++j
                }
                distance < Math.abs(vector2[j] - vector1[i]) -> ++i
                else -> ++j
            }
        }

        return resList
    }

}