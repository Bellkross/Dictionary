import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class MyDocument(pathname: String, val id: Int) {

    /** Regex for splitting words */
    companion object {
        val regex: Regex = "([^а-яА-Яa-zA-Z']+)'*\\1*".toRegex()
    }

    /** All content of document in List<String> object */
    var dictionary: MutableList<String> = ArrayList()

    /** Position of each word in document */
    var positions: HashMap<String, MutableList<Int>> = HashMap()

    var title: List<String> = ArrayList()
    var author: List<String> = ArrayList()
    var releaseDate: List<String> = ArrayList()

    /** Adding positions for all words from document dictionary */
    init {

        val documentBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document: Document = documentBuilder.parse(pathname)

        title = title(document = document)
        author = author(document = document)
        releaseDate = date(document = document)

        var list: MutableList<Int>
        dictionary = body(document = document).filter { it != "" }.toMutableList()
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

    private fun title(document: Document): ArrayList<String> {
        var string = String()
        val result = ArrayList<String>()
        val pathFactory: XPathFactory = XPathFactory.newInstance()
        val xpath = pathFactory.newXPath()
        val expr = xpath.compile("//FictionBook/description/title-info")

        val nodes: NodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList
        var i = 0

        while (i < nodes.length) {
            val n: Node = nodes.item(i++)
            string += n.textContent
        }

        result.addAll(string.split(MyDocument.regex))
        result.removeAll(listOf(""," "))
        return result
    }

    private fun author(document: Document): ArrayList<String> {
        var string = String()
        val result = ArrayList<String>()
        val pathFactory: XPathFactory = XPathFactory.newInstance()
        val xpath = pathFactory.newXPath()
        val expr = xpath.compile("//FictionBook/description/title-info/author")

        val nodes: NodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList
        var i = 0

        while (i < nodes.length) {
            val n: Node = nodes.item(i++)
            string += n.textContent
        }

        result.addAll(string.split(MyDocument.regex))
        result.removeAll(listOf(""," "))
        return result
    }

    private fun date(document: Document): ArrayList<String> {
        var string = String()
        val result = ArrayList<String>()
        val pathFactory: XPathFactory = XPathFactory.newInstance()
        val xpath = pathFactory.newXPath()
        val expr = xpath.compile("//FictionBook/description/document-info/date")

        val nodes: NodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList
        var i = 0

        while (i < nodes.length) {
            val n: Node = nodes.item(i++)
            string += n.textContent
        }

        result.addAll(string.split(MyDocument.regex))
        result.removeAll(listOf(""," "))
        return result
    }

    private fun body(document: Document): ArrayList<String> {
        var string = String()
        val result = ArrayList<String>()
        val pathFactory: XPathFactory = XPathFactory.newInstance()
        val xpath = pathFactory.newXPath()
        val expr = xpath.compile("//FictionBook/body")

        val nodes: NodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList
        var i = 0

        while (i < nodes.length) {
            val n: Node = nodes.item(i++)
            string += n.textContent
        }

        result.addAll(string.split(MyDocument.regex))
        result.removeAll(listOf(""," "))
        return result
    }

}