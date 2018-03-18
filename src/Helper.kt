import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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

    fun parseStringToIndex(inputString: String): TreeSet<Int> {
        val string = inputString.substring(inputString.indexOf("["))
        val result = TreeSet<Int>()
        result.addAll(string.removePrefix("[").removeSuffix("]").replace(" ", "").split(",").map { Integer.parseInt(it) })
        return result
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

}