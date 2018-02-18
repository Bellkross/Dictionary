import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Helper {

    fun permutationIndex(inputString: String): MutableList<String> {
        var string = "$inputString$"
        var i = string.length
        val result: MutableList<String> = LinkedList()
        while (--i >= 0) {
            result.add(string)
            string += string[0]
            string = string.substring(1)
        }

        //hello -> hello$, ello$h, llo$he, lo$hel, o$hell, $hello
        return result
    }

    fun changeJoker(inputString: String): String {
        var string = "$$inputString"
        while (string[string.length - 1] != '*') {
            string += string[0]
            string = string.substring(1)
        }

        return string
    }

    //for normal words
    fun kgramIndex(inputString: String, k: Int): MutableList<String> {
        var string = "$$inputString$"
        var i = string.length
        val result: MutableList<String> = LinkedList()
        while (--i >= k - 1) {
            result.add(string.substring(0, k))
            string = string.substring(1)
        }

        //castle -> $ca, cas, ast, stl, tle, le$
        return result
    }

    //for non-ended words
    fun kgramIndex2(inputString: String, k: Int): MutableList<String> {
        var string = "$$inputString"
        var i = string.length
        val result: MutableList<String> = LinkedList()
        while (--i >= k - 1) {
            result.add(string.substring(0, k))
            string = string.substring(1)
        }

        return result
    }

    //for non-started words
    fun kgramIndex3(inputString: String, k: Int): MutableList<String> {
        var string = "$inputString$"
        var i = string.length
        val result: MutableList<String> = LinkedList()
        while (--i >= k - 1) {
            result.add(string.substring(0, k))
            string = string.substring(1)
        }

        return result
    }

    //p1&p2 -> p1
    fun mergeFiles(path1: String, path2: String, resPath: String) {
        val file1 = File(path1)
        val file2 = File(path2)

        val res = HashMap<String, HashMap<Int, HashSet<Int>>>()

        var string1: String? = ""
        var string2: String? = ""
        var word1 = ""
        var word2 = ""
        var treeSetWords = TreeSet<String>()

        var resString = ""

        val fileResult = File(resPath)
        file1.bufferedReader().use { br1 ->
            file2.bufferedReader().use { br2 ->

                string1 = br1.readLine()
                string2 = br2.readLine()
                while (string1 != null && string2 != null) {

                    word1 = getWordFromCoordinateIndex(string1!!)
                    word2 = getWordFromCoordinateIndex(string2!!)
                    treeSetWords.add(word1)
                    treeSetWords.add(word2)
                    if (word1 == word2) {
                        res[word1] = parseStringToCoordinateIndex(string1!!)
                        parseStringToCoordinateIndex(string2!!).keys.forEach {
                            res[word1]!![it] = parseStringToCoordinateIndex(string2!!)[it]!!
                        }
                        resString += "$word1 -> ${res[word1].toString()}\n"
/*
                        fileResult.bufferedWriter().use { out ->
                            out.write("$word1 -> ${res[word1].toString()}\n")
                        }
*/
                        string1 = br1.readLine()
                        string2 = br2.readLine()
                    } else {
                        when {
                            treeSetWords.last()==word2 -> {
                                res[word1] = parseStringToCoordinateIndex(string1!!)
                                /*
                                fileResult.bufferedWriter().use { out ->
                                    out.write("$word1 -> ${res[word1].toString()}\n")
                                }
                                */
                                resString += "$word1 -> ${res[word1].toString()}\n"
                                string1 = br1.readLine()
                            }
                            else -> {
                                res[word2] = parseStringToCoordinateIndex(string2!!)
                                /*
                                fileResult.bufferedWriter().use { out ->
                                    out.write("$word2 -> ${res[word2].toString()}\n")
                                }
                                */
                                resString += "$word2 -> ${res[word2].toString()}\n"
                                string2 = br2.readLine()
                            }
                        }
                    }
                    treeSetWords.clear()
                }

                while (string1 == null && string2 != null) {
                    word2 = getWordFromCoordinateIndex(string2!!)
                    res[word2] = parseStringToCoordinateIndex(string2!!)
                    //fileResult.appendText("$word2 -> ${res[word2].toString()}\n")
                    resString += "$word2 -> ${res[word2].toString()}\n"
                    string2 = br2.readLine()
                }
                while (string1 != null && string2 == null) {
                    word1 = getWordFromCoordinateIndex(string1!!)
                    res[word1] = parseStringToCoordinateIndex(string1!!)
                    //fileResult.appendText("$word1 -> ${res[word1].toString()}\n")
                    resString += "$word1 -> ${res[word1].toString()}\n"
                    string1 = br1.readLine()
                }

            }
        }
        fileResult.bufferedWriter().use { out ->
            out.write(resString)
        }

    }

    fun getWordFromCoordinateIndex(inputString: String): String = inputString.substring(0, inputString.indexOf(" -> "))

    fun parseStringToCoordinateIndex(inputString: String): HashMap<Int, HashSet<Int>> {

        val result: HashMap<Int, HashSet<Int>> = HashMap()
        var string = inputString
        var coordinates: HashSet<Int>
        var id = 0

        string = string.substring(string.indexOf("{") + "{".length)

        while (string.indexOf("}") != 0) {
            id = Integer.parseInt(string.substring(
                    startIndex = 0,
                    endIndex = string.indexOf("=[")))
            string = string.substring(string.indexOf("=["))
            coordinates = parseStringToMutableList(string.substring(string.indexOf("=["), string.indexOf("]") + 1))
            if (string.contains("], ")) {
                string = string.substring(string.indexOf("], ") + "], ".length)
            } else {
                string = string.substring(string.indexOf("]") + "]".length)
            }
            result[id] = coordinates
        }

        return result
    }

    fun parseStringToMutableList(inputString: String): HashSet<Int> {

        if (inputString.length < 3) {
            return HashSet()
        }
        var string = inputString.substring(inputString.indexOf("[") + 1)
        val result = HashSet<Int>()
        while (string.indexOf("]") != 0) {
            string = if (string.contains(", ")) {
                result.add(Integer.parseInt(string.substring(0, string.indexOf(", "))))
                string.substring(string.indexOf(", ") + ", ".length)
            } else {
                result.add(Integer.parseInt(string.substring(0, string.indexOf("]"))))
                string.substring(string.indexOf("]"))
            }
        }

        return result
    }

    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        val resList: MutableList<Int> = LinkedList()
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