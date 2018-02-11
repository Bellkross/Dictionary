import java.lang.Math.abs
import java.util.*

class Dictionary {

    val helper = Helper()

    var list: MutableList<String> = LinkedList()
    var coordinateInvertedIndex: TreeMap<String, TreeMap<Int, MutableList<Int>>> = TreeMap()

    var twList: MutableList<String> = LinkedList()
    var twoWordCoordinateIndex: TreeMap<String, TreeMap<Int, MutableList<Int>>> = TreeMap()


    ////////////////////////////////////////////////////////////////////////////////////////
    var tree: BTree<String, TreeMap<Int, MutableList<Int>>> = BTree()

    var permutationIndex: TreeMap<String, MutableList<String>> = TreeMap()

    var trigramList: TreeSet<String> = TreeSet()
    var trigramIndex: TreeMap<String, MutableList<String>> = TreeMap()

    var bigramList: TreeSet<String> = TreeSet()
    var bigramIndex: TreeMap<String, MutableList<String>> = TreeMap()
    ////////////////////////////////////////////////////////////////////////////////////////

    fun addDocument(document: Document) {
        val documentId = document.id

        //coordinate index + tree
        val tokens = document.dictionary
        val positions: TreeMap<String, MutableList<Int>> = document.positions

        tokens.forEach { k ->
            if (!coordinateInvertedIndex.contains(k)) {
                coordinateInvertedIndex[k] = TreeMap()
                coordinateInvertedIndex[k]!![documentId] = positions[k]!!
                //tree
                tree.put(k, TreeMap())
                tree.get(k)[documentId] = positions[k]!!

            } else if (!coordinateInvertedIndex[k]!!.contains(documentId)) {
                coordinateInvertedIndex[k]!![documentId] = positions[k]!!

                //tree
                tree.get(k)[documentId] = positions[k]!!
            }
        }

        list.addAll(coordinateInvertedIndex.keys)
        list = TreeSet<String>(list).toMutableList()

        //two-word index
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
        ////////////////////////////////////////////////////////////////////////////////////////

        //permutation index
        list.forEach {
            permutationIndex[it] = helper.permutationIndex(it)
        }

        //trigramIndex
        list.forEach {
            helper.kgramIndex(it,3).forEach { trigram ->
                if (!trigramList.contains(trigram)) {
                    trigramList.add(trigram)
                    val list = LinkedList<String>()
                    list.add(it)
                    trigramIndex[trigram] = list
                } else {
                    if (!trigramIndex[trigram]!!.contains(it))
                        trigramIndex[trigram]!!.add(it)
                }
            }
        }

        //bigramIndex
        list.forEach {
            helper.kgramIndex(it,2).forEach { bigram ->
                if (!bigramList.contains(bigram)) {
                    bigramList.add(bigram)
                    val list = LinkedList<String>()
                    list.add(it)
                    bigramIndex[bigram] = list
                } else {
                    if (!bigramIndex[bigram]!!.contains(it))
                        bigramIndex[bigram]!!.add(it)
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////
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
                if (key != strings[0])
                    wordsIndex = intersect(wordsIndex, coordinateInvertedIndex[key]!![fileId]!!.toMutableList(), distance++)
            }
            if (wordsIndex.isNotEmpty())
                result[fileId] = wordsIndex
        }


        return result
    }

    fun trigramFind(inputString: String): TreeMap<Int, MutableList<Int>> {

        val k = 3;

        var result: TreeMap<Int, MutableList<Int>> = TreeMap()
        var listOfWords: MutableList<String> = LinkedList()
        val list = inputString.split('*')
        var string1 = ""
        var string2 = ""
        var trigrams: MutableList<String> = LinkedList()

        //check and write string1 & string2
        when {
            list.size == 2 && (list[0] == "" || list[1] == "") -> {
                when {
                    list[1] == "" -> {
                        string1 = "${list[0]}"
                        trigrams = helper.kgramIndex2(string1,k)
                        println("$string1 -> $trigrams")
                    }
                    else -> {
                        string1 = "${list[1]}"
                        trigrams = helper.kgramIndex3(string1,k)
                        println("$string1 -> $trigrams")
                    }
                }

                trigrams.forEach {
                    if (trigramIndex.containsKey(it)) {
                        listOfWords = if (listOfWords.isEmpty()) {
                            trigramIndex[it]!!
                        } else {
                            wordsIntersect(listOfWords, trigramIndex[it]!!)
                        }
                    }
                }
                //end of first part
            }
            else -> {
                //we need more trigrams
                string1 = "${list[0]}"
                //if string1.size <= 2
                //...
                string2 = "${list[list.lastIndex]}"
                //if string2.size <= 2
                //...
                trigrams = helper.kgramIndex2(string1,k)
                trigrams.addAll(helper.kgramIndex3(string2,k))

                println("$trigrams")
                //trigrams.foreach intersect trigram list
                trigrams.forEach {
                    if (listOfWords.isEmpty()) {
                        listOfWords = wordsIntersect(trigramIndex[it]!!, trigramIndex[it]!!)
                    } else {
                        listOfWords = wordsIntersect(listOfWords, trigramIndex[it]!!)
                    }
                }

            }
        }

        if (listOfWords.isEmpty()) return TreeMap()

        listOfWords.forEach {
            //here
            coordinateInvertedIndex[it]!!.keys.forEach { k ->
                if (!result.containsKey(k))
                    result[k] = coordinateInvertedIndex[it]!![k]!!
                else
                    result[k]!!.addAll(coordinateInvertedIndex[it]!![k]!!)
            }
        }

        return result
    }

    fun bigramFind(inputString: String): TreeMap<Int, MutableList<Int>> {

        val k = 2;

        var result: TreeMap<Int, MutableList<Int>> = TreeMap()
        var listOfWords: MutableList<String> = LinkedList()
        val list = inputString.split('*')
        var string1 = ""
        var string2 = ""
        var bigrams: MutableList<String> = LinkedList()

        //check and write string1 & string2
        when {
            list.size == 2 && (list[0] == "" || list[1] == "") -> {
                when {
                    list[1] == "" -> {
                        string1 = "${list[0]}"
                        bigrams = helper.kgramIndex2(string1,k)
                        println("$string1 -> $bigrams")
                    }
                    else -> {
                        string1 = "${list[1]}"
                        bigrams = helper.kgramIndex3(string1,k)
                        println("$string1 -> $bigrams")
                    }
                }

                bigrams.forEach {
                    if (bigramIndex.containsKey(it)) {
                        listOfWords = if (listOfWords.isEmpty()) {
                            bigramIndex[it]!!
                        } else {
                            wordsIntersect(listOfWords, bigramIndex[it]!!)
                        }
                    }
                }
                //end of first part
            }
            else -> {
                //we need more bigrams
                string1 = "${list[0]}"
                //if string1.size <= 2
                //...
                string2 = "${list[list.lastIndex]}"
                //if string2.size <= 2
                //...
                bigrams = helper.kgramIndex2(string1,k)
                bigrams.addAll(helper.kgramIndex3(string2,k))

                println("$bigrams")
                //bigrams.foreach intersect trigram list
                bigrams.forEach {
                    if (listOfWords.isEmpty()) {
                        listOfWords = wordsIntersect(bigramIndex[it]!!, bigramIndex[it]!!)
                    } else {
                        listOfWords = wordsIntersect(listOfWords, bigramIndex[it]!!)
                    }
                }

            }
        }

        if (listOfWords.isEmpty()) return TreeMap()

        listOfWords.forEach {
            //here
            coordinateInvertedIndex[it]!!.keys.forEach { k ->
                if (!result.containsKey(k))
                    result[k] = coordinateInvertedIndex[it]!![k]!!
                else
                    result[k]!!.addAll(coordinateInvertedIndex[it]!![k]!!)
            }
        }

        return result
    }

    private fun intersect(vector1: MutableList<Int>, vector2: MutableList<Int>, distance: Int): MutableList<Int> {
        val resList: MutableList<Int> = LinkedList()
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

    private fun wordsIntersect(words1: MutableList<String>, words2: MutableList<String>): MutableList<String> {
        val resList: MutableList<String> = LinkedList()
        when {
            (words1.size > words2.size) -> {
                words2.forEach {
                    if (words1.contains(it))
                        resList.add(it)
                }
            }
            else -> {
                words1.forEach {
                    if (words2.contains(it))
                        resList.add(it)
                }
            }
        }
        return resList
    }

}