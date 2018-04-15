import java.util.TreeSet
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.collections.MutableList
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.map
import kotlin.collections.set
import kotlin.collections.toMutableList
import kotlin.math.log2
import kotlin.math.sqrt

class Clasterizator(val dictionary: Dictionary) {

    //term frequency
    //key -> (word, document id), value -> frequency
    var tf: HashMap<Pair<String, Int>, Int> = dictionary.tf

    var scoreBM25: HashMap<Pair<String, Int>, Double> = dictionary.scoreBM25

    //document frequency
    //key -> word, value -> frequency
    var df: HashMap<String, Float> = HashMap()
    var idf: HashMap<String, Float> = HashMap()

    var tfidf: HashMap<Pair<String, Int>, Float> = HashMap()

    //key (doc id) -> value (list of tfidfs)
    var docVectors: HashMap<Int, MutableList<Pair<String, Float>>> = HashMap()

    //key (leader doc id) -> value (follower doc ids)
    var clasters: HashMap<Int, MutableList<Int>> = HashMap()

    var checked: HashSet<Int> = HashSet()

    init {
        genVectors()
        val eps = 0.02
        val count = sqrt(dictionary.n + 0f)
        var i = 0
        while (i < count) {
            clasters[dictionary.n - (++i)] = ArrayList()
            checked.add(dictionary.n - (i))
        }

        clasters.keys.forEach { leaderId ->
            docVectors.keys.forEach { followerId ->
                if (!checked.contains(followerId)&&
                        sim(docVectors[leaderId]!!.map { it.second }.toMutableList(),
                                docVectors[followerId]!!.map { it.second }.toMutableList()) > eps) {
                    clasters[leaderId]!!.add(followerId)
                    checked.add(followerId)
                }
            }
        }
    }

    private fun genVectors() {
        dictionary.list.forEach { k ->
            df[k] = if (df.containsKey(k)) df[k]!! + 1f else 1f
        }
        df.keys.forEach { k ->
            idf[k] = log2(dictionary.n / df[k]!!)
        }
        tf.keys.forEach { tfKey ->
            tfidf[tfKey] = tf[tfKey]!! * idf[tfKey.first]!!
        }

        var docId = 0
        while (docId < dictionary.n) {
            TreeSet<String>(dictionary.list).forEach {
                if (!docVectors.containsKey(docId))
                    docVectors[docId] = ArrayList()

                if (tfidf.containsKey(Pair(it, docId)))
                    docVectors[docId]!!.add(Pair(it, tfidf[Pair(it, docId)]!!))
                else
                    docVectors[docId]!!.add(Pair(it, 0f))

            }
            docId++
        }
    }

    fun sim(v1: MutableList<Float>, v2: MutableList<Float>) = mul(v1, v2) / (sqrt(mul(v1, v1)) * sqrt(mul(v2, v2)) + 0.00000000000001)


    private fun mul(v1: MutableList<Float>, v2: MutableList<Float>): Float {
        var result = 0f
        v1.forEachIndexed { i, _ -> result += v1[i] * v2[i] }
        return result
    }

}