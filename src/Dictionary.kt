import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Dictionary {

    var list: MutableList<String> = ArrayList()
    var invertedIndex: HashMap<String, MutableList<Int>> = HashMap()

    fun addDocument(document: Document) {
        val documentId = document.id

        val tokens = document.dictionary

        tokens.forEach { k ->
            if (!invertedIndex.contains(k)) {
                invertedIndex[k] = ArrayList()
                invertedIndex[k]!!.add(documentId)
            } else {
                if (!invertedIndex[k]!!.contains(documentId))
                    invertedIndex[k]!!.add(documentId)
            }
        }

        list.addAll(invertedIndex.keys)
    }


    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        val resList: MutableList<Int> = ArrayList()
        var i = 0
        var j = 0
        while (i < vector1.size && j < vector2.size) {
            when {
                distance >= abs(vector2[j] - vector1[i]) -> {
                    resList.add(vector1[i++])
                    ++j
                }
                distance < abs(vector2[j] - vector1[i]) -> ++i
                else -> ++j
            }
        }

        return resList
    }

}