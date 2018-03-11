import java.lang.Math.abs

class Dictionary {

    var list: MutableList<String> = ArrayList()

    var invertedIndex: HashMap<String, HashMap<Int, MutableList<Meta>>> = HashMap()

    var bodyIndex: HashMap<String, MutableList<Int>> = HashMap()//body

    var titleIndex: HashMap<String, MutableList<Int>> = HashMap()

    var authorIndex: HashMap<String, MutableList<Int>> = HashMap()

    var releaseDateIndex: HashMap<String, MutableList<Int>> = HashMap()

    fun addDocument(document: Document) {

        val documentId = document.id

        val tokens = document.dictionary

        tokens.forEach { k ->
            if (!bodyIndex.contains(k)) {
                bodyIndex[k] = ArrayList()
                bodyIndex[k]!!.add(documentId)
                invertedIndex[k] = HashMap()
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.BODY)
            } else if (!bodyIndex[k]!!.contains(documentId)) {
                bodyIndex[k]!!.add(documentId)
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.BODY)
            }
        }

        document.title.forEach { k ->
            if (!titleIndex.containsKey(k)) {
                titleIndex[k] = ArrayList()
                titleIndex[k]!!.add(documentId)
                invertedIndex[k] = HashMap()
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.TITLE)
            } else if (!titleIndex[k]!!.contains(documentId)) {
                titleIndex[k]!!.add(documentId)
                if (!invertedIndex.containsKey(k))
                    invertedIndex[k] = HashMap()
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.TITLE)
            }
        }

        document.author.forEach { k ->
            if (!authorIndex.containsKey(k)) {
                authorIndex[k] = ArrayList()
                authorIndex[k]!!.add(documentId)
                invertedIndex[k] = HashMap()
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.AUTHOR)
            } else if (!authorIndex[k]!!.contains(documentId)) {
                authorIndex[k]!!.add(documentId)
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.AUTHOR)
            }
        }

        document.releaseDate.forEach { k ->
            if (!releaseDateIndex.containsKey(k)) {
                releaseDateIndex[k] = ArrayList()
                releaseDateIndex[k]!!.add(documentId)
                invertedIndex[k] = HashMap()
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.DATE)
            } else if (!releaseDateIndex[k]!!.contains(documentId)) {
                releaseDateIndex[k]!!.add(documentId)
                invertedIndex[k]!![documentId] = ArrayList()
                invertedIndex[k]!![documentId]!!.add(Meta.DATE)
            }
        }

        list.addAll(bodyIndex.keys)
    }

}