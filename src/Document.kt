import java.io.File

class Document(pathname: String, val id: Int) {

    /** Regex for splitting words */
    companion object {
        val regex: Regex = "([^а-яА-Яa-zA-Z']+)'*\\1*".toRegex()
    }

    /** All content of document in String object */
    private val content: String = String(File(pathname).readBytes())

    /** All content of document in List<String> object */
    val dictionary: MutableList<String> = content.split(regex).filter{it != ""}.toMutableList()

    /** Position of each word in document */
    var positions: HashMap<String, MutableList<Int>> = HashMap()

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
    }

}