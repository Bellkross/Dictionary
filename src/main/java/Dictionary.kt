import smile.nlp.relevance.BM25

class Dictionary {

    //document count
    var n = 0

    var list: MutableList<String> = ArrayList()

    var invertedIndex: HashMap<String, MutableList<Int>> = HashMap()

    //term frequency
    //key -> word, document id, value -> frequency
    var tf: HashMap<Pair<String, Int>, Int> = HashMap()

    var scoreBM25: HashMap<Pair<String, Int>, Double> = HashMap()
    var ranker: BM25 = BM25()


    fun addDocument(document: Document) {
        ++n

        val documentId = document.id

        document.dictionary.forEach { k ->
            if (!invertedIndex.contains(k)) {
                invertedIndex[k] = ArrayList()
                invertedIndex[k]!!.add(documentId)
            } else if (!invertedIndex[k]!!.contains(documentId)) {
                invertedIndex[k]!!.add(documentId)
            }

            val key: Pair<String, Int> = Pair(k,documentId)

            tf[key] = if (document.tf.containsKey(k)) {
                if (tf.containsKey(key))
                    tf[key]!! + document.tf[k]!!
                else
                    document.tf[k]!!
            } else 0

        }

        tf.keys.forEach{ key ->
            scoreBM25[key] = ranker.score(tf[key]!!.toDouble(), 3L, 1L)
        }

        list.addAll(invertedIndex.keys)
    }

}