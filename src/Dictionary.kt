import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class Dictionary {

    var list: MutableList<String> = LinkedList()
    var coordinateInvertedIndex: HashMap<String, HashMap<Int, MutableList<Int>>> = HashMap()

    fun addDocument(document: Document) {
        var documentId = document.id
        var tokens = document.dictionary
        var wordPositions: HashMap<String, MutableList<Int>> = document.positions

        tokens.forEach { k ->
            if (!coordinateInvertedIndex.contains(k)) {
                coordinateInvertedIndex[k] = HashMap()
                coordinateInvertedIndex[k]!![documentId] = wordPositions[k]!!
            } else {
                if(!coordinateInvertedIndex[k]!!.contains(documentId)){
                    coordinateInvertedIndex[k]!![documentId] = wordPositions[k]!!
                }
            }
        }

        list.addAll(coordinateInvertedIndex.keys)
        list = HashSet<String>(list).toMutableList()
        list.sort()
    }

    fun find(string : String, distance : Int){
        var list: MutableList<String> = string.split(Document.regex).toMutableList()


    }


}