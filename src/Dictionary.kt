import java.util.*

class Dictionary {

    var list: MutableList<String> = LinkedList()
    var coordinateInvertedIndex: TreeMap<String, TreeMap<Int, MutableList<Int>>> = TreeMap()

    fun addDocument(document: Document) {
        var documentId = document.id
        var tokens = document.dictionary
        var wordPositions: TreeMap<String, MutableList<Int>> = document.positions

        tokens.forEach { k ->
            if (!coordinateInvertedIndex.contains(k)) {
                coordinateInvertedIndex[k] = TreeMap()
                coordinateInvertedIndex[k]!![documentId] = wordPositions[k]!!
            } else {
                if (!coordinateInvertedIndex[k]!!.contains(documentId)) {
                    coordinateInvertedIndex[k]!![documentId] = wordPositions[k]!!
                }
            }
        }

        list.addAll(coordinateInvertedIndex.keys)
        list = TreeSet<String>(list).toMutableList()
    }

    /*

    fun find(string: String, distance: Int): TreeMap<Int, MutableList<Int>> {
        if (string.isEmpty()) {
            return TreeMap()
        }

        var result: TreeMap<Int, MutableList<Int>> = TreeMap()
        var list: MutableList<String> = string.split(Document.regex).toMutableList()
        var invertedIndexList: MutableList<Int>

        if (!coordinateInvertedIndex.containsKey(list[0])) {
            return TreeMap()
        } else {
            invertedIndexList = coordinateInvertedIndex[list[0]]!!.keys.toMutableList()
        }

        list.forEachIndexed { i, key ->
            if (i > 0)
                invertedIndexList = intersect(invertedIndexList, coordinateInvertedIndex[key]!!.keys.toMutableList())
        }

        if (list.size >= invertedIndexList.size) {
            invertedIndexList.forEach { id ->
                list.forEach { key ->
                    //word1[i] = word2[i + distance] -> add i
                    //
                }
            }
        } else {
            list.forEach { key ->
                invertedIndexList.forEach { id ->

                }
            }
        }

        return result
    }

    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>): MutableList<Int> {
        val resList: MutableList<Int> = LinkedList()
        var i: Int = vector1.size
        var j: Int = vector2.size
        while (i > 0 && j > 0) {
            when {

                vector1[i] == vector2[j] -> {
                    resList.add(vector1[i--])
                    --j
                }

                vector1[i] < vector2[j] -> ++i

                else -> ++j
            }
        }

        return resList
    }

    private fun intersectWithDistance(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        var resList: MutableList<Int> = LinkedList()
        var i: Int = vector1.size
        var j: Int = vector2.size
        while (i > 0 && j > 0) {
            when {
                vector1[i] == vector2[j] -> {
                    resList.add(vector1[i--])
                    --j
                }
                vector1[i] < vector2[j] -> ++i
                else -> ++j
            }
        }

        return resList
    }

    */
}