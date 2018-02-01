import java.io.File

class Document(pathname: String, val id: Int) {

    /** Regex for splitting words */
    companion object {
        val regex: Regex = "([^a-zA-Z']+)'*\\1*".toRegex()
        //val regex: Regex = "[ \r\n-,.-]".toRegex()
    }
    /** All content of document in String object */
    private val content: String = String(File(pathname).readBytes())

    /** All content of document in List<String> object */
    val dictionary: MutableList<String> = content.split(regex).toMutableList()

    /** Position of each word in document */
    var positions: HashMap<String, MutableList<Int>> = HashMap(30000)

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