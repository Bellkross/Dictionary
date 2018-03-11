import java.io.File

class Document(pathname: String, val id: Int) {

    /** Regex for splitting words */
    companion object {
        val regex: Regex = "([^а-яА-Яa-zA-Z']+)'*\\1*".toRegex()
    }

    /** All content of document in String object */
    private var content: String = String(File(pathname).readBytes())

    /** All content of document in List<String> object */
    var dictionary: MutableList<String> = ArrayList()

    /** Position of each word in document */
    var positions: HashMap<String, MutableList<Int>> = HashMap()

    var title : List<String> = ArrayList()
    var author : List<String> = ArrayList()
    var releaseDate : List<String> = ArrayList()

    /** Adding positions for all words from document dictionary */
    init {
        var titleContent = content.substring(content.indexOf("Title: ")+"Title: ".length)
        var authorContent = content.substring(content.indexOf("Author: ")+"Author: ".length)
        var releaseDateContent = content.substring(content.indexOf("Release date: ")+"Release date: ".length)

        titleContent = titleContent.substring(0,titleContent.indexOf('\n'))
        authorContent = authorContent.substring(0,authorContent.indexOf('\n'))
        releaseDateContent = releaseDateContent.substring(0,releaseDateContent.indexOf('\n'))

        title = titleContent.split(regex)
        author = authorContent.split(regex)
        releaseDate = releaseDateContent.split(regex)


        content = content.substring(content.indexOf(releaseDateContent) + releaseDateContent.length+1)
        var list: MutableList<Int>
        dictionary = content.split(regex).filter{it != ""}.toMutableList()
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