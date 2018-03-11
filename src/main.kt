import java.io.File
import java.util.TreeSet
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.toMutableList

const val mFilepath = "D:\\IntelIJ\\txt2\\"
var files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()

val helper = Helper()

fun main(args: Array<String>) {

    println(helper.range("AAA"))
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
    helper.mergeRange(File("D:\\IntelIJ\\Dictionary\\index").listFiles())
    val time2 = LocalTime.now().toNanoOfDay()
    println("Time of merging -> ${(time2 - time1) / 1000000000} sec")
    //println("All time -> ${(time2 - time11) / 1000000000} sec")
    */

}

fun indexing(): ArrayList<File> {

    val resFiles = ArrayList<File>()
    var fileInvertedIndex: File
    var doc: Document

    //val lim = 5049619648
    val lim = 108183696

    var j = 0
    var count = 0
    val minCount = 80

    files.forEachIndexed { i, file ->
        doc = Document(file.absolutePath, i)
        dictionary.addDocument(doc)
        if (Runtime.getRuntime().freeMemory() < lim || ++count > minCount) {
            println("free memory < $lim bytes or files > $minCount")
            fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\index\\${j}_${i}_invertedIndex.txt")
            resFiles.add(fileInvertedIndex)
            fileInvertedIndex.bufferedWriter().use { out ->
                dictionary.list = TreeSet<String>(dictionary.list).toMutableList()
                dictionary.list.forEach {
                    out.write("$it -> ${dictionary.invertedIndex[it]}\n")
                }
            }

            count = 0
            dictionary.invertedIndex = HashMap()
            dictionary.bodyIndex.clear()
            dictionary.authorIndex.clear()
            dictionary.releaseDateIndex.clear()
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