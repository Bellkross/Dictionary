import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class Document(pathname: String, val id: Int) {

    /** Regex for splitting words */
    companion object {
        //val regex: Regex = "([^a-zA-Z']+)'*\\1*".toRegex()
        val regex: Regex = "([^а-яА-Яa-zA-Z']+)'*\\1*".toRegex()
        //val regex: Regex = "[ \r\n-,.-]".toRegex()
    }

    /** All content of document in String object */
    private val content: String = String(File(pathname).readBytes())

    /** All content of document in List<String> object */
    val dictionary: MutableList<String> = content.split(regex).filter { string -> string != "" }.toMutableList()

    /** Position of each word in document */
    var positions: TreeMap<String, MutableList<Int>> = TreeMap()

    /** wo-word dictionary and positions */
    val twDictionary: MutableList<String> = ArrayList()
    val twPositions: TreeMap<String, MutableList<Int>> = TreeMap()

    /** Adding positions for all words from document dictionary */
    init {
        var list: MutableList<Int>
        dictionary.forEachIndexed { i, k ->
            if (positions.containsKey(k)) {
                positions[k]?.add(i)
            } else {
                list = ArrayList(1)
                list.add(i)
                positions[k] = list
            }
        }

        dictionary.forEachIndexed { i, k ->
            if (i + 2 < dictionary.size && k != "") {
                twDictionary.add(k + " " + dictionary[i + 1])
                //println("$i -> ${k + " " + dictionary[i + 1]}")
            }
        }

        twDictionary.forEachIndexed { i, k ->
            if (twPositions.containsKey(k)) {
                twPositions[k]?.add(i)
            } else {
                list = ArrayList(1)
                list.add(i)
                twPositions[k] = list
            }
        }

    }
}