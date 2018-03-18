import java.io.File
import java.time.LocalTime
import java.util.TreeSet
import kotlin.collections.ArrayList

const val mFilepath = "D:\\IntelIJ\\txt2\\"
var files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()
lateinit var clasterizator: Clasterizator

val helper = Helper()

fun main(args: Array<String>) {


    indxng()

    /*
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[0]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[1]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[2]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[3]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[4]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[5]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[6]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[7]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[8]!!.map { it.second }.toMutableList()))
    println(clasterizator.sim(clasterizator.docVectors[1]!!.map { it.second }.toMutableList(),clasterizator.docVectors[8]!!.map { it.second }.toMutableList()))
    */

    /*
    while (!helper.check(files)) {
        helper.split(files, mFilepath)
        files = File(mFilepath).listFiles()
    }
    val time11: Long = LocalTime.now().toNanoOfDay()
    indexing()
    val time22 = LocalTime.now().toNanoOfDay()
    println("Time of indexing -> ${(time22 - time11) / 1000000000} sec")

    val time1: Long = LocalTime.now().toNanoOfDay()
    helper.merge(File("D:\\IntelIJ\\Dictionary\\index").listFiles())
    val time2 = LocalTime.now().toNanoOfDay()
    println("Time of merging -> ${(time2 - time1) / 1000000000} sec")
    //println("All time -> ${(time2 - time11) / 1000000000} sec")
    */
}

fun indexing(): ArrayList<File> {

    val resFiles = ArrayList<File>()
    var fileInvertedIndex: File
    //var fileTf: File
    var doc: Document

    val lim = 108183696

    var j = 0
    var count = 0
    val minCount = 30

    files.forEachIndexed { i, file ->
        doc = Document(file.absolutePath, i)
        dictionary.addDocument(doc)
        if (Runtime.getRuntime().freeMemory() < lim || ++count > minCount) {
            println("free memory < $lim bytes or files > $minCount")
            //fileTf = File("D:\\IntelIJ\\Dictionary\\index\\${j}_${i}_tf.txt")
            fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\index\\${j}_${i}_invertedIndex.txt")
            resFiles.add(fileInvertedIndex)
            fileInvertedIndex.bufferedWriter().use { out ->
                dictionary.list = TreeSet<String>(dictionary.list).toMutableList()
                dictionary.list.forEach {
                    out.write("$it -> ${dictionary.invertedIndex[it]}\n")
                }
            }

            count = 0
            dictionary.invertedIndex.clear()
            dictionary.list.clear()
            j = i
        }
        println("file #$i / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")
        System.gc()
    }

    //prev+1 & this -> word
    fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\index\\last_invertedIndex.txt")
    fileInvertedIndex.bufferedWriter().use { out ->
        dictionary.list = TreeSet<String>(dictionary.list).toMutableList()
        dictionary.list.forEach {
            out.write("$it -> ${dictionary.invertedIndex[it]}\n")
        }
    }
    dictionary.invertedIndex.clear()
    dictionary.list.clear()
    println("last file / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")


    return resFiles

}

fun indxng() {

    files.forEachIndexed { i, file ->
        dictionary.addDocument(Document(file.absolutePath, i))
        println("file #${i} / ${files.size-1} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")
        System.gc()
    }

    clasterizator = Clasterizator(dictionary)

}