import java.io.File
import smile.nlp.relevance.BM25
import smile.nlp.relevance.RelevanceRanker

class Document(pathname: String, val id: Int) {

    /** Regex for splitting words */
    companion object {
        val regex: Regex = "([^а-яА-Яa-zA-Z']+)'*\\1*".toRegex()
    }

    /** All content of document in String object */
    private var content: String = String(File(pathname).readBytes())

    /** All content of document in List<String> object */
    var dictionary: MutableList<String> = content.split(regex).map { it.toLowerCase() }.filter { it != "" }.toMutableList()

    /** Position of each word in document */
    var positions: HashMap<String, MutableList<Int>> = HashMap()

    /** Obviously */
    var tf: HashMap<String, Int> = HashMap()

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

            tf[k] = if (!tf.containsKey(k)) 0 else tf[k]!! + 1
        }
    }

}