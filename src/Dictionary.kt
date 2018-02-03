import java.util.*

class Dictionary {

    var list: MutableList<String> = LinkedList()
    var coordinateInvertedIndex: TreeMap<String, TreeMap<Int, MutableList<Int>>> = TreeMap()

    var twList: MutableList<String> = LinkedList()
    var twoWordCoordinateIndex: TreeMap<String, TreeMap<Int, MutableList<Int>>> = TreeMap()

    fun addDocument(document: Document) {
        val documentId = document.id

        val tokens = document.dictionary
        val positions: TreeMap<String, MutableList<Int>> = document.positions

        tokens.forEach { k ->
            if (!coordinateInvertedIndex.contains(k)) {
                coordinateInvertedIndex[k] = TreeMap()
                coordinateInvertedIndex[k]!![documentId] = positions[k]!!
            } else {
                if (!coordinateInvertedIndex[k]!!.contains(documentId)) {
                    coordinateInvertedIndex[k]!![documentId] = positions[k]!!
                }
            }
        }

        list.addAll(coordinateInvertedIndex.keys)
        list = TreeSet<String>(list).toMutableList()

        val twTokens = document.twDictionary
        val twPositions: TreeMap<String, MutableList<Int>> = document.twPositions

        twTokens.forEach { k ->
            if (!twoWordCoordinateIndex.contains(k)) {
                twoWordCoordinateIndex[k] = TreeMap()
                twoWordCoordinateIndex[k]!![documentId] = twPositions[k]!!
            } else {
                if (!twoWordCoordinateIndex[k]!!.containsKey(documentId)) {
                    twoWordCoordinateIndex[k]!![documentId] = twPositions[k]!!
                }
            }
        }

        twList.addAll(twoWordCoordinateIndex.keys)
        twList = TreeSet<String>(twList).toMutableList()
    }

    fun find(string: String, inputDistance: Int): TreeMap<Int, MutableList<Int>> {
        if (string.isEmpty()) {
            return TreeMap()
        }

        var distance = inputDistance
        var strings: MutableList<String> = string.split(Document.regex).toMutableList()
        var filesIndex: MutableList<Int>
        var wordsIndex: MutableList<Int>
        var result: TreeMap<Int, MutableList<Int>> = TreeMap()

        if (!coordinateInvertedIndex.containsKey(strings[0])) {
            return TreeMap()
        } else {
            filesIndex = coordinateInvertedIndex[strings[0]]!!.keys.toMutableList()
        }

        strings.forEach { key ->
            /** intersect for files need distance = 0 */
            filesIndex = intersect(filesIndex, coordinateInvertedIndex[key]!!.keys.toMutableList(), 0)
        }

        wordsIndex = coordinateInvertedIndex[strings[0]]!![filesIndex[0]]!!.toMutableList()
        filesIndex.forEach { fileId ->
            strings.forEach { key ->
                /** intersect for words need distance = 1 */
                if(key != strings[0])
                wordsIndex = intersect(wordsIndex, coordinateInvertedIndex[key]!![fileId]!!.toMutableList(), distance++)
            }
            if (wordsIndex.isNotEmpty())
                result[fileId] = wordsIndex
        }


        return result
    }

    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        val resList: MutableList<Int> = LinkedList()
        var i = 0
        var j = 0
        while (i < vector1.size && j < vector2.size) {
            when {
                vector1[i] == (vector2[j] - distance) -> {
                    resList.add(vector1[i++])
                    ++j
                }
                vector1[i] < (vector2[j] - distance) -> ++i
                else -> ++j
            }
        }

        return resList
    }

/*
    Hey Hello world again
    0.Hey Hello
    1.Hello world
    2.world again

    Hello world
    return: 0: 1

    Hello world again (distance)
    1. Hello world
    return: 0: 1
    2. world again
    return: 0: 2

    Hello world
    return: 0: 1, 0: 2
    */

}